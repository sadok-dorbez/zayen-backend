package com.zayen.dto.response;

import com.zayen.entities.CartItem;
import lombok.Data;

@Data
public class CartItemResponse {
    private Long id;
    private Long itemId;
    private String itemTitle;
    private int quantity;
    private double price;

    public CartItemResponse(CartItem cartItem) {
        this.id = cartItem.getId();
        this.itemId = cartItem.getItem().getId();
        this.itemTitle = cartItem.getItem().getTitle();
        this.quantity = cartItem.getQuantity();
        this.price = cartItem.getItem().getPrice();
    }
}