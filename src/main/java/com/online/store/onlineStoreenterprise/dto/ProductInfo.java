package com.online.store.onlineStoreenterprise.dto;

import com.online.store.onlineStoreenterprise.models.Product;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class ProductInfo {
    private  UUID id;
    private  String brand;
    private  String categories;
    private  String name;
    private  long sku;
    private  int stockQty;
    private  double price;
    private  String description;

    public ProductInfo(Product product)
    {
        id = product.getId();
        name = product.getName();
        brand = product.getBrand();
        sku = product.getSku();
        categories = product.getCategories();
        stockQty = product.getStockQty();
        price = product.getPrice();
        description = product.getDescription();
    }
}
