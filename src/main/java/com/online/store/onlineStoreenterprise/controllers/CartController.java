package com.online.store.onlineStoreenterprise.controllers;

import com.online.store.onlineStoreenterprise.dto.ProductInfo;
import com.online.store.onlineStoreenterprise.models.CartInfo;
import com.online.store.onlineStoreenterprise.models.Product;
import com.online.store.onlineStoreenterprise.services.OrderService;
import com.online.store.onlineStoreenterprise.services.ProductService;
import com.online.store.onlineStoreenterprise.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
@RequestMapping("/api/cart")
public class CartController {

    ProductService productService;
    OrderService orderService;

    @Autowired
    public CartController(ProductService productService, OrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;
    }

    @GetMapping("/show")
    public String shoppingCart(HttpServletRequest request, Model model) {
        CartInfo myCart = Util.getCartInSession(request);

        model.addAttribute("cartForm", myCart);
        return "cart/shoppingCart";
    }

    @GetMapping("/add/{id}")
    public String showCart(HttpServletRequest request,
                           Model model,
                           @PathVariable("id") UUID id) {
        Product product = productService.getProductById(id);
        if (product != null) {
            CartInfo cartInfo = Util.getCartInSession(request);
            ProductInfo productInfo = new ProductInfo(product);
            cartInfo.addProduct(productInfo, 1);
        }
        return "redirect:/api/cart/show";
    }

    @PostMapping("/update-qty")
    public String shoppingCartUpdateQty(CartInfo cartForm, HttpServletRequest request) {
        CartInfo cartInfo = Util.getCartInSession(request);
        cartInfo.updateQuantity(cartForm);
        return "redirect:/api/cart/show";
    }

    @GetMapping({ "/remove-item/{id}" })
    public String removeProductHandler(@PathVariable("id") UUID id, HttpServletRequest request, Model model) {
        Product product = productService.getProductById(id);
        if (product != null) {
            CartInfo cartInfo = Util.getCartInSession(request);
            ProductInfo productInfo = new ProductInfo(product);
            cartInfo.removeProduct(productInfo);
        }
        return "redirect:/api/cart/show";
    }

    @GetMapping("/confirm-review")
    public String shoppingCartConfirmationReview(HttpServletRequest request, Model model) {
        CartInfo cartInfo = Util.getCartInSession(request);
        if (cartInfo.isEmpty()) {
            return "redirect:/api/cart/show";
        }
        model.addAttribute("myCart", cartInfo);
        return "cart/confirmation-review";
    }

    @PostMapping("/order-confirm")
    public String orderConfirm(HttpServletRequest request, Model model) {
        CartInfo cartInfo = Util.getCartInSession(request);
        if (cartInfo.isEmpty()) {
            return "redirect:/api/cart/show";
        }
        try {
            orderService.saveOrder(cartInfo);
        } catch (Exception e) {
            return "cart/confirmation-review";
        }
        Util.removeCartInSession(request);
        Util.storeLastOrderedCartInSession(request, cartInfo);
        return "redirect:/api/cart/finalized";
    }

    @GetMapping("/finalized")
    public String shoppingCartFinalize(HttpServletRequest request, Model model) {
        CartInfo lastOrderedCart = Util.getLastOrderedCartInSession(request);
        if (lastOrderedCart == null) {
            return "redirect:/api/cart/show";
        }
        model.addAttribute("lastOrderedCart", lastOrderedCart);
        return "cart/finalized";
    }
}
