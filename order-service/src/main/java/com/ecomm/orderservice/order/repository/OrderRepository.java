package com.ecomm.orderservice.order.repository;

import com.ecomm.orderservice.order.entity.Order;
import com.ecomm.orderservice.order.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID>, JpaSpecificationExecutor<Order> {

    Page<Order> findByUserId(UUID userId, Pageable pageable);

    //  Review-service aid
    boolean existsByUserIdAndOrderStatusAndItemsProductId(
            UUID userId, OrderStatus orderStatus, UUID productId);
}
