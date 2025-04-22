package com.scms.scms_be.model.entity.Delivery;

import java.time.LocalDateTime;

import com.scms.scms_be.model.entity.Sales.SalesOrder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "delivery_order")
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doId;

    @Column(unique = true, nullable = false)
    private String doCode;

    @OneToOne
    @JoinColumn(name = "so_id",nullable = false)
    private SalesOrder soId;
    
    private String createBy;
    private LocalDateTime createdOn;
    private LocalDateTime lastUpdatedOn;
    private String status;
}
