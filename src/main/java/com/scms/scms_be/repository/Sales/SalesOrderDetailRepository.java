package com.scms.scms_be.repository.Sales;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scms.scms_be.model.entity.Sales.SalesOrderDetail;

public interface SalesOrderDetailRepository extends JpaRepository<SalesOrderDetail, Long> {

  List<SalesOrderDetail> findBySalesOrder_SoId(Long soId);

}
