package com.ms.e_project.Services.admin.adminOrder;

import com.ms.e_project.dto.AnalyticsResponce;
import com.ms.e_project.dto.OrderDto;

import java.util.List;

public interface AdminOrderService {
    public List<OrderDto> getAllPlacedOrders();
    public OrderDto changeOrderStatus(Long orderId, String status);
    AnalyticsResponce calculateAnalytics();
}
