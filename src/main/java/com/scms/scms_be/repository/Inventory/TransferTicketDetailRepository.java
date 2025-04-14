package com.scms.scms_be.repository.Inventory;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scms.scms_be.model.entity.Inventory.TransferTicketDetail;
@Repository
public interface TransferTicketDetailRepository extends JpaRepository<TransferTicketDetail, Long> {

    List<TransferTicketDetail> findByTicketTicketId(Long ticketId);
   

}
