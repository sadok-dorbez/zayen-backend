package com.zayen.dto.response;

import com.zayen.enumeration.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderResponseDTO {
    private Long orderId;
    private Long clientId;
    private Long sellerId;
    private OrderStatus status;
    private LocalDateTime createdAt;
}
