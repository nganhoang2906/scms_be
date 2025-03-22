package com.scms.scms_be.repository.General;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scms.scms_be.model.entity.General.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByItemItemId(Long itemId);
}
