package com.scms.scms_be.repository.Purchasing;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scms.scms_be.model.entity.Purchasing.PurchaseOrder;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder,Long> {

    PurchaseOrder findByPoCode(String poCode);
}
