package com.scms.scms_be.controller.General;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.scms.scms_be.model.dto.General.ProductDto;
import com.scms.scms_be.model.request.General.ProductRequest;
import com.scms.scms_be.service.General.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProductController {

    @Autowired
    private ProductService productService;

    // Tạo Product
    @PostMapping("/comad/create-product/{itemId}")
    public ResponseEntity<ProductDto> create(@PathVariable Long itemId, @RequestBody ProductRequest product) {
        ProductDto createdProduct = productService.createProduct(itemId, product);
        return ResponseEntity.ok(createdProduct);
    }

    // Lấy tất cả Product trong Item
    @GetMapping("/user/get-all-product-in-item/{itemId}")
    public ResponseEntity<List<ProductDto>> getAllByItem(@PathVariable Long itemId) {
        List<ProductDto> products = productService.getAllProductsByItem(itemId);
        return ResponseEntity.ok(products);
    }

    // Lấy Product theo ID
    @GetMapping("/user/get-product/{productId}")
    public ResponseEntity<ProductDto> getById(@PathVariable Long productId) {
        ProductDto product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    // Cập nhật Product
    @PutMapping("/comad/update-product/{productId}")
    public ResponseEntity<ProductDto> update(@PathVariable Long productId, @RequestBody ProductRequest product) {
        ProductDto updatedProduct = productService.updateProduct(productId, product);
        return ResponseEntity.ok(updatedProduct);
    }

    // Xóa Product
    @DeleteMapping("/comad/delete-product/{productId}")
    public ResponseEntity<String> delete(@PathVariable Long productId) {
        boolean deleted = productService.deleteProduct(productId);
        if (deleted) {
            return ResponseEntity.ok("Sản phẩm đã được xóa thành công.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
