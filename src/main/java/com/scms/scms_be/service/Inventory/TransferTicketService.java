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

    public TransferTicketDto createTicket(TransferTicketDto dto) {
        TransferTicket ticket = new TransferTicket();
        
        Company company = companyRepo.findById(dto.getCompany_id())
            .orElseThrow(() -> new CustomException("Company không tồn tại", HttpStatus.NOT_FOUND));
        
        Warehouse fromWarehouse = warehouseRepo.findById(dto.getFrom_warehouse_id())
            .orElseThrow(() -> new CustomException("Kho gửi không tồn tại", HttpStatus.NOT_FOUND));
        
        Warehouse toWarehouse = warehouseRepo.findById(dto.getTo_warehouse_id())
            .orElseThrow(() -> new CustomException("Kho nhận không tồn tại", HttpStatus.NOT_FOUND));

        ticket.setCompany(company);
        ticket.setTicketCode(generateTransferTicketCode(dto.getCompany_id()));
        ticket.setFromWarehouse(fromWarehouse);
        ticket.setToWarehouse(toWarehouse);
        ticket.setReason(dto.getReason());
        ticket.setCreatedBy(dto.getCreatedBy());
        ticket.setCreatedOn(LocalDateTime.now());
        ticket.setLastUpdatedOn(LocalDateTime.now());
        ticket.setStatus(dto.getStatus());
        ticket.setFile(dto.getFile());

        TransferTicket savedTicket = ticketRepo.save(ticket);

        if (dto.getTransferTicketDetails() != null) {
            for (TransferTicketDetailDto detailDto : dto.getTransferTicketDetails()) {
                
                Item item = itemRepo.findById(detailDto.getItem_id())
                .orElseThrow(() -> new CustomException("Item không tồn tại", HttpStatus.NOT_FOUND));
                
                Inventory inventory = inventoryRepo.findByItem_ItemIdAndWarehouse_WarehouseId(
                    item.getItemId(),fromWarehouse.getWarehouseId());
                
                if ( (inventory.getQuantity()- inventory.getOnDemandQuantity()) < detailDto.getQuantity()) {
                    throw new CustomException("Số lượng item "+ item.getItemName() +" trong kho không đủ", HttpStatus.BAD_REQUEST);
                }

                TransferTicketDetail detail = new TransferTicketDetail();
                detail.setTicket(savedTicket);
                detail.setItem(item);
                detail.setQuantity(detailDto.getQuantity());
                detail.setNote(detailDto.getNote());
                detailRepo.save(detail);
                
                inventoryService.increaseOnDemand(
                    inventory.getInventoryId(), detailDto.getQuantity());
                    
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

    public TransferTicketDto updateTicket_Status(Long id, TransferTicketDto dto) {
        TransferTicket ticket = ticketRepo.findById(id).orElseThrow();
        
        ticket.setStatus(dto.getStatus());

        return convertToDto(ticketRepo.save(ticket));
    }

    public String generateTransferTicketCode(Long companyId) {
        String prefix = "TT-" + companyId + "-";
        int count = ticketRepo.countByTicketCodeStartingWith(prefix);
        return prefix + String.format("%04d", count + 1);
    }

    private TransferTicketDto convertToDto(TransferTicket ticket) {
        TransferTicketDto dto = new TransferTicketDto();
        dto.setTicketId(ticket.getTicketId());
        dto.setCompany_id(ticket.getCompany().getCompanyId());
        dto.setTicketCode(ticket.getTicketCode());
        dto.setFrom_warehouse_id(ticket.getFromWarehouse().getWarehouseId());
        dto.setTo_warehouse_id(ticket.getToWarehouse().getWarehouseId());
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
        dto.setTicket_id(detail.getTicket().getTicketId());
        dto.setItem_id(detail.getItem().getItemId());
        dto.setQuantity(detail.getQuantity());
        dto.setNote(detail.getNote());
        return dto;
    }
}
