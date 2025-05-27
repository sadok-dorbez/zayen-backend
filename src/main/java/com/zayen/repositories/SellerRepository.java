package com.zayen.repositories;

import com.zayen.entities.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository <Seller, Long> {
}
