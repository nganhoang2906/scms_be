package com.scms.scms_be.service.Sales;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.dto.Sales.SalesOrderDetailDto;
import com.scms.scms_be.model.dto.Sales.SalesOrderDto;
import com.scms.scms_be.model.entity.General.Company;
import com.scms.scms_be.model.entity.General.Item;
import com.scms.scms_be.model.entity.Purchasing.PurchaseOrder;
import com.scms.scms_be.model.entity.Sales.SalesOrder;
import com.scms.scms_be.model.entity.Sales.SalesOrderDetail;
import com.scms.scms_be.model.request.Sales.SalesOrderDetailRequest;
import com.scms.scms_be.model.request.Sales.SalesOrderRequest;
import com.scms.scms_be.repository.General.CompanyRepository;
import com.scms.scms_be.repository.General.ItemRepository;
import com.scms.scms_be.repository.Purchasing.PurchaseOrderRepository;
import com.scms.scms_be.repository.Sales.SalesOrderDetailRepository;
import com.scms.scms_be.repository.Sales.SalesOrderRepository;


@Service
public class SalesOrderService {
    @Autowired
    private  SalesOrderRepository salesOrderRepository;

    @Autowired
    private  SalesOrderDetailRepository salesOrderDetailRepository;
    
    @Autowired
    private  ItemRepository itemRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private CompanyRepository companyRepository;

