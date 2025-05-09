package com.scms.scms_be.service.Delivery;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scms.scms_be.model.dto.Delivery.DeliveryOrderDto;
import com.scms.scms_be.model.entity.Delivery.DeliveryOrder;
import com.scms.scms_be.model.entity.Sales.SalesOrder;
import com.scms.scms_be.model.request.Delivery.DeliveryOrderRequest;
import com.scms.scms_be.repository.Delivery.DeliveryOrderRepository;
import com.scms.scms_be.repository.Sales.SalesOrderRepository;

@Service
public class DeliveryOrderService {
  @Autowired
  private DeliveryOrderRepository deliveryOrderRepository;

  @Autowired
  private SalesOrderRepository salesOrderRepository;

  public DeliveryOrderDto createDeliveryOrder(DeliveryOrderRequest deliveryOrderRequest) {
    SalesOrder salesOrder = salesOrderRepository.findById(deliveryOrderRequest.getSoId())
        .orElseThrow(() -> new RuntimeException("Sales Order not found"));

    DeliveryOrder deliveryOrder = new DeliveryOrder();
    deliveryOrder.setSalesOrder(salesOrder);
    deliveryOrder.setDoCode(generateDoCode(salesOrder.getSoId()));
    deliveryOrder.setCreateBy(salesOrder.getCreatedBy());
    deliveryOrder.setCreatedOn(LocalDateTime.now());
    deliveryOrder.setLastUpdatedOn(LocalDateTime.now());
    deliveryOrder.setStatus(deliveryOrderRequest.getStatus());

    DeliveryOrder savedDeliveryOrder = deliveryOrderRepository.save(deliveryOrder);
    return convertToDto(savedDeliveryOrder);
  }

  private String generateDoCode(Long soId) {
    String prefix = "DO" + String.valueOf(soId).substring(0, Math.min(5, String.valueOf(soId).length()));
    String year = String.valueOf(LocalDateTime.now().getYear()).substring(2);
    return prefix + year + String.format("%04d", new Random().nextInt(10000));
  }

  public DeliveryOrderDto convertToDto(DeliveryOrder deliveryOrder) {
    DeliveryOrderDto deliveryOrderDto = new DeliveryOrderDto();
    deliveryOrderDto.setDoId(deliveryOrder.getDoId());
    deliveryOrderDto.setDoCode(deliveryOrder.getDoCode());
    deliveryOrderDto.setSoId(deliveryOrder.getSalesOrder().getSoId());
    deliveryOrderDto.setSoCode(deliveryOrder.getSalesOrder().getSoCode());
    deliveryOrderDto.setCreateBy(deliveryOrder.getCreateBy());
    deliveryOrderDto.setCreatedOn(deliveryOrder.getCreatedOn());
    deliveryOrderDto.setLastUpdatedOn(deliveryOrder.getLastUpdatedOn());
    deliveryOrderDto.setStatus(deliveryOrder.getStatus());
    return deliveryOrderDto;
  }
  
}
