package com.scms.scms_be.repository.Inventory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scms.scms_be.model.entity.Inventory.TransferTicket;

@Repository
public interface TransferTicketRepository extends JpaRepository<TransferTicket,Long> {

    List<TransferTicket> findByCompanyCompanyId(Long companyId);

    List<TransferTicket> findByFromWarehouseWarehouseId(Long warehouseId);

    int countByTicketCodeStartingWith(String prefix);

    TransferTicket findByTicketCode(String ticketCode);

}
