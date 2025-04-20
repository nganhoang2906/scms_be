package com.scms.scms_be.service.Purchasing;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.dto.Purchasing.PurchaseOrderDetailDto;
import com.scms.scms_be.model.dto.Purchasing.PurchaseOrderDto;
import com.scms.scms_be.model.entity.General.Company;
import com.scms.scms_be.model.entity.General.Item;
import com.scms.scms_be.model.entity.Purchasing.PurchaseOrder;
import com.scms.scms_be.model.entity.Purchasing.PurchaseOrderDetail;
import com.scms.scms_be.model.request.Purchasing.PurchaseOrderDetailRequest;
import com.scms.scms_be.model.request.Purchasing.PurchaseOrderRequest;
import com.scms.scms_be.repository.General.CompanyRepository;
import com.scms.scms_be.repository.General.ItemRepository;
import com.scms.scms_be.repository.Purchasing.PurchaseOrderDetailRepository;
import com.scms.scms_be.repository.Purchasing.PurchaseOrderRepository;

@Service
public class PurchaseOrderService {
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private PurchaseOrderDetailRepository poDetailRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ItemRepository itemRepository;

    public PurchaseOrderDto createPurchaseOrder(PurchaseOrderRequest purchaseOrderRequest) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        
        Company company = companyRepository.findById(purchaseOrderRequest.getCompanyId())
                .orElseThrow(() -> new CustomException("Company not found", HttpStatus.NOT_FOUND));
        Company supplierCompany = companyRepository.findById(purchaseOrderRequest.getSuplierCompanyId())
                .orElseThrow(() -> new CustomException("Supplier company not found", HttpStatus.NOT_FOUND));
        purchaseOrder.setCompany(company);
        purchaseOrder.setSuplierCompany(supplierCompany);
        purchaseOrder.setPoCode(generatePurchasingOrderCode(company.getCompanyId(), supplierCompany.getCompanyId()));
        purchaseOrder.setDescription(purchaseOrderRequest.getDescription());
        purchaseOrder.setCreatedBy(purchaseOrderRequest.getCreatedBy());
        purchaseOrder.setStatus(purchaseOrderRequest.getStatus());
        purchaseOrder.setCreatedOn(LocalDateTime.now());
        purchaseOrder.setLastUpdatedOn(LocalDateTime.now());

        PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.save(purchaseOrder);

        for(PurchaseOrderDetailRequest  poDetailRequest : purchaseOrderRequest.getPurchaseOrderDetails()) {
            Item item = itemRepository.findById(poDetailRequest.getItemId())    
                    .orElseThrow(() -> new CustomException("Item not found", HttpStatus.NOT_FOUND));
            PurchaseOrderDetail newPoDeatil = new PurchaseOrderDetail();
            newPoDeatil.setPo(savedPurchaseOrder);
            newPoDeatil.setItem(item);
            newPoDeatil.setQuantity(poDetailRequest.getQuantity());
            newPoDeatil.setNote(poDetailRequest.getNote());
            poDetailRepository.save(newPoDeatil);
        }

