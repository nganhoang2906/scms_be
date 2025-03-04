package com.scms.scms_be.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.scms.scms_be.dto.CategoryDto;
import com.scms.scms_be.entity.Category;
import com.scms.scms_be.service.CategoryService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequiredArgsConstructor
public class CategoryController {
    @Autowired
    private final CategoryService categoryService;
    
    @PostMapping("/admin/category")
    public ResponseEntity<Category> createCategory(@RequestBody CategoryDto categoryDto){
        return ResponseEntity.ok(categoryService.createCategory(categoryDto));
    }

    @GetMapping("/auth/get-all-category")
    public ResponseEntity<List<Category>> getAllCategory() {
        return ResponseEntity.ok(categoryService.getAllCategory());
    }
    @GetMapping("/auth/get-category/{categoryId}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Integer categoryId) {
        return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
    }
    
    @PutMapping("/admin/update-category/{categoryId}")
    public ResponseEntity<Category> updateCategory(@PathVariable Integer categoryId,
                                                 @RequestBody CategoryDto  categoryDto) {
        return ResponseEntity.ok(categoryService.updateCategory(categoryId, categoryDto));
    }

    @DeleteMapping ("/admin/delete-category/{categoryId}")
    public ResponseEntity<CategoryDto> deleteCategory(@PathVariable Integer categoryId) {
        return ResponseEntity.ok(categoryService.deleteCategory(categoryId));
    }

}
