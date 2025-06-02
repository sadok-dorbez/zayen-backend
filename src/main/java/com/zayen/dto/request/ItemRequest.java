package com.zayen.dto.request;

import com.zayen.enumeration.Color;
import com.zayen.enumeration.ItemCategory;
import com.zayen.enumeration.Size;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class ItemRequest {
    private String title;
    private double price;
    private String description;
    private String ownerName;
    private int quantity;
    private String city;
    private Size size;
    private Color color;
    private ItemCategory category;
    private Long sellerId;
}

