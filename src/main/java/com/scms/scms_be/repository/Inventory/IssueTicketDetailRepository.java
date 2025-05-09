package com.scms.scms_be.repository.Inventory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scms.scms_be.model.entity.Inventory.IssueTicketDetail;

public interface IssueTicketDetailRepository extends JpaRepository<IssueTicketDetail, Long> {
  
  List<IssueTicketDetail> findByTicketTicketId(Long ticketId);
}
