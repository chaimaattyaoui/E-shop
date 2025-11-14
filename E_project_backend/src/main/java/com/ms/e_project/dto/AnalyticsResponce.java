package com.ms.e_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnalyticsResponce {

    private Long placed;
    private Long shipped;
    private Long delivered;
    private Long currentMonthOrders;
    private Long previousMonthOrders;
    private Long currentMonthEarnings;
    private Long previousMonthEarnings;
}
