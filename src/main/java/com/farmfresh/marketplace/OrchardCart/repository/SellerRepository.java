package com.farmfresh.marketplace.OrchardCart.repository;

import com.farmfresh.marketplace.OrchardCart.model.Seller;
import com.farmfresh.marketplace.OrchardCart.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller,Integer> {
    Optional<Seller> findByBusinessName(String businessName);
}
