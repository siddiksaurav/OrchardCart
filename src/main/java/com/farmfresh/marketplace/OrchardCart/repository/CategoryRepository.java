package com.farmfresh.marketplace.OrchardCart.repository;

import com.farmfresh.marketplace.OrchardCart.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
}
