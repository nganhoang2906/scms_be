package com.scms.scms_be.model.entity.Inventory;

import java.time.LocalDateTime;
import java.util.List;

import com.scms.scms_be.model.entity.General.Company;
import com.scms.scms_be.model.entity.General.Warehouse;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transfer_ticket")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferTicket {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long ticketId;

  @ManyToOne
  @JoinColumn(name = "company_id", nullable = false)
  private Company company;

  private String ticketCode;

  @ManyToOne
  @JoinColumn(name = "from_warehouse_id", nullable = false)
  private Warehouse fromWarehouse;

  @ManyToOne
  @JoinColumn(name = "to_warehouse_id", nullable = false)
  private Warehouse toWarehouse;

  private String reason;
  private String createdBy;

  private LocalDateTime createdOn;
  private LocalDateTime lastUpdatedOn;

  private String status;
  private String file;

  @OneToMany(mappedBy = "ticket", orphanRemoval = true, cascade = CascadeType.ALL)
  private List<TransferTicketDetail> transferTicketDetails;
}
