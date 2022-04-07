package com.online.store.onlineStoreenterprise.dao;

import com.online.store.onlineStoreenterprise.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

}
