package com.scms.scms_be.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductItem {
    private String productName;
    private int quantity;
    private double price;
    private double total;
}
