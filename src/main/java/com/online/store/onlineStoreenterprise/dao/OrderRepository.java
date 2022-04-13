package com.online.store.onlineStoreenterprise.dao;

import com.online.store.onlineStoreenterprise.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> { }
