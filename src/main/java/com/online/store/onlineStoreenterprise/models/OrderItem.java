package com.online.store.onlineStoreenterprise.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="order_items")
public class OrderItem {
    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID id;

    private int quantity;
    private double price;
    private double amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = false,
            foreignKey = @ForeignKey(name = "ORDER_ITEMS_ORD_FK"))
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false,
            foreignKey = @ForeignKey(name = "ORDER_ITEMS_PROD_FK"))
    private Product product;

}
