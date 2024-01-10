package com.farmfresh.marketplace.OrchardCart.repository;

import com.farmfresh.marketplace.OrchardCart.model.OrderItem;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,Integer> {
}
