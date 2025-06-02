package com.zayen.dto.request;


import com.zayen.enumeration.ItemCategory;
import com.zayen.enumeration.Size;
import lombok.Data;

import java.awt.*;

@Data
public class ItemFilterCrit {
    private double price;
    private Size size;
    private Color color;
    private ItemCategory category;
    private String city;
    private int quantity;
}
