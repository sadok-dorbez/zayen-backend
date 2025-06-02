package com.zayen.services;

import com.zayen.dto.request.OrderStatusUpdateRequest;
import com.zayen.dto.response.OrderResponseDTO;

import java.util.List;

public interface AdminService {
    List<OrderResponseDTO> getAllOrders();
    void updateOrderStatus(OrderStatusUpdateRequest request);
}
