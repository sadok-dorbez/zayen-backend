package com.zayen.controllers;

import com.zayen.dto.request.CartItemRequest;
import com.zayen.dto.response.CartItemResponse;
import com.zayen.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<CartItemResponse> addToCart(@RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartService.addToCart(request.getClientId(), request.getItemId(), request.getQuantity()));
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long id) {
        cartService.removeFromCart(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}/{qty}")
    public ResponseEntity<CartItemResponse> updateQuantity(@PathVariable Long id, @PathVariable int qty) {
        return ResponseEntity.ok(cartService.updateQuantity(id, qty));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<CartItemResponse>> getCartByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(cartService.getCartByClient(clientId));
    }

    @DeleteMapping("/clear/{clientId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long clientId) {
        cartService.clearCart(clientId);
        return ResponseEntity.noContent().build();
    }
}