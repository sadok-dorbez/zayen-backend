package com.zayen.repositories;

import com.zayen.entities.Item;
import com.zayen.enumeration.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ItemRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {
    long countItemsByStatusAndClientId(ItemStatus status, Long clientId);

}
