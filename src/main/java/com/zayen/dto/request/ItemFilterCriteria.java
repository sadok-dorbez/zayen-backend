package com.zayen.dto.request;

import com.zayen.enumeration.Color;
import com.zayen.enumeration.ItemCategory;
import com.zayen.enumeration.Size;
import lombok.Data;

@Data
public class ItemFilterCriteria {

    private ItemCategory itemCategory;
    private Size itemSize;
    private Color itemColor;
    private double price;
    private double minPrice;
    private double maxPrice;
    private String city;
}
