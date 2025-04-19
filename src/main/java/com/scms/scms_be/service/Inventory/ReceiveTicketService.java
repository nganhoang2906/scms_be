package com.scms.scms_be.service.Inventory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.dto.Inventory.ReceiveTickeDto;
import com.scms.scms_be.model.dto.Inventory.ReceiveTicketDetailDto;
import com.scms.scms_be.model.entity.General.Company;
import com.scms.scms_be.model.entity.General.Item;
import com.scms.scms_be.model.entity.General.Warehouse;
import com.scms.scms_be.model.entity.Inventory.IssueTicketDetail;
import com.scms.scms_be.model.entity.Inventory.ReceiveTicket;
import com.scms.scms_be.model.entity.Inventory.ReceiveTicketDetail;
import com.scms.scms_be.model.entity.Inventory.TransferTicket;
import com.scms.scms_be.model.entity.Inventory.TransferTicketDetail;
import com.scms.scms_be.model.entity.Manufacturing.ManufactureOrder;
import com.scms.scms_be.model.entity.Purchasing.PurchaseOrder;
import com.scms.scms_be.model.entity.Purchasing.PurchaseOrderDetail;
import com.scms.scms_be.model.entity.Sales.SalesOrder;
import com.scms.scms_be.model.entity.Sales.SalesOrderDetail;
import com.scms.scms_be.model.request.Inventory.ReceiveTicketDetailRequest;
import com.scms.scms_be.model.request.Inventory.ReceiveTicketRequest;
import com.scms.scms_be.repository.General.CompanyRepository;
import com.scms.scms_be.repository.General.ItemRepository;
import com.scms.scms_be.repository.General.WarehouseRepository;
import com.scms.scms_be.repository.Inventory.ReceiveTicketDetailRepository;
import com.scms.scms_be.repository.Inventory.ReceiveTicketRepository;
import com.scms.scms_be.repository.Inventory.TransferTicketRepository;
import com.scms.scms_be.repository.Manufacturing.ManufactureOrderRepository;
import com.scms.scms_be.repository.Purchasing.PurchaseOrderRepository;

@Service
public class ReceiveTicketService {

    @Autowired
    private ReceiveTicketRepository ticketRepo;

    @Autowired
    private ReceiveTicketDetailRepository detailRepo;

    @Autowired
    private CompanyRepository companyRepo;

    @Autowired
    private WarehouseRepository warehouseRepo;

    @Autowired
    private ItemRepository itemRepo;

    @Autowired
    private TransferTicketRepository transferTicketRepository;

    @Autowired
    private ManufactureOrderRepository manufactureOrderRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    public ReceiveTickeDto create(ReceiveTicketRequest request) {
        Company company = companyRepo.findById(request.getCompanyId())
                .orElseThrow(() -> new CustomException("Company không tồn tại!", HttpStatus.NOT_FOUND));
        Warehouse warehouse = warehouseRepo.findById(request.getWarehouseId())
                .orElseThrow(() -> new CustomException("Warehouse không tồn tại!", HttpStatus.NOT_FOUND));

        ReceiveTicket ticket = new ReceiveTicket();
        ticket.setCompany(company);
        ticket.setWarehouse(warehouse);
        ticket.setTicketCode(request.getTicketCode());
        ticket.setReceiveDate(request.getReceiveDate());
        ticket.setReason(request.getReason());
        ticket.setReceiveType(request.getReceiveType());

        List<ReceiveTicketDetail> details = new ArrayList<>();

        if(request.getReceiveType().equals("Manufacture Order")){
            ManufactureOrder manufactureOrder = manufactureOrderRepository.findByMoCode(request.getReferenceCode());
            ticket.setReferenceId(manufactureOrder.getMoId());
            ReceiveTicketDetail detail = new ReceiveTicketDetail();
            detail.setTicket(ticket);
            Item item = itemRepo.findById(manufactureOrder.getItem().getItemId())
                    .orElseThrow(() -> new CustomException("Item không tồn tại", HttpStatus.NOT_FOUND));
            detail.setItem(item);
            detail.setQuantity(manufactureOrder.getQuantity());
            detail.setNote(request.getNote());
            details.add(detail);
        }
        else if(request.getReceiveType().equals("Purchase Order")){
            PurchaseOrder purchaseOrder = purchaseOrderRepository.findByPoCode(request.getReferenceCode());
            ticket.setReferenceId(purchaseOrder.getPoId());
            List<PurchaseOrderDetail> purchaseOrderDetails = purchaseOrder.getPurchaseOrderDetails();
            for (PurchaseOrderDetail purchaseOrderDetail : purchaseOrderDetails) {
                ReceiveTicketDetail detail = new ReceiveTicketDetail();
                detail.setTicket(ticket);
                Item item = itemRepo.findById(purchaseOrderDetail.getItem().getItemId())
                        .orElseThrow(() -> new CustomException("Item không tồn tại", HttpStatus.NOT_FOUND));
                detail.setItem(item);
                detail.setQuantity(purchaseOrderDetail.getQuantity());
                detail.setNote(request.getNote());
                details.add(detail);
            }
        }
         else  if(request.getReceiveType().equals("Transfer Ticket")){
            TransferTicket transferTicket = transferTicketRepository.findByTicketCode(request.getReferenceCode());
            ticket.setReferenceId(transferTicket.getTicketId());
            List<TransferTicketDetail> transferTicketDetails = transferTicket.getTransferTicketDetails();
            for (TransferTicketDetail transferTicketDetail : transferTicketDetails) {
                ReceiveTicketDetail detail = new ReceiveTicketDetail();
                detail.setTicket(ticket);
                Item item = itemRepo.findById(transferTicketDetail.getItem().getItemId())
                        .orElseThrow(() -> new CustomException("Item không tồn tại", HttpStatus.NOT_FOUND));
                detail.setItem(item);
                detail.setQuantity(transferTicketDetail.getQuantity());
                detail.setNote(request.getNote());
                details.add(detail);
            }
        } else {
            throw new CustomException("Loại phiếu không hợp lệ!", HttpStatus.BAD_REQUEST);
        }

        
        ticket.setCreatedBy(request.getCreatedBy());
        ticket.setCreatedOn(LocalDateTime.now());
        ticket.setLastUpdatedOn(LocalDateTime.now());
        ticket.setStatus(request.getStatus());
        ticket.setFile(request.getFile());

        ticket.setReceiveTicketDetails(details);

        ReceiveTicket saved = ticketRepo.save(ticket);


        return convertToDto(saved);
    }

