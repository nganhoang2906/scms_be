package com.scms.scms_be.repository.Sales;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scms.scms_be.model.entity.Sales.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

}
