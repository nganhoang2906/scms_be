package com.scms.scms_be.repository.Delivery;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scms.scms_be.model.entity.Delivery.DeliveryOrder;

public interface DeliveryOrderRepository extends JpaRepository<DeliveryOrder, Long> {

}
