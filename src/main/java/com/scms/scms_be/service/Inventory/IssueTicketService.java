package com.scms.scms_be.service.Inventory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.dto.Inventory.IssueTicketDetailDto;
import com.scms.scms_be.model.dto.Inventory.IssueTicketDto;
import com.scms.scms_be.model.entity.General.Company;
import com.scms.scms_be.model.entity.General.Item;
import com.scms.scms_be.model.entity.General.Warehouse;
import com.scms.scms_be.model.entity.Inventory.IssueTicket;
import com.scms.scms_be.model.entity.Inventory.IssueTicketDetail;
import com.scms.scms_be.model.entity.Inventory.TransferTicket;
import com.scms.scms_be.model.entity.Inventory.TransferTicketDetail;
import com.scms.scms_be.model.entity.Manufacturing.ManufactureOrder;
import com.scms.scms_be.model.entity.Sales.SalesOrder;
import com.scms.scms_be.model.entity.Sales.SalesOrderDetail;
import com.scms.scms_be.model.request.Inventory.IssueTicketRequest;
import com.scms.scms_be.repository.General.CompanyRepository;
import com.scms.scms_be.repository.General.ItemRepository;
import com.scms.scms_be.repository.General.WarehouseRepository;
import com.scms.scms_be.repository.Inventory.IssueTicketDetailRepository;
import com.scms.scms_be.repository.Inventory.IssueTicketRepository;
import com.scms.scms_be.repository.Inventory.TransferTicketRepository;
import com.scms.scms_be.repository.Manufacturing.ManufactureOrderRepository;
import com.scms.scms_be.repository.Sales.SalesOrderRepository;

@Service
public class IssueTicketService {
    @Autowired
    private IssueTicketRepository ticketRepo;
    @Autowired
    private IssueTicketDetailRepository detailRepo;
    @Autowired
    private CompanyRepository companyRepo;
    @Autowired
    private WarehouseRepository warehouseRepo;
    @Autowired
    private ManufactureOrderRepository manufactureOrderRepository;
    @Autowired
    private TransferTicketRepository transferTicketRepository;
    @Autowired
    private SalesOrderRepository    salesOrderRepository;
    @Autowired
    private ItemRepository itemRepo;


