package com.scms.scms_be.repository.Purchasing;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scms.scms_be.model.entity.Purchasing.PurchaseOrderDetail;

public interface PurchaseOrderDetailRepository extends JpaRepository<PurchaseOrderDetail, Long> {

}
