package com.online.store.onlineStoreenterprise.controllers;

import com.online.store.onlineStoreenterprise.dto.LoginRequest;
import com.online.store.onlineStoreenterprise.dto.SaveProductRequest;
import com.online.store.onlineStoreenterprise.models.Product;
import com.online.store.onlineStoreenterprise.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/")
    public String getAllProducts(Model model) {
        try {
            List<Product> products = new ArrayList<Product>();
            productService.getAllProducts().forEach(products::add);

            model.addAttribute("products", products);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "product/index";
    }

    @GetMapping("/add")
    public String add(Product product) {
        return "product/create";
    }

    @PostMapping("/add")
    public String createProduct(SaveProductRequest product, Model model) {
        try {
            Product _product = productService.addProduct(product);
            return "redirect:/api/products/";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "product/create";
        }
    }

    @GetMapping("/detail/{id}")
    public String edit(@PathVariable("id") UUID id, Model model) {
        try {
            Product product = productService.getProductById(id);
            model.addAttribute("product", product);
            return "product/detail";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/api/products/";
        }
    }
}
