package com.online.store.onlineStoreenterprise.controllers;

import com.online.store.onlineStoreenterprise.models.Order;
import com.online.store.onlineStoreenterprise.models.authorization.User;
import com.online.store.onlineStoreenterprise.services.OrderService;
import com.online.store.onlineStoreenterprise.services.authorization.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @GetMapping("/")
    public String getAllOrders(Model model, HttpServletRequest request) {
        try {
            User authenticatedUser = userService.getAuthenticatedUserFromRequest(request);
            List<Order> orders = orderService.getOrdersByUserId(authenticatedUser.getId());
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
