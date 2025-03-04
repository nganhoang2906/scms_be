package com.scms.scms_be.service;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scms.scms_be.dto.ProductDto;
import com.scms.scms_be.entity.Category;
import com.scms.scms_be.entity.Product;
import com.scms.scms_be.repository.CategoryRepository;
import com.scms.scms_be.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    public ProductDto addProduct(ProductDto productDto) throws IOException{
        
        Product product = new Product();

        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setImg(productDto.getImg().getBytes());

        Category category = categoryRepo.findById(productDto.getCategoryId()).orElseThrow();

        product.setCategory(category);
        return productRepo.save(product).getDto();
    }
    public ProductDto updateProduct(Integer productId, ProductDto productDto) throws IOException {

        Product existingProduct = productRepo.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Product with ID " + productId + " not found"));
    
        existingProduct.setName(productDto.getName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setPrice(productDto.getPrice());
    
        if (productDto.getImg() != null) {
            existingProduct.setImg(productDto.getImg().getBytes());
        }
    
        // Cập nhật danh mục nếu có thay đổi
        if (productDto.getCategoryId() != null) {
            Category category = categoryRepo.findById(productDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category with ID " + productDto.getCategoryId() + " not found"));
            existingProduct.setCategory(category);
        }
    
        // Lưu lại sản phẩm đã cập nhật
        return productRepo.save(existingProduct).getDto();
    }
    

    public List<ProductDto> getAllProduct(){
        List<Product> products = productRepo.findAll();
        return products.stream().map(Product :: getDto).collect(Collectors.toList());
    }
    public ProductDto getProductById(Integer productId) {
        return productRepo.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Product with ID " + productId + " not found"))
            .getDto();
    }

}
