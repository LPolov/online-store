package com.online.store.onlineStoreenterprise.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SaveProductRequest {
    private final String brand;
    private final String categories;
    private final String name;
    private final int stockQty;
    private final double price;
    private final String description;
}
