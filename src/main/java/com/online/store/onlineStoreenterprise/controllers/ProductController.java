package com.online.store.onlineStoreenterprise.controllers;

import com.online.store.onlineStoreenterprise.dto.LoginRequest;
import com.online.store.onlineStoreenterprise.dto.SaveProductRequest;
import com.online.store.onlineStoreenterprise.dto.UpdateProductRequest;
import com.online.store.onlineStoreenterprise.models.Product;
import com.online.store.onlineStoreenterprise.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;
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
    public String createProduct(SaveProductRequest product, Model model, RedirectAttributes redirAttrs) {
        try {
            Product _product = productService.addProduct(product);
            redirAttrs.addFlashAttribute("success", "Product added successfully.");
            return "redirect:/api/products/";
        } catch (Exception e) {
            redirAttrs.addFlashAttribute("error", e.getMessage());
            return "product/create";
        }
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") UUID id, Model model, RedirectAttributes redirAttrs) {
        try {
            Product product = productService.getProductById(id);
            model.addAttribute("product", product);
            return "product/detail";
        } catch (Exception e) {
            redirAttrs.addFlashAttribute("error", e.getMessage());
            return "redirect:/api/products/";
        }
    }

    @GetMapping("/update/{id}")
    public String edit(@PathVariable("id") UUID id, Model model, RedirectAttributes redirAttrs) {
        try {
            Product product = productService.getProductById(id);
            model.addAttribute("product", product);
            return "product/edit";
        } catch (Exception e) {
            redirAttrs.addFlashAttribute("error", e.getMessage());
            return "redirect:/api/products/";
        }
    }

    @PostMapping("/update/{id}")
    public String updateDevice(@PathVariable("id") UUID id, UpdateProductRequest product, Model model, RedirectAttributes redirAttrs) {
        try {
            Product _product = productService.updateProduct(id, product);
            redirAttrs.addFlashAttribute("success", "Product updated successfully.");
        } catch (Exception e) {
            redirAttrs.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/api/products/";
    }

    @GetMapping("/delete")
    public String deleteDevice(@RequestParam("id") UUID id, Model model, RedirectAttributes redirAttrs) {
        try {
            productService.deleteProduct(id);
            redirAttrs.addFlashAttribute("success", "Product deleted successfully.");
        } catch (Exception e) {
            redirAttrs.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/api/products/";
    }
}
