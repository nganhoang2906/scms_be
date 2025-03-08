package com.scms.scms_be.model.dto;

import lombok.Data;

@Data
public class CategoryDto {

    private String name;
    private String description;

    private int statusCode;
    private String error;
    private String message;
}
