package com.scms.scms_be.repository.Inventory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scms.scms_be.model.entity.Inventory.ReceiveTicketDetail;

public interface ReceiveTicketDetailRepository extends JpaRepository<ReceiveTicketDetail, Long> {
  
  List<ReceiveTicketDetail> findByTicketTicketId(Long ticketId);
  
}
