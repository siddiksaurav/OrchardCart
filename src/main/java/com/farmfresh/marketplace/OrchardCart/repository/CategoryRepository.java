package com.farmfresh.marketplace.OrchardCart.repository;

import com.farmfresh.marketplace.OrchardCart.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findByCategoryName(String categoryName);
    //@Query(value = "SELECT * FROM category ORDER BY RANDOM() LIMIT 4", nativeQuery = true)
    @Query(value = "SELECT * FROM category WHERE category_name IN ('Fruits', 'Vegetables', 'Root Vegetables')", nativeQuery = true)
    List<Category> findRandomCategories();
}
