package com.scms.scms_be.model.entity.General;

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
@Table(name = "warehouse")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Warehouse {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long warehouseId;

  @ManyToOne
  @JoinColumn(name = "company_id", nullable = false)
  private Company company;

  @Column(unique = true, nullable = false)
  private String warehouseCode;

  @Column(nullable = false)
  private String warehouseName;

  private String description;

  @Column(nullable = false)
  private Double maxCapacity;

  @Column(nullable = false)
  private String warehouseType;

  @Column(nullable = false)
  private String status;
}
