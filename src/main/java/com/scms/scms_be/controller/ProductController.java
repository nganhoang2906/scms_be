package com.scms.scms_be.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scms.scms_be.dto.ProductDto;
import com.scms.scms_be.service.ProductService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequiredArgsConstructor
public class ProductController {

    @Autowired
    private final ProductService productService;

    @PostMapping("/admin/product")
    public ResponseEntity<ProductDto> addProduct(@ModelAttribute ProductDto productDto) throws IOException {
       
        return ResponseEntity.ok(productService.addProduct(productDto));
    }

    @GetMapping("/auth/get-all-product")
    public ResponseEntity<List<ProductDto>> getAllProduct() {
        return ResponseEntity.ok(productService.getAllProduct());
    }
    
    @GetMapping("/auth/get-product/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Integer productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @PutMapping("/admin/update-product/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Integer productId,
                                                 @RequestBody ProductDto newProduct) throws IOException {
        return ResponseEntity.ok(productService.updateProduct(productId, newProduct));
    }
}
