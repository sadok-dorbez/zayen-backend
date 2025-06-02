package com.zayen.services;

import com.zayen.dto.response.CartItemResponse;

import java.util.List;

public interface CartService {
    CartItemResponse addToCart(Long clientId, Long itemId, int quantity);
    void removeFromCart(Long cartItemId);
    CartItemResponse updateQuantity(Long cartItemId, int newQty);
    List<CartItemResponse> getCartByClient(Long clientId);
    void clearCart(Long clientId);
}