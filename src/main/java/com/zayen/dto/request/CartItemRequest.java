package com.zayen.dto.request;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long clientId;
    private Long itemId;
    private int quantity;
}