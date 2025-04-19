package com.scms.scms_be.service.Inventory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.dto.Inventory.TransferTicketDetailDto;
import com.scms.scms_be.model.dto.Inventory.TransferTicketDto;
import com.scms.scms_be.model.entity.General.Company;
import com.scms.scms_be.model.entity.General.Item;
import com.scms.scms_be.model.entity.General.Warehouse;
import com.scms.scms_be.model.entity.Inventory.Inventory;
import com.scms.scms_be.model.entity.Inventory.TransferTicket;
import com.scms.scms_be.model.entity.Inventory.TransferTicketDetail;
import com.scms.scms_be.model.request.Inventory.TransferTicketDetailRequest;
import com.scms.scms_be.model.request.Inventory.TransferTicketRequest;
import com.scms.scms_be.repository.General.CompanyRepository;
import com.scms.scms_be.repository.General.ItemRepository;
import com.scms.scms_be.repository.General.WarehouseRepository;
import com.scms.scms_be.repository.Inventory.InventoryRepository;
import com.scms.scms_be.repository.Inventory.TransferTicketDetailRepository;
import com.scms.scms_be.repository.Inventory.TransferTicketRepository;

@Service
public class TransferTicketService {
    @Autowired 
    private TransferTicketRepository ticketRepo;
    
    @Autowired 
    private TransferTicketDetailRepository detailRepo;
    
    @Autowired 
    private CompanyRepository companyRepo;
    
    @Autowired 
    private WarehouseRepository warehouseRepo;
    
    @Autowired 
    private ItemRepository itemRepo;

    @Autowired
    private InventoryRepository inventoryRepo;

    @Autowired
    private InventoryService inventoryService;

    public TransferTicketDto createTicket(TransferTicketRequest request) {
        TransferTicket ticket = new TransferTicket();
        
        Company company = companyRepo.findById(request.getCompany_id())
            .orElseThrow(() -> new CustomException("Company không tồn tại", HttpStatus.NOT_FOUND));
        
        Warehouse fromWarehouse = warehouseRepo.findById(request.getFrom_warehouse_id())
            .orElseThrow(() -> new CustomException("Kho gửi không tồn tại", HttpStatus.NOT_FOUND));
        
        Warehouse toWarehouse = warehouseRepo.findById(request.getTo_warehouse_id())
            .orElseThrow(() -> new CustomException("Kho nhận không tồn tại", HttpStatus.NOT_FOUND));

        ticket.setCompany(company);
        ticket.setTicketCode(generateTransferTicketCode(request.getCompany_id()));
        ticket.setFromWarehouse(fromWarehouse);
        ticket.setToWarehouse(toWarehouse);
        ticket.setReason(request.getReason());
        ticket.setCreatedBy(request.getCreatedBy());
        ticket.setCreatedOn(LocalDateTime.now());
        ticket.setLastUpdatedOn(LocalDateTime.now());
        ticket.setStatus(request.getStatus());
        ticket.setFile(request.getFile());

        TransferTicket savedTicket = ticketRepo.save(ticket);

        if (request.getTransferTicketDetails() != null) {
            for (TransferTicketDetailRequest detailRequest : request.getTransferTicketDetails()) {
                
                Item item = itemRepo.findById(detailRequest.getItem_id())
                .orElseThrow(() -> new CustomException("Item không tồn tại", HttpStatus.NOT_FOUND));
                
                Inventory inventory = inventoryRepo.findByItem_ItemIdAndWarehouse_WarehouseId(
                    item.getItemId(),fromWarehouse.getWarehouseId());
                
                if ( (inventory.getQuantity()- inventory.getOnDemandQuantity()) < detailRequest.getQuantity()) {
                    throw new CustomException("Số lượng item "+ item.getItemName() +" trong kho không đủ", HttpStatus.BAD_REQUEST);
                }

                TransferTicketDetail detail = new TransferTicketDetail();
                detail.setTicket(savedTicket);
                detail.setItem(item);
                detail.setQuantity(detailRequest.getQuantity());
                detail.setNote(detailRequest.getNote());
                detailRepo.save(detail);
                
                inventoryService.increaseOnDemand(
                    inventory.getInventoryId(), detailRequest.getQuantity());
                    
            }
        }

        return convertToDto(savedTicket);
    }

