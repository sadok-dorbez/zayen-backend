package com.zayen.specifications;

import com.zayen.entities.Item;
import com.zayen.enumeration.Color;
import com.zayen.enumeration.ItemCategory;
import com.zayen.enumeration.ItemStatus;
import com.zayen.enumeration.Size;
import org.springframework.data.jpa.domain.Specification;

public class ItemSpecifications {

    public static Specification<Item> itemCategoryIs(ItemCategory itemCategory) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("category"), itemCategory);
    }

    public static Specification<Item> itemSizeIs(Size size) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("size"), size);
    }

    public static Specification<Item> itemColorIs(Color color) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("color"), color);
    }

    public static Specification<Item> priceBetween(double minPrice, double maxPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
    }

    public static Specification<Item> priceGreaterThanOrEqual(double minPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Item> priceLessThanOrEqual(double maxPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public static Specification<Item> priceEqual(double price) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("price"), price);
    }

    public static Specification<Item> cityIsLike(String city) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("city")), "%" + city.toLowerCase() + "%");
    }

    public static Specification<Item> statusIs(ItemStatus status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Item> itemSizeIsLike(String size) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("size"), Size.valueOf(size.toUpperCase()));
    }

    public static Specification<Item> itemColorIsLike(String color) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("color"), Color.valueOf(color.toUpperCase()));
    }
}
