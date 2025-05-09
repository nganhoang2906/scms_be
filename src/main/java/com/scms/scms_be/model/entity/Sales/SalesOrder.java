package com.scms.scms_be.model.entity.Sales;

import java.time.LocalDateTime;
import java.util.List;

import com.scms.scms_be.model.entity.General.Company;
import com.scms.scms_be.model.entity.Purchasing.PurchaseOrder;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sales_order")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrder {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long soId;

  @ManyToOne
  @JoinColumn(name = "company_id")
  private Company company;

  @OneToOne
  @JoinColumn(name = "po_id")
  private PurchaseOrder purchaseOrder;

  @Column(unique = true, nullable = false)
  private String soCode;

  private Double taxRate;
  private Double totalPrice;
  private String description;
  private String createdBy;
  private LocalDateTime createdOn;
  private LocalDateTime lastUpdatedOn;
  private String status;

  @OneToMany(mappedBy = "salesOrder", orphanRemoval = true, cascade = CascadeType.ALL)
  private List<SalesOrderDetail> salesOrderDetails;
}