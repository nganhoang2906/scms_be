package com.scms.scms_be.service.General;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.dto.General.ProductDto;
import com.scms.scms_be.model.entity.General.Item;
import com.scms.scms_be.model.entity.General.Product;
import com.scms.scms_be.model.request.General.ProductRequest;
import com.scms.scms_be.repository.General.ItemRepository;
import com.scms.scms_be.repository.General.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private ItemRepository itemRepo;

    public ProductDto createProduct(Long itemId, ProductRequest newProduct) {
        Item item = itemRepo.findById(itemId)
                .orElseThrow(() -> new CustomException("Mặt hàng không tồn tại!", HttpStatus.NOT_FOUND));

        Product product = new Product();
        
        product.setItem(item);
        product.setSerialNumber(UUID.randomUUID().toString().substring(0, 8));
        product.setBatchNo(newProduct.getBatchNo());
        product.setQrCode(newProduct.getQrCode());
        return convertToDto(productRepo.save(product));
    }

    public List<ProductDto> getAllProductsByItem(Long itemId) {
        List<Product> products = productRepo.findByItemItemId(itemId);
        return products.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public ProductDto getProductById(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new CustomException("Không tìm thấy hàng hóa!", HttpStatus.NOT_FOUND));
        return convertToDto(product);
    }

    public ProductDto updateProduct(Long productId, ProductRequest updated) {
        Product existing = productRepo.findById(productId)
                .orElseThrow(() -> new CustomException("Hàng hóa không tồn tại!", HttpStatus.NOT_FOUND));

        existing.setBatchNo(updated.getBatchNo());
        existing.setQrCode(updated.getQrCode());
        return convertToDto(productRepo.save(existing));
    }

    public boolean deleteProduct(Long productId) {
        Optional<Product> product = productRepo.findById(productId);
        if (product.isPresent()) {
            productRepo.delete(product.get());
            return true;
        }
        return false;
    }

    private ProductDto convertToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setProductId(product.getProductId());
        dto.setItemId(product.getItem().getItemId());
        dto.setItemName(product.getItem().getItemName());
        dto.setTechnicalSpecifications(product.getItem().getTechnicalSpecifications());
        dto.setSerialNumber(product.getSerialNumber());
        dto.setBatchNo(product.getBatchNo());
        dto.setQrCode(product.getQrCode());
        dto.setCurrentCompanyId(product.getCurrentCompanyId());
        return dto;
    }
}