package com.online.store.onlineStoreenterprise.services;

import com.online.store.onlineStoreenterprise.dao.OrderItemRepository;
import com.online.store.onlineStoreenterprise.dao.OrderRepository;
import com.online.store.onlineStoreenterprise.models.*;
import com.online.store.onlineStoreenterprise.models.authorization.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    OrderRepository orderRepository;
    OrderItemService orderItemService;
    ProductService productService;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderItemService orderItemService, ProductService productService) {
        this.orderRepository = orderRepository;
        this.orderItemService = orderItemService;
        this.productService = productService;
    }

    @Transactional()
    public Order saveOrder(CartInfo cartInfo) {
        Order order = new Order();
        long orderNum = (long) Math.floor(Math.random() * 9_000_000L) + 1_000_000L;
        order.setOrderNumber(orderNum);
        order.setOrderDate(new Date());
        order.setAmount(cartInfo.getAmountTotal());

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        order.setUser(user);
        Order _order = orderRepository.save(order);
        List<CartLineInfo> lines = cartInfo.getCartLines();

        for (CartLineInfo line : lines) {
            OrderItem item = new OrderItem();
            item.setOrder(_order);
            item.setAmount(line.getAmount());
            item.setPrice(line.getProductInfo().getPrice());
            item.setQuantity(line.getQuantity());

            UUID productId = line.getProductInfo().getId();
            Product product = productService.getProductById(productId);
            item.setProduct(product);

            orderItemService.add(item);
        }

        cartInfo.setOrderNum(orderNum);
        return _order;
    }

    public List<Order> getOrdersByUserId(UUID userId) {
        return orderRepository.findAll()
            .stream()
            .filter(order -> isOrderBelongsToUser(order, userId))
            .collect(Collectors.toList());
    }

    private boolean isOrderBelongsToUser(Order order, UUID userId) {
        return order.getUser().getId().equals(userId);
    }

    public Order getOrderById(UUID id)
    {
        return orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Order not found"));
    }
}