    public SalesOrderDto createSalesOrder(SalesOrderRequest salesOrderRequest) {
         PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(salesOrderRequest.getPoId())
                .orElseThrow(() -> new CustomException("Đơn đặt hàng không tồn tại!", HttpStatus.NOT_FOUND));
        Company company = companyRepository.findById(salesOrderRequest.getCompanyId())
                .orElseThrow(() -> new CustomException("Công ty không tồn tại!", HttpStatus.NOT_FOUND));
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setCompany(company);
        salesOrder.setPurchaseOrder(purchaseOrder);
        salesOrder.setSoCode(generateSalesOrderCode(purchaseOrder.getPoId()));
        salesOrder.setTaxRate(salesOrderRequest.getTaxRate());
        salesOrder.setDescription(salesOrderRequest.getDescription());
        salesOrder.setCreatedBy(salesOrderRequest.getCreatedBy());
        salesOrder.setStatus(salesOrderRequest.getStatus());
        salesOrder.setCreatedOn(LocalDateTime.now());
        salesOrder.setLastUpdatedOn(LocalDateTime.now());
        
        SalesOrder savedSalesOrder = salesOrderRepository.save(salesOrder);

        if (salesOrderRequest.getSalesOrderDetails() == null || salesOrderRequest.getSalesOrderDetails().isEmpty()) {
            throw new CustomException("Danh sách hàng hóa không được để trống!", HttpStatus.BAD_REQUEST);
        }
        Double totalPrice = 0.0;

        for (SalesOrderDetailRequest salesOrderDetailRequest : salesOrderRequest.getSalesOrderDetails()) {
            Item item = itemRepository.findById(salesOrderDetailRequest.getItemId())
                    .orElseThrow(() -> new CustomException("Item không tồn tại!", HttpStatus.NOT_FOUND));
            SalesOrderDetail salesOrderDetail = new SalesOrderDetail();
            salesOrderDetail.setItem(item);
            salesOrderDetail.setQuantity(salesOrderDetailRequest.getQuantity());
            salesOrderDetail.setItemPrice(item.getExportPrice());
            salesOrderDetail.setNote(salesOrderDetailRequest.getNote());
            salesOrderDetail.setSalesOrder(savedSalesOrder);
            salesOrderDetailRepository.save(salesOrderDetail);
            totalPrice += salesOrderDetail.getItemPrice() * salesOrderDetail.getQuantity();
        }
        savedSalesOrder.setTotalPrice(totalPrice);
        salesOrderRepository.save(savedSalesOrder);

        return convertToDto(savedSalesOrder);
    }
    public SalesOrderDto getSalesOrderById(Long soId) {
        SalesOrder salesOrder = salesOrderRepository.findById(soId)
                .orElseThrow(() -> new CustomException("Đơn hàng không tồn tại!", HttpStatus.NOT_FOUND));
        return convertToDto(salesOrder);
    }
    public List<SalesOrderDto> getAllSalesOrdersByCompany(Long companyId) {
        List<SalesOrder> salesOrders = salesOrderRepository.findByCompany_CompanyId(companyId);
        return salesOrders.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<SalesOrderDto> getAllSalesOrderByPoId(Long poId) {
        List<SalesOrder> salesOrders = salesOrderRepository.findByPurchaseOrder_PoId(poId);
        return salesOrders.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public SalesOrderDto updateSoStatus(Long soId, String status) {
        SalesOrder salesOrder = salesOrderRepository.findById(soId)
                .orElseThrow(() -> new CustomException("Đơn hàng không tồn tại!", HttpStatus.NOT_FOUND));
        if (salesOrder.getStatus().equals("Đã hoàn thành")) {
            throw new CustomException("Purchase order đã hoàn thành không thể thay đổi trạng thái", HttpStatus.BAD_REQUEST);
        }
        if (salesOrder.getStatus().equals("Đã hủy")){
            throw new CustomException("Purchase order đã hủy không thể thay đổi trạng thái", HttpStatus.BAD_REQUEST);
        }

        salesOrder.setStatus(status);
        salesOrder.setLastUpdatedOn(LocalDateTime.now());

        return convertToDto(salesOrderRepository.save(salesOrder));
    }

    public String generateSalesOrderCode(Long companyId) {
        String prefix = "SO" + String.valueOf(companyId).substring(1) ; 
        String year = String.valueOf(LocalDateTime.now().getYear()).substring(2);
        int count = salesOrderRepository.countBySoCodeStartingWith(prefix);
        return prefix + year + String.format("%04d", count + 1);
    }


    public SalesOrderDto convertToDto (SalesOrder salesOrder) {
        SalesOrderDto dto = new SalesOrderDto();
        dto.setSoId(salesOrder.getSoId());
        dto.setSoCode(salesOrder.getSoCode());
        dto.setCompanyId(salesOrder.getCompany().getCompanyId());
        dto.setCompanyName(salesOrder.getCompany().getCompanyName());
        dto.setPoId(salesOrder.getPurchaseOrder().getPoId());
        dto.setPoCode(salesOrder.getPurchaseOrder().getPoCode());
        dto.setCreatedBy(salesOrder.getCreatedBy());
        dto.setCreatedOn(LocalDateTime.now());
        dto.setLastUpdatedOn(salesOrder.getLastUpdatedOn());
        dto.setTotalPrice(salesOrder.getTotalPrice());
        dto.setTaxRate(salesOrder.getTaxRate());
        dto.setDescription(salesOrder.getDescription());
        dto.setStatus(salesOrder.getStatus()); 

        List<SalesOrderDetailDto> salesOrderDetails = salesOrderDetailRepository
                        .findBySalesOrder_SoId(salesOrder.getSoId())
                        .stream()
                        .map(this::convertToDetailDto)
                        .collect(Collectors.toList());

        dto.setSalesOrderDetails(salesOrderDetails);
        return dto;
    }

    public SalesOrderDetailDto convertToDetailDto(SalesOrderDetail salesOrderDetail) {
        SalesOrderDetailDto dto = new SalesOrderDetailDto();
        dto.setSoDetailId(salesOrderDetail.getSoDetailId());
        dto.setSoId(salesOrderDetail.getSalesOrder().getSoId());
        dto.setItemId(salesOrderDetail.getItem().getItemId());
        dto.setItemName(salesOrderDetail.getItem().getItemName());
        dto.setItemCode(salesOrderDetail.getItem().getItemCode());
        dto.setQuantity(salesOrderDetail.getQuantity());
        dto.setItemPrice(salesOrderDetail.getItemPrice());
        dto.setNote(salesOrderDetail.getNote());

        return dto;
    }
}