    public IssueTicketDto createIssueTicket(IssueTicketRequest request) {
        Company company = companyRepo.findById(request.getCompanyId())
                .orElseThrow(() -> new CustomException("Company not found", HttpStatus.NOT_FOUND));
        Warehouse warehouse = warehouseRepo.findById(request.getWarehouseId())
                .orElseThrow(() -> new CustomException("Warehouse not found", HttpStatus.NOT_FOUND));
        
        IssueTicket ticket = new IssueTicket();
        ticket.setCompany(company);
        ticket.setWarehouse(warehouse);
        ticket.setTicketCode(generateTicketCode(company.getCompanyId(),request.getReferenceCode()));
        ticket.setIssueDate(request.getIssueDate());
        ticket.setReason(request.getReason());
        ticket.setIssueType(request.getIssueType());

        List<IssueTicketDetail> details = new ArrayList<>();

        if(request.getIssueType().equals("Manufacture Order")){
            ManufactureOrder manufactureOrder = manufactureOrderRepository.findByMoCode(request.getReferenceCode());
            ticket.setReferenceId(manufactureOrder.getMoId());
            IssueTicketDetail detail = new IssueTicketDetail();
            detail.setTicket(ticket);
            Item item = itemRepo.findById(manufactureOrder.getItem().getItemId())
                    .orElseThrow(() -> new CustomException("Item không tồn tại", HttpStatus.NOT_FOUND));
            detail.setItem(item);
            detail.setQuantity(manufactureOrder.getQuantity());
            detail.setNote(request.getNote());
            details.add(detail);
        }
        else if(request.getIssueType().equals("Sales Order")){
            SalesOrder salesOrder = salesOrderRepository.findBySoCode(request.getReferenceCode());
            ticket.setReferenceId(salesOrder.getSoId());
            List<SalesOrderDetail> salesOrderDetails = salesOrder.getSalesOrderDetails();
            for (SalesOrderDetail salesOrderDetail : salesOrderDetails) {
                IssueTicketDetail detail = new IssueTicketDetail();
                detail.setTicket(ticket);
                Item item = itemRepo.findById(salesOrderDetail.getItem().getItemId())
                        .orElseThrow(() -> new CustomException("Item không tồn tại", HttpStatus.NOT_FOUND));
                detail.setItem(item);
                detail.setQuantity(salesOrderDetail.getQuantity());
                detail.setNote(request.getNote());
                details.add(detail);
            }
        }
         else  if(request.getIssueType().equals("Transfer Ticket")){
            TransferTicket transferTicket = transferTicketRepository.findByTicketCode(request.getReferenceCode());
            ticket.setReferenceId(transferTicket.getTicketId());
            List<TransferTicketDetail> transferTicketDetails = transferTicket.getTransferTicketDetails();
            for (TransferTicketDetail transferTicketDetail : transferTicketDetails) {
                IssueTicketDetail detail = new IssueTicketDetail();
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
        ticket.setStatus(request.getStatus());
        ticket.setFile(request.getFile());
        ticket.setCreatedOn(LocalDateTime.now());
        ticket.setLastUpdatedOn(LocalDateTime.now());

        ticket.setIssueTicketDetails(details);

        IssueTicket issueTicket= ticketRepo.save(ticket);

        return convertToDto(issueTicket); 
    }

    public List<IssueTicketDto> getAllInCompany(Long companyId) {
        return ticketRepo.findByCompany_CompanyId(companyId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
                
    }

    public IssueTicketDto getById(Long ticketId) {
        IssueTicket ticket = ticketRepo.findById(ticketId)
                .orElseThrow(() -> new CustomException("Issue ticket not found", HttpStatus.NOT_FOUND));
        return convertToDto(ticket);    
    }

    public IssueTicketDto updateStatus(Long ticketId, String status) {
        IssueTicket ticket = ticketRepo.findById(ticketId)
                .orElseThrow(() -> new CustomException("Issue ticket not found", HttpStatus.NOT_FOUND));
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
        String prefix = "IssueTk-" + companyId  + referenceCode;
        return prefix;
    }

    public IssueTicketDto convertToDto(IssueTicket ticket) {
        IssueTicketDto dto = new IssueTicketDto();
        dto.setTicketId(ticket.getTicketId());
        dto.setCompanyId(ticket.getCompany().getCompanyId());

        dto.setWarehouseId(ticket.getWarehouse().getWarehouseId());
        dto.setWarehouseCode(ticket.getWarehouse().getWarehouseCode());
        dto.setWarehouseName(ticket.getWarehouse().getWarehouseName());

        dto.setIssueDate(ticket.getIssueDate());
        dto.setReason(ticket.getReason());
        dto.setIssueType(ticket.getIssueType());
        dto.setReferenceId(ticket.getReferenceId());
        dto.setCreatedBy(ticket.getCreatedBy());

        dto.setCreatedOn(LocalDateTime.now());
        dto.setLastUpdatedOn(LocalDateTime.now());

        dto.setStatus(ticket.getStatus());
        dto.setFile(ticket.getFile());

        List<IssueTicketDetailDto> details = detailRepo
                .findByTicketTicketId(ticket.getTicketId())
                .stream()
                .map(this::convertToDetailDto)
                .collect(Collectors.toList());
        dto.setIssueTicketDetails(details);

        return dto;
     }

    public IssueTicketDetailDto convertToDetailDto(IssueTicketDetail detail) {
        IssueTicketDetailDto dto = new IssueTicketDetailDto();
        dto.setITdetailId(detail.getITdetailId());
        dto.setTicketId(detail.getTicket().getTicketId());

        dto.setItemId(detail.getItem().getItemId());
        dto.setItemCode(detail.getItem().getItemCode());
        dto.setItemName(detail.getItem().getItemName());

        dto.setQuantity(detail.getQuantity());
        dto.setNote(detail.getNote());
        return dto;
     }
}
