package com.scms.scms_be.service.General;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.entity.General.Item;
import com.scms.scms_be.model.entity.General.Product;
import com.scms.scms_be.repository.General.ItemRepository;
import com.scms.scms_be.repository.General.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private ItemRepository itemRepo;

    public Product createProduct(Long itemId, Product product) {
        Item item = itemRepo.findById(itemId)
                .orElseThrow(() -> new CustomException("Mặt hàng không tồn tại!", HttpStatus.NOT_FOUND));

        product.setItem(item);
        return productRepo.save(product);
    }

    public List<Product> getAllByItem(Long itemId) {
        return productRepo.findByItemItemId(itemId);
    }

    public Product getProductById(Long id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new CustomException("Không tìm thấy sản phẩm!", HttpStatus.NOT_FOUND));
    }

    public Product updateProduct(Long productId, Product updated) {
        Product existing = getProductById(productId);
        existing.setSerialNumber(updated.getSerialNumber());
        existing.setBatchId(updated.getBatchId());
        existing.setQrCode(updated.getQrCode());
        return productRepo.save(existing);
    }
}
