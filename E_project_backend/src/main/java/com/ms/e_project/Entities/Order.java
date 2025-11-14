package com.ms.e_project.Entities;

import com.ms.e_project.dto.OrderDto;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderDescription;
    private Date date;
    private Long amount;
    private String address;
    private String payment;

    @Enumerated(EnumType.STRING) // important
    private OrderStatus orderStatus;

    private Long totalAmount;
    private Long discount;
    private UUID trackingId;

    @ManyToOne(fetch = FetchType.LAZY) // ✅ correction : plusieurs orders par user
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY) // ✅ idem pour coupon
    @JoinColumn(name = "coupon_id", referencedColumnName = "id")
    private Coupon coupon;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL)
    private List<CartItems> cartItems;

    public OrderDto getOrderDto() {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(id);
        orderDto.setOrderDescription(orderDescription);
        orderDto.setDate(date);
        orderDto.setAmount(amount);
        orderDto.setAddress(address);
        orderDto.setOrderStatus(orderStatus);
        orderDto.setTrackingId(trackingId);
        orderDto.setUserName(user.getName());
        if (coupon != null) {
            orderDto.setCouponName(coupon.getName());
        }
        return orderDto;
    }
}