        return convertToDto(savedPurchaseOrder);
    }

    public List<PurchaseOrderDto> getAllPoByCompany(Long companyId) {
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findByCompany_CompanyId(companyId);
        return purchaseOrders.stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<PurchaseOrderDto> getAllPoBySupplierCompany(Long supplierCompanyId) {
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findBySuplierCompany_CompanyId(supplierCompanyId);
        return purchaseOrders.stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<PurchaseOrderDto> getAllPoByCompanyAndSupplierCompany(Long companyId, Long supplierCompanyId) {
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findByCompany_CompanyIdAndSuplierCompany_CompanyId(companyId, supplierCompanyId);
        return purchaseOrders.stream()
                .map(this::convertToDto)
                .toList();
    }


    public PurchaseOrderDto getPurchaseOrderById(Long poId) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(poId)
                .orElseThrow(() -> new CustomException("Purchase order not found", HttpStatus.NOT_FOUND));
        return convertToDto(purchaseOrder);
    }

    public PurchaseOrderDto updatePoStatus(Long poId, String status) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(poId)
                .orElseThrow(() -> new CustomException("Purchase order not found", HttpStatus.NOT_FOUND));
        if (purchaseOrder.getStatus().equals("Đã hoàn thành")) {
            throw new CustomException("Purchase order đã hoàn thành không thể thay đổi trạng thái", HttpStatus.BAD_REQUEST);
        }
        if (purchaseOrder.getStatus().equals("Đã hủy")){
            throw new CustomException("Purchase order đã hủy không thể thay đổi trạng thái", HttpStatus.BAD_REQUEST);
        }
        
        purchaseOrder.setStatus(status);
        purchaseOrder.setLastUpdatedOn(LocalDateTime.now());

        return convertToDto(purchaseOrderRepository.save(purchaseOrder));
    }

    public String generatePurchasingOrderCode(Long companyId,Long supplierCompanyId) {
        String prefix = "PO" + companyId + supplierCompanyId; 
        String year = String.valueOf(LocalDateTime.now().getYear()).substring(2);
        int count = purchaseOrderRepository.countByPoCodeStartingWith(prefix);
        return prefix + year + String.format("%04d", count + 1);
    }

    public PurchaseOrderDto convertToDto(PurchaseOrder purchaseOrder) {
        PurchaseOrderDto purchaseOrderDto = new PurchaseOrderDto();
        purchaseOrderDto.setPoId(purchaseOrder.getPoId());
        purchaseOrderDto.setPoCode(purchaseOrder.getPoCode());
        purchaseOrderDto.setCompanyId(purchaseOrder.getCompany().getCompanyId());
        purchaseOrderDto.setCompanyName(purchaseOrder.getCompany().getCompanyName());
        purchaseOrderDto.setSuplierCompanyId(purchaseOrder.getSuplierCompany().getCompanyId());
        purchaseOrderDto.setSuplierCompanyName(purchaseOrder.getSuplierCompany().getCompanyName());
        purchaseOrderDto.setDescription(purchaseOrder.getDescription());
        purchaseOrderDto.setCreatedBy(purchaseOrder.getCreatedBy());
        purchaseOrderDto.setStatus(purchaseOrder.getStatus());

        List<PurchaseOrderDetailDto> purchaseOrderDetailDtos = poDetailRepository
                                                    .findByPo_PoId(purchaseOrder.getPoId())
                                                    .stream()
                                                    .map(this::convertToDetailDto)
                                                    .toList();
        purchaseOrderDto.setPurchaseOrderDetails(purchaseOrderDetailDtos);

        return purchaseOrderDto;
    }

    public PurchaseOrderDetailDto convertToDetailDto(PurchaseOrderDetail purchaseOrderDetail) {
        PurchaseOrderDetailDto purchaseOrderDetailDto = new PurchaseOrderDetailDto();
        purchaseOrderDetailDto.setPurchaseOrderDetailId(purchaseOrderDetail.getPurchaseOrderDetailId());
        purchaseOrderDetailDto.setPoId(purchaseOrderDetail.getPo().getPoId());
        purchaseOrderDetailDto.setPoCode(purchaseOrderDetail.getPo().getPoCode());
        purchaseOrderDetailDto.setItemId(purchaseOrderDetail.getItem().getItemId());
        purchaseOrderDetailDto.setItemName(purchaseOrderDetail.getItem().getItemName());
        purchaseOrderDetailDto.setItemCode(purchaseOrderDetail.getItem().getItemCode());
        purchaseOrderDetailDto.setQuantity(purchaseOrderDetail.getQuantity());
        purchaseOrderDetailDto.setNote(purchaseOrderDetail.getNote());
        return purchaseOrderDetailDto;
    }
}
