package com.scms.scms_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scms.scms_be.model.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

}
