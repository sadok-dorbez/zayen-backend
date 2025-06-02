package com.zayen.controllers;

import com.zayen.dto.response.OrderResponseDTO;
import com.zayen.services.SellerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sellers")
public class SellerController {

    private final SellerService sellerService;

    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @GetMapping("/{sellerId}/orders/delivered")
    public ResponseEntity<List<OrderResponseDTO>> getDeliveredOrders(@PathVariable Long sellerId) {
        List<OrderResponseDTO> orders = sellerService.getDeliveredOrdersBySeller(sellerId);
        return ResponseEntity.ok(orders);
    }
}
