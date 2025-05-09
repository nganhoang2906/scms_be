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
@Table(name = "manufacture_plant")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManufacturePlant {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long plantId;

  @ManyToOne
  @JoinColumn(name = "company_id", nullable = false)
  private Company company;

  @Column(unique = true, nullable = false)
  private String plantCode;

  @Column(nullable = false)
  private String plantName;

  private String description;
}
