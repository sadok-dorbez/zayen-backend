package com.zayen.services.impl;

import com.zayen.dto.request.OrderStatusUpdateRequest;
import com.zayen.dto.response.OrderResponseDTO;
import com.zayen.entities.Order;
import com.zayen.enumeration.OrderStatus;
import com.zayen.exceptions.NotFoundException;
import com.zayen.repositories.OrderRepository;
import com.zayen.services.AdminService;
import com.zayen.services.impl.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final OrderRepository orderRepository;
    private final EmailService emailService;

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll().stream().map(order -> {
            OrderResponseDTO dto = new OrderResponseDTO();
            dto.setOrderId(order.getId());
            dto.setClientId(order.getClient().getId());
            dto.setSellerId(order.getSeller().getId());
            dto.setStatus(order.getStatus());
            dto.setCreatedAt(order.getOrderDate());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public void updateOrderStatus(OrderStatusUpdateRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new NotFoundException("Order not found"));

        order.setStatus(request.getNewStatus());
        orderRepository.save(order);

        emailService.sendOrderStatusUpdate(order); // Send mail to client and seller
    }
}
