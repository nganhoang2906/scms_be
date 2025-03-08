package com.scms.scms_be.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scms.scms_be.model.entity.Category;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer > {

    Optional<Category> findById(Long categoryId);
 
}
