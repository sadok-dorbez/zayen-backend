package com.zayen.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zayen.enumeration.Color;
import com.zayen.enumeration.ItemCategory;
import com.zayen.enumeration.ItemStatus;
import com.zayen.enumeration.Size;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ref;
    private String title;
    private double price;
    private String description;
    private String ownerName;



    @Enumerated(EnumType.STRING)
    private ItemStatus status;
    @Enumerated(EnumType.STRING)
    private Size size;
    @Enumerated(EnumType.STRING)
    private Color color;
    @Enumerated(EnumType.STRING)
    private ItemCategory category;


    @ManyToOne
    @JoinColumn(name = "seller_id")
    @JsonIgnore
    private Seller seller;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonIgnore
    private Client client;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<Image> images;

}
