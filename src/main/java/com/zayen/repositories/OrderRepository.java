package com.zayen.repositories;

import com.zayen.entities.Order;
import com.zayen.enumeration.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository <Order, Long>{
    List<Order> findBySeller_IdAndStatus(Long sellerId, OrderStatus status);

}
