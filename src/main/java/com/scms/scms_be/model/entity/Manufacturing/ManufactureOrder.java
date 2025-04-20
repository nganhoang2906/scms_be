package com.scms.scms_be.model.entity.Manufacturing;

import java.time.LocalDateTime;

import com.scms.scms_be.model.entity.General.Item;
import com.scms.scms_be.model.entity.General.ManufactureLine;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "manufacture_order")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManufactureOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long moId;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "line_id", nullable = false)
    private ManufactureLine line;

    @Column(unique = true, nullable = false)
    private String moCode;

    private String type;
    private Double quantity;

    private LocalDateTime estimatedStartTime;
    private LocalDateTime estimatedEndTime;

    private String createdBy;
    private LocalDateTime createdOn;
    private LocalDateTime lastUpdatedOn;
    private String status;
}
