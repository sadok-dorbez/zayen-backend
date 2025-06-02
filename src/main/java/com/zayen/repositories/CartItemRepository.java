package com.zayen.repositories;

import com.zayen.entities.Cart;
import com.zayen.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByCart_Client_Id(Long clientId); // or findByCartClientId

    void deleteAllByCart_Client_Id(Long clientId); // for clearing cart

    List<CartItem> findByCart(Cart cart); // Optional alternative
    void deleteAllByCart_Id(Long cartId);
}