    public TransferTicketDto getTicketById(Long id) {
        TransferTicket ticket = ticketRepo.findById(id).orElseThrow();
        return convertToDto(ticket);
    }

    public List<TransferTicketDto> getAllByCompany(Long companyId) {
        return ticketRepo.findByCompanyCompanyId(companyId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<TransferTicketDto> getAllByFromWarehouse(Long warehouseId) {
        return ticketRepo.findByFromWarehouseWarehouseId(warehouseId)
            .stream()
            .map(this::convertToDto)
            .toList();
    }

    public TransferTicketDto updateTicket_Status(Long id, String status) {
        TransferTicket ticket = ticketRepo.findById(id).orElseThrow();
        if (ticket.getStatus().equals("Đã hoàn thành")) {
            throw new CustomException("Ticket đã hoàn thành không thể thay đổi trạng thái", HttpStatus.BAD_REQUEST);
        }
        if (ticket.getStatus().equals("Đã hủy")){
            throw new CustomException("Ticket đã hủy không thể thay đổi trạng thái", HttpStatus.BAD_REQUEST);
        }

        ticket.setStatus(status);
        ticket.setLastUpdatedOn(LocalDateTime.now());
        return convertToDto(ticketRepo.save(ticket));
    }

    public String generateTransferTicketCode(Long companyId) {
        String prefix = "TT" + companyId ;
        int count = ticketRepo.countByTicketCodeStartingWith(prefix);
        return prefix + String.format("%04d", count + 1);
    }

    private TransferTicketDto convertToDto(TransferTicket ticket) {
        TransferTicketDto dto = new TransferTicketDto();
        dto.setTicketId(ticket.getTicketId());
        dto.setCompanyId(ticket.getCompany().getCompanyId());

        dto.setTicketCode(ticket.getTicketCode());

        dto.setFrom_warehouseId(ticket.getFromWarehouse().getWarehouseId());
        dto.setFrom_warehouseName(ticket.getFromWarehouse().getWarehouseName());
        dto.setFrom_warehouseCode(ticket.getFromWarehouse().getWarehouseCode());

        dto.setTo_warehouseId(ticket.getToWarehouse().getWarehouseId());
        dto.setTo_warehouseName(ticket.getToWarehouse().getWarehouseName());
        dto.setTo_warehouseCode(ticket.getToWarehouse().getWarehouseCode());

        dto.setReason(ticket.getReason());
        dto.setCreatedBy(ticket.getCreatedBy());
        dto.setCreatedOn(ticket.getCreatedOn());
        dto.setLastUpdatedOn(ticket.getLastUpdatedOn());
        dto.setStatus(ticket.getStatus());
        dto.setFile(ticket.getFile());

        List<TransferTicketDetailDto> details = detailRepo
                .findByTicketTicketId(ticket.getTicketId())
                .stream()
                .map(this::convertToDetailDto)
                .collect(Collectors.toList());
        dto.setTransferTicketDetails(details);
        return dto;
    }

    private TransferTicketDetailDto convertToDetailDto(TransferTicketDetail detail) {
        TransferTicketDetailDto dto = new TransferTicketDetailDto();
        dto.setTTdetailId(detail.getTTdetailId());
        dto.setTicketId(detail.getTicket().getTicketId());
        
        dto.setItemId(detail.getItem().getItemId());
        dto.setItemCode(detail.getItem().getItemCode());
        dto.setItemName(detail.getItem().getItemName());

        dto.setQuantity(detail.getQuantity());
        dto.setNote(detail.getNote());
        return dto;
    }
}
