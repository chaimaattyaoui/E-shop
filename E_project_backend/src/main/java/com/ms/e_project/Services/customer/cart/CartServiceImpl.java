package com.ms.e_project.Services.customer.cart;

import com.ms.e_project.Entities.*;
import com.ms.e_project.Repository.*;
import com.ms.e_project.dto.AddProductInCartDto;
import com.ms.e_project.dto.CartItemsDto;
import com.ms.e_project.dto.OrderDto;
import com.ms.e_project.dto.PlaceOrderDto;
import com.ms.e_project.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartItemsRepository cartItemsRepository;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CouponRepository couponRepository;

    public ResponseEntity<?> addProductToCart(AddProductInCartDto dto) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(dto.getUserId(), OrderStatus.Pending);

        if (activeOrder == null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ValidationException("User not found"));

            activeOrder = new Order();
            activeOrder.setUser(user);
            activeOrder.setOrderStatus(OrderStatus.Pending);
            activeOrder.setAmount(0L);
            activeOrder.setTotalAmount(0L);
            activeOrder.setDiscount(0L);
            orderRepository.save(activeOrder);
        }

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ValidationException("Product not found"));

        Optional<CartItems> existingItem = cartItemsRepository
                .findByProductIdAndOrderIdAndUserId(product.getId(), activeOrder.getId(), dto.getUserId());

        if (existingItem.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Product already in cart!");
        }

        CartItems newItem = new CartItems();
        newItem.setProduct(product);
        newItem.setUser(activeOrder.getUser());
        newItem.setOrder(activeOrder);
        newItem.setQuantity(1L);
        newItem.setPrice(product.getPrice());

        cartItemsRepository.save(newItem);

        // Recalculate totalAmount based on all cart items
        long newTotal = activeOrder.getCartItems().stream()
                .mapToLong(ci -> ci.getPrice() * ci.getQuantity())
                .sum();
        activeOrder.setTotalAmount(newTotal);

        // Update amount (net amount) based on totalAmount and discount
        if (activeOrder.getCoupon() != null) {
            double discountDouble = (activeOrder.getCoupon().getDiscount() / 100.0) * activeOrder.getTotalAmount();
            long discount = Math.round(discountDouble);
            activeOrder.setDiscount(discount);
            activeOrder.setAmount(activeOrder.getTotalAmount() - discount);
        } else {
            activeOrder.setDiscount(0L);
            activeOrder.setAmount(activeOrder.getTotalAmount());
        }

        orderRepository.save(activeOrder);

        return ResponseEntity.ok("Product added to cart successfully");
    }

    public OrderDto getCartByUserId(Long userId) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.Pending);
        if (activeOrder == null) {
            // Return an empty cart if no active order exists
            return new OrderDto();
        }
        List<CartItemsDto> cartItemsDtoList = activeOrder.getCartItems().stream().map(CartItems::getCartDto).collect(Collectors.toList());
        OrderDto orderDto = new OrderDto();
        orderDto.setAmount(activeOrder.getAmount());
        orderDto.setId(activeOrder.getId());
        orderDto.setOrderStatus(activeOrder.getOrderStatus());
        orderDto.setDiscount(activeOrder.getDiscount());
        orderDto.setTotalAmount(activeOrder.getTotalAmount());
        orderDto.setCartItems(cartItemsDtoList);
        if (activeOrder.getCoupon() != null) {
            orderDto.setCouponName(activeOrder.getCoupon().getName());
        }
        return orderDto;
    }

    public OrderDto applyCoupon(Long userId, String code) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.Pending);
        if (activeOrder == null) throw new ValidationException("No active cart found");

        Coupon coupon = couponRepository.findByCode(code).orElseThrow(() -> new ValidationException("Coupon not found"));

        if (couponIsExpired(coupon)) {
            throw new ValidationException("Coupon is expired");
        }

        // Recalculate totalAmount to ensure accuracy
        long newTotal = activeOrder.getCartItems().stream()
                .mapToLong(ci -> ci.getPrice() * ci.getQuantity())
                .sum();
        activeOrder.setTotalAmount(newTotal);

        // Calculate discount based on totalAmount
        double discountAmountDouble = (coupon.getDiscount() / 100.0) * activeOrder.getTotalAmount();
        long discountAmount = Math.round(discountAmountDouble);
        long netAmount = activeOrder.getTotalAmount() - discountAmount;

        activeOrder.setDiscount(discountAmount);
        activeOrder.setAmount(netAmount);
        activeOrder.setCoupon(coupon);

        orderRepository.save(activeOrder);
        return activeOrder.getOrderDto();
    }

    private boolean couponIsExpired(Coupon coupon) {
        Date currentDate = new Date();
        Date expirationDate = coupon.getExpirationDate();
        return expirationDate != null && currentDate.after(expirationDate);
    }

    public OrderDto increaseProductQuantity(AddProductInCartDto dto) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(dto.getUserId(), OrderStatus.Pending);
        Optional<Product> optionalProduct = productRepository.findById(dto.getProductId());
        Optional<CartItems> optionalCartItem = cartItemsRepository.findByProductIdAndOrderIdAndUserId(
                dto.getProductId(), activeOrder.getId(), dto.getUserId());

        if (optionalProduct.isPresent() && optionalCartItem.isPresent()) {
            CartItems cartItem = optionalCartItem.get();
            Product product = optionalProduct.get();

            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cartItemsRepository.save(cartItem);

            long newTotal = activeOrder.getCartItems().stream()
                    .mapToLong(ci -> ci.getPrice() * ci.getQuantity())
                    .sum();
            activeOrder.setTotalAmount(newTotal);

            if (activeOrder.getCoupon() != null) {
                double discountDouble = (activeOrder.getCoupon().getDiscount() / 100.0) * activeOrder.getTotalAmount();
                long discount = Math.round(discountDouble);
                activeOrder.setDiscount(discount);
                activeOrder.setAmount(activeOrder.getTotalAmount() - discount);
            } else {
                activeOrder.setDiscount(0L);
                activeOrder.setAmount(activeOrder.getTotalAmount());
            }

            orderRepository.save(activeOrder);
            return activeOrder.getOrderDto();
        }
        return null;
    }

    public OrderDto decreaseProductQuantity(AddProductInCartDto dto) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(dto.getUserId(), OrderStatus.Pending);
        Optional<Product> optionalProduct = productRepository.findById(dto.getProductId());
        Optional<CartItems> optionalCartItem = cartItemsRepository.findByProductIdAndOrderIdAndUserId(
                dto.getProductId(), activeOrder.getId(), dto.getUserId());

        if (optionalProduct.isPresent() && optionalCartItem.isPresent()) {
            CartItems cartItem = optionalCartItem.get();
            Product product = optionalProduct.get();

            if (cartItem.getQuantity() <= 1) {
                cartItemsRepository.delete(cartItem);
            } else {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                cartItemsRepository.save(cartItem);
            }

            long newTotal = activeOrder.getCartItems().stream()
                    .mapToLong(ci -> ci.getPrice() * ci.getQuantity())
                    .sum();
            activeOrder.setTotalAmount(newTotal);

            if (activeOrder.getCoupon() != null) {
                double discountDouble = (activeOrder.getCoupon().getDiscount() / 100.0) * activeOrder.getTotalAmount();
                long discount = Math.round(discountDouble);
                activeOrder.setDiscount(discount);
                activeOrder.setAmount(activeOrder.getTotalAmount() - discount);
            } else {
                activeOrder.setDiscount(0L);
                activeOrder.setAmount(activeOrder.getTotalAmount());
            }

            orderRepository.save(activeOrder);
            return activeOrder.getOrderDto();
        }
        return null;
    }

    public OrderDto placeOrder(PlaceOrderDto placeOrderDto) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(placeOrderDto.getUserId(), OrderStatus.Pending);
        User user = userRepository.findById(placeOrderDto.getUserId())
                .orElseThrow(() -> new ValidationException("User not found"));

        if (activeOrder == null) {
            throw new ValidationException("No active cart found for this user");
        }

        activeOrder.setOrderDescription(placeOrderDto.getOrderDescription());
        activeOrder.setAddress(placeOrderDto.getAddress());
        activeOrder.setDate(new Date());
        activeOrder.setOrderStatus(OrderStatus.Placed);
        activeOrder.setTrackingId(UUID.randomUUID());
        orderRepository.save(activeOrder);

        Order pendingOrder = orderRepository.findByUserIdAndOrderStatus(user.getId(), OrderStatus.Pending);
        if (pendingOrder == null) {
            Order newOrder = new Order();
            newOrder.setAmount(0L);
            newOrder.setTotalAmount(0L);
            newOrder.setDiscount(0L);
            newOrder.setUser(user);
            newOrder.setOrderStatus(OrderStatus.Pending);
            orderRepository.save(newOrder);
        }

        return activeOrder.getOrderDto();
    }

    public List<OrderDto> getMyPlacedOrders(Long userId) {
        return orderRepository.findByUserIdAndOrderStatusIn(userId, List.of(OrderStatus.Placed, OrderStatus.Shipped, OrderStatus.Delivered)).stream().map(Order::getOrderDto).collect(Collectors.toList());
    }

    public OrderDto searchOrderByTrackingId(UUID trackingId) {
        Optional<Order> optionalOrder = orderRepository.findByTrackingId(trackingId);
        if (optionalOrder.isPresent()) {
            return optionalOrder.get().getOrderDto();
        }
        return null;
    }

    public OrderDto removeProductFromCart(Long userId, Long productId) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.Pending);
        if (activeOrder == null) {
            throw new ValidationException("No active cart found");
        }

        CartItems cartItem = cartItemsRepository.findByProductIdAndOrderIdAndUserId(productId, activeOrder.getId(), userId)
                .orElseThrow(() -> new ValidationException("Product not in cart"));

        long productTotal = cartItem.getPrice() * cartItem.getQuantity();

        activeOrder.setAmount(activeOrder.getAmount() - productTotal);
        activeOrder.setTotalAmount(activeOrder.getTotalAmount() - productTotal);

        cartItemsRepository.delete(cartItem);
        orderRepository.save(activeOrder);

        return activeOrder.getOrderDto();
    }
}