package com.scms.scms_be.repository.Purchasing;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scms.scms_be.model.entity.Purchasing.PurchaseOrderDetail;

@Repository
public interface PurchaseOrderDetailRepository extends JpaRepository<PurchaseOrderDetail, Long> {

  List<PurchaseOrderDetail> findByPo_PoId(Long poId);

}
