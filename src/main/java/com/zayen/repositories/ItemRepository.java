package com.zayen.repositories;

import com.zayen.entities.Item;
import com.zayen.enumeration.ItemStatus;
import com.zayen.enumeration.TunisianCity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {
    long countItemsByStatusAndClientId(ItemStatus status, Long clientId);
    Page<Item> findBySellerId(Long sellerId, Pageable pageable);
    Page<Item> findBySellerIdAndStatus(Long sellerId,ItemStatus status, Pageable pageable);
    Page<Item> findByStatusOrderByIdDesc(ItemStatus status, Pageable pageable);
    Page<Item> findByStatusAndCityOrderByIdDesc(ItemStatus status, TunisianCity cityFilter, Pageable pageable);
    long countByStatus(ItemStatus status);


    List<Item> findTop5ByStatusOrderByIdDesc(ItemStatus status);
    List<Item> findByStatus(ItemStatus status);
    Page<Item> findByStatusAndCityOrderByIdDesc(ItemStatus status, String city, Pageable pageable);
}
