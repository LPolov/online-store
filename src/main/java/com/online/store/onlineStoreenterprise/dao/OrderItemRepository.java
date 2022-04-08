package com.online.store.onlineStoreenterprise.dao;

import com.online.store.onlineStoreenterprise.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
}
