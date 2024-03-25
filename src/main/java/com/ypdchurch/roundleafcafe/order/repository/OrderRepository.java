package com.ypdchurch.roundleafcafe.order.repository;

import com.ypdchurch.roundleafcafe.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
