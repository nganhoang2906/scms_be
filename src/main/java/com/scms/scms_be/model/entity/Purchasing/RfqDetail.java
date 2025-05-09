package com.scms.scms_be.model.entity.Purchasing;

import com.scms.scms_be.model.entity.General.Item;

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
@Table(name = "rfq_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RfqDetail {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long RfqDetailId;

  @ManyToOne
  @JoinColumn(name = "rfq_id", nullable = false)
  private RequestForQuotation rfq;

  @ManyToOne
  @JoinColumn(name = "item_id", nullable = false)
  private Item item;

  private Double quantity;
  private String note;
}
