package com.ms.e_project.Services.auth;

import com.ms.e_project.Entities.Order;
import com.ms.e_project.Entities.OrderStatus;
import com.ms.e_project.Entities.User;
import com.ms.e_project.Entities.UserRole;
import com.ms.e_project.Repository.OrderRepository;
import com.ms.e_project.Repository.UserRepository;
import com.ms.e_project.dto.SignupRequest;
import com.ms.e_project.dto.UserDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Changed to PasswordEncoder interface

    @Autowired
    private OrderRepository orderRepository;

    public UserDto createUser(SignupRequest signupRequest) {
        User user = new User();
        user.setName(signupRequest.getName());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setEmail(signupRequest.getEmail());
        user.setRole(UserRole.CUSTOMER);
        User createdUser = userRepository.save(user);
        Order order = new Order();
        order.setAmount(0L);
        order.setTotalAmount(0L);
        order.setDiscount(0L);
        order.setUser(createdUser);
        order.setOrderStatus(OrderStatus.Pending);
        orderRepository.save(order);

        UserDto userDto = new UserDto();
        userDto.setId(createdUser.getId());
        userDto.setEmail(createdUser.getEmail());
        userDto.setName(createdUser.getName());
        userDto.setUserRole(createdUser.getRole());
        return userDto;
    }

    public Boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }

    @PostConstruct
    public void createAdminAccount() {
        User adminAccount = userRepository.findByRole(UserRole.ADMIN);
        if (adminAccount == null) {
            User user = new User();
            user.setEmail("admin@test.com");
            user.setName("admin");
            user.setRole(UserRole.ADMIN);
            user.setPassword(passwordEncoder.encode("admin123"));
            userRepository.save(user);
        }
    }
}
