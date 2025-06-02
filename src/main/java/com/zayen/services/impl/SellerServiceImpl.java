package com.zayen.services.impl;

import com.zayen.dto.response.OrderResponseDTO;
import com.zayen.entities.Order;
import com.zayen.enumeration.OrderStatus;
import com.zayen.repositories.OrderRepository;
import com.zayen.services.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final OrderRepository orderRepository;

    @Override
    public List<OrderResponseDTO> getDeliveredOrdersBySeller(Long sellerId) {
        return orderRepository.findBySeller_IdAndStatus(sellerId, OrderStatus.DELIVERED)
                .stream()
                .map(order -> {
                    OrderResponseDTO dto = new OrderResponseDTO();
                    dto.setOrderId(order.getId());
                    dto.setClientId(order.getClient().getId());
                    dto.setSellerId(order.getSeller().getId());
                    dto.setStatus(order.getStatus());
                    dto.setCreatedAt(order.getOrderDate());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
