package com.zayen.controllers;

import com.zayen.entities.Order;
import com.zayen.enumeration.PaymentMethod;
import com.zayen.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/place/{clientId}")
    public ResponseEntity<Order> placeOrder(
            @PathVariable Long clientId,
            @RequestParam PaymentMethod paymentMethod) {
        Order order = orderService.placeOrder(clientId, paymentMethod);
        return ResponseEntity.ok(order);
    }
}
