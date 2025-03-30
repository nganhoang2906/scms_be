package com.scms.scms_be.repository.Sales;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scms.scms_be.model.entity.Sales.SalesOrder;

public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {

}
