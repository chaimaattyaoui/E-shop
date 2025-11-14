package com.ms.e_project.Controllers.admin;
import com.ms.e_project.Services.admin.adminOrder.AdminOrderService;
import com.ms.e_project.dto.AnalyticsResponce;
import com.ms.e_project.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    @GetMapping("/placedOrders")
    public ResponseEntity<List<OrderDto>> getAllPlacedOrders() {
        return ResponseEntity.ok(adminOrderService.getAllPlacedOrders());
    }


    @GetMapping("/order/{orderId}/{status}")
    public ResponseEntity<?> changeOrderStatus(@PathVariable Long orderId,@PathVariable String status) {
        OrderDto orderDto = adminOrderService.changeOrderStatus(orderId,status);
        if(orderDto == null)
            return new ResponseEntity<>("Something went wrong!" , HttpStatus.BAD_REQUEST);
            return ResponseEntity.status(HttpStatus.OK).body(orderDto);

    }

    @GetMapping("/order/analytics")
    public ResponseEntity<AnalyticsResponce> getAnalytics() {
        return ResponseEntity.ok(adminOrderService.calculateAnalytics());
    }

}


