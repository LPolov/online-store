package com.online.store.onlineStoreenterprise.services;

import com.online.store.onlineStoreenterprise.dao.ProductRepository;
import com.online.store.onlineStoreenterprise.dto.SaveProductRequest;
import com.online.store.onlineStoreenterprise.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public Product getProductById(UUID id) {
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public Product addProduct(SaveProductRequest product) {
        Product _product = productRepository.save(new Product(product));
        return _product;
    }
}
