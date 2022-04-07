package com.online.store.onlineStoreenterprise.services;

import com.online.store.onlineStoreenterprise.dao.OrderItemRepository;
import com.online.store.onlineStoreenterprise.dto.SaveProductRequest;
import com.online.store.onlineStoreenterprise.models.OrderItem;
import com.online.store.onlineStoreenterprise.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class OrderItemService {

    @Autowired
    OrderItemRepository orderItemRepository;

    @Transactional
    public OrderItem add(OrderItem orderItem) {
        OrderItem _orderItem = orderItemRepository.save(orderItem);
        return _orderItem;
    }
}
