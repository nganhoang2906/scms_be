package com.scms.scms_be.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scms.scms_be.model.dto.CategoryDto;
import com.scms.scms_be.model.entity.Category;
import com.scms.scms_be.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepo;


    public Category createCategory(CategoryDto categoryDto) {

        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());

        return categoryRepo.save(category);
    }

    public Category updateCategory(Integer categoryId,  CategoryDto categoryDto) {
        Category existingCategory = categoryRepo.findById(categoryId)
        .orElseThrow(() -> new IllegalArgumentException("Category with ID " + 
                                                    categoryId + " not found"));

        existingCategory.setName(categoryDto.getName());
        existingCategory.setDescription(categoryDto.getDescription());

        return categoryRepo.save(existingCategory);
    }


    public CategoryDto deleteCategory(Integer categoryId) {
        CategoryDto response = new CategoryDto();
         try {
            Optional<Category> categoryOptional = categoryRepo.findById(categoryId);
            if (categoryOptional.isPresent()) {
                categoryRepo.deleteById(categoryId);
                response.setStatusCode(200);
                response.setMessage("Successful");
            } else {
                response.setStatusCode(404);
                response.setMessage("Not found user for delete");
            }
            return response;
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError("Error to delete user " + e.getMessage());
            return response;
        }
    }

    public List<Category> getAllCategory(){
        return categoryRepo.findAll();
    }

    public Category getCategoryById(Integer id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));
    }
}
