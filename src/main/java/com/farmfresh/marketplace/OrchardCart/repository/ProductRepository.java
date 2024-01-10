package com.farmfresh.marketplace.OrchardCart.repository;

import com.farmfresh.marketplace.OrchardCart.model.Category;
import com.farmfresh.marketplace.OrchardCart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    Optional<Product> findByCategoryId(Long category_id);
    @Query("SELECT p.seller.userInfo.email FROM Product p WHERE p.id = :productId")
    String findSellerEmailByProductId(@Param("productId") Integer productId);
}
