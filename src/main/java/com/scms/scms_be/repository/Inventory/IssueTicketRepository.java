package com.scms.scms_be.repository.Inventory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scms.scms_be.model.entity.Inventory.IssueTicket;

public interface IssueTicketRepository extends JpaRepository<IssueTicket, Long> {
  
  List<IssueTicket> findByCompany_CompanyId(Long companyId);

  int countByTicketCodeStartingWith(String prefix);

}
