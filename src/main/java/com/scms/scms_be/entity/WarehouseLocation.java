package com.scms.scms_be.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "warehouse_location")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationId;

    private Long warehouseId;
    private String locationCode;
    private String locationName;
    private String description;
    private String locationType;
    private double maxCapacity;
    private double usedSpace;
    private String status;
}