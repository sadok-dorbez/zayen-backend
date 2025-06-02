package com.zayen.dto.request;

import com.zayen.enumeration.OrderStatus;
import lombok.Data;

@Data
public class OrderStatusUpdateRequest {
    private Long orderId;
    private OrderStatus newStatus;
}