package com.scms.scms_be.repository.Inventory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scms.scms_be.model.entity.Inventory.ReceiveTicket;

public interface ReceiveTicketRepository extends JpaRepository<ReceiveTicket, Long> {

    List<ReceiveTicket> findByCompany_CompanyId(Long companyId);
    
}
