package com.farmfresh.marketplace.OrchardCart.repository;
import com.farmfresh.marketplace.OrchardCart.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders,Integer> {
    List<Orders> findByUserId(Integer userId);
}
