package com.online.store.onlineStoreenterprise.models;

import com.online.store.onlineStoreenterprise.dto.SaveProductRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="products")
public class Product {
    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID id;
    private long sku;
    private String brand;
    private String categories;
    private String name;
    private int stockQty;
    private double price;
    private String description;

    public Product(SaveProductRequest saveProductRequest) {
        brand = saveProductRequest.getBrand();
        categories = saveProductRequest.getCategories();
        name = saveProductRequest.getName();
        stockQty = saveProductRequest.getStockQty();
        price = saveProductRequest.getPrice();
        description = saveProductRequest.getDescription();
        sku = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
    }
}
