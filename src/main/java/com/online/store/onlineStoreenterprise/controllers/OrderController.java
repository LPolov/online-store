package com.online.store.onlineStoreenterprise.controllers;

import com.online.store.onlineStoreenterprise.models.Order;
import com.online.store.onlineStoreenterprise.models.Product;
import com.online.store.onlineStoreenterprise.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    OrderService orderService;

    @GetMapping("/")
    public String getAllOrders(Model model) {
        try {
            List<Order> orders = new ArrayList<Order>();
            orderService.getAllOrders().forEach(orders::add);

            model.addAttribute("orders", orders);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "order/index";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") UUID id, Model model)
    {
        try {
            Order order = orderService.getOrderById(id);
            model.addAttribute("order", order);
            return "order/detail";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/api/orders/";
        }
    }
}
