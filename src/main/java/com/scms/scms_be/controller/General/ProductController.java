package com.scms.scms_be.controller.General;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.scms.scms_be.model.entity.General.Product;
import com.scms.scms_be.service.General.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/comad/create-product/{itemId}")
    public ResponseEntity<Product> create(@PathVariable Long itemId, @RequestBody Product product) {
        return ResponseEntity.ok(productService.createProduct(itemId, product));
    }

    @GetMapping("/user/all-product-in-item/{itemId}")
    public ResponseEntity<List<Product>> getAllByItem(@PathVariable Long itemId) {
        return ResponseEntity.ok(productService.getAllByItem(itemId));
    }

    @GetMapping("/user/get-product/{productId}")
    public ResponseEntity<Product> getById(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @PutMapping("/comad/update-product/{productId}")
    public ResponseEntity<Product> update(@PathVariable Long productId, @RequestBody Product product) {
        return ResponseEntity.ok(productService.updateProduct(productId, product));
    }
}
