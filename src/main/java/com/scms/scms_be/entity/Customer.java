package com.scms.scms_be.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "customer")
@NoArgsConstructor
@AllArgsConstructor

public class Customer {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long customerId;
    
    private String customerName;
    private String address;
    private String phoneNumber;
    private String email;
    private String customerType;
    private String contactPerson;
}
