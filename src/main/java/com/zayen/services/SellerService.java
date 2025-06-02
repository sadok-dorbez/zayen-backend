package com.zayen.services;

import com.zayen.dto.response.OrderResponseDTO;

import java.util.List;

public interface SellerService {
    List<OrderResponseDTO> getDeliveredOrdersBySeller(Long sellerId);
}
