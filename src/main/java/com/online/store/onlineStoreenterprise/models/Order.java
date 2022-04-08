package com.online.store.onlineStoreenterprise.models;

import com.online.store.onlineStoreenterprise.models.authorization.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID id;
    private long orderNumber;
    private Date orderDate;
    private double amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "ORDER_USER_FK"))
    private User user;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "order")
    private List<OrderItem> orderItems;
}
