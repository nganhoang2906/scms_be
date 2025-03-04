package com.scms.scms_be.dto;

import org.springframework.web.multipart.MultipartFile;

import com.scms.scms_be.entity.Product;

import lombok.Data;

@Data
public class ProductDto {
  
    private String name;
    private double price;
    private String description;
    private byte[] byteImg;
    private Long categoryId;
    private String categoryName;
    private MultipartFile img;
    
    private int statusCode;
    private String error;
    private String message;

    private Product product;

    public void setMessage(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setMessage'");
    }
}