    public List<ReceiveTickeDto> getAllInCompany(Long companyId) {
        return ticketRepo.findByCompany_CompanyId(companyId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ReceiveTickeDto getById(Long id) {
        ReceiveTicket ticket = ticketRepo.findById(id)
                .orElseThrow(() -> new CustomException("Phiếu nhập không tồn tại", HttpStatus.NOT_FOUND));
        return convertToDto(ticket);
    }

    public ReceiveTickeDto updateStatus(Long id, String status) {
        ReceiveTicket ticket = ticketRepo.findById(id)
                .orElseThrow(() -> new CustomException("Phiếu nhập không tồn tại", HttpStatus.NOT_FOUND));
        if (ticket.getStatus().equals("Đã hoàn thành")) {
            throw new CustomException("Ticket đã hoàn thành không thể thay đổi trạng thái", HttpStatus.BAD_REQUEST);
        }
        if (ticket.getStatus().equals("Đã hủy")){
            throw new CustomException("Ticket đã hủy không thể thay đổi trạng thái", HttpStatus.BAD_REQUEST);
        }
        
        ticket.setStatus(status);
        ticket.setLastUpdatedOn(LocalDateTime.now());

        ticketRepo.save(ticket);
        return convertToDto(ticket);
    }

    public String generateTicketCode(Long companyId, String referenceCode) {
        String prefix = "ReceiveTk-" + companyId  + referenceCode;
        return prefix;
    }

    private ReceiveTickeDto convertToDto(ReceiveTicket ticket) {
        ReceiveTickeDto dto = new ReceiveTickeDto();
        dto.setTicketId(ticket.getTicketId());
        dto.setCompanyId(ticket.getCompany().getCompanyId());

        dto.setTicketCode(ticket.getTicketCode());

        dto.setWarehouseId(ticket.getWarehouse().getWarehouseId());
        dto.setWarehouseCode(ticket.getWarehouse().getWarehouseCode());
        dto.setWarehouseName(ticket.getWarehouse().getWarehouseName());

        dto.setReceiveDate(ticket.getReceiveDate());
        dto.setReason(ticket.getReason());

        dto.setReceiveType(ticket.getReceiveType());
        dto.setReferenceId(ticket.getReferenceId());

        dto.setCreatedBy(ticket.getCreatedBy());
        dto.setCreatedOn(ticket.getCreatedOn());
        dto.setLastUpdatedOn(ticket.getLastUpdatedOn());
        dto.setStatus(ticket.getStatus());
        dto.setFile(ticket.getFile());

        List<ReceiveTicketDetailDto> details = detailRepo
                .findByTicketTicketId(ticket.getTicketId())
                .stream()
                .map(this::convertToDetailDto)
                .collect(Collectors.toList());
        dto.setReceiveTicketDetails(details);
        return dto;
    }

    private ReceiveTicketDetailDto convertToDetailDto(ReceiveTicketDetail detail) {
        ReceiveTicketDetailDto dto = new ReceiveTicketDetailDto();
        dto.setRTdetailId(detail.getRTdetailId());

        dto.setTicketId(detail.getTicket().getTicketId());

        dto.setItemId(detail.getItem().getItemId());
        dto.setItemCode(detail.getItem().getItemCode());
        dto.setItemName(detail.getItem().getItemName());
        
        dto.setQuantity(detail.getQuantity());
        dto.setNote(detail.getNote());
        return dto;
    }
}
