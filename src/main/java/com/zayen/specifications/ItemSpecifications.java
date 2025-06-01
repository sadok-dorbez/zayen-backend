package com.zayen.specifications;


import com.zayen.entities.Item;
import com.zayen.enumeration.ItemStatus;
import org.springframework.data.jpa.domain.Specification;

public class ItemSpecifications {



    public static Specification<Item> itemCategoryIs(String itemCategory) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("itemCategory"), itemCategory);
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
                criteriaBuilder.like(root.get("city"), "%" + city + "%");
    }

    public static Specification<Item> statusIs(ItemStatus status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }

}

