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
import com.scms.scms_be.model.entity.Manufacturing.BOM;
import com.scms.scms_be.model.entity.Manufacturing.BOMDetail;
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
import com.scms.scms_be.repository.Manufacturing.BOMRepository;
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
  private SalesOrderRepository salesOrderRepository;
  @Autowired
  private ItemRepository itemRepo;
  @Autowired
  private BOMRepository bomRepo;

  public IssueTicketDto createIssueTicket(IssueTicketRequest request) {
    Company company = companyRepo.findById(request.getCompanyId())
        .orElseThrow(() -> new CustomException("Không tìm thấy công ty!", HttpStatus.NOT_FOUND));
    Warehouse warehouse = warehouseRepo.findById(request.getWarehouseId())
        .orElseThrow(() -> new CustomException("Không tìm thấy kho!", HttpStatus.NOT_FOUND));

    IssueTicket ticket = new IssueTicket();
    ticket.setCompany(company);
    ticket.setWarehouse(warehouse);
    ticket.setTicketCode(generateTicketCode(company.getCompanyId()));
    ticket.setIssueDate(request.getIssueDate());
    ticket.setReason(request.getReason());
    ticket.setIssueType(request.getIssueType());

    List<IssueTicketDetail> details = new ArrayList<>();

    if (request.getIssueType().equals("Sản xuất")) {
      ManufactureOrder manufactureOrder = manufactureOrderRepository.findByMoCode(request.getReferenceCode());
      if (manufactureOrder == null) {
        throw new CustomException("Không tìm thấy công lệnh sản xuất!", HttpStatus.NOT_FOUND);
      }
      ticket.setReferenceId(manufactureOrder.getMoId());

      BOM bom = bomRepo.findByItem_ItemId(manufactureOrder.getItem().getItemId());
      List<BOMDetail> bomDetails = bom.getBomDetails();
      for (BOMDetail bomDetail : bomDetails) {
        IssueTicketDetail detail = new IssueTicketDetail();
        detail.setTicket(ticket);
        Item item = itemRepo.findById(bomDetail.getItem().getItemId())
            .orElseThrow(() -> new CustomException("Không tìm thấy hàng hóa!", HttpStatus.NOT_FOUND));
        detail.setItem(item);
        detail.setQuantity(bomDetail.getQuantity() * manufactureOrder.getQuantity());
        detail.setNote(request.getNote());
        details.add(detail);
      }

    } else if (request.getIssueType().equals("Bán hàng")) {
      SalesOrder salesOrder = salesOrderRepository.findBySoCode(request.getReferenceCode());
      if (salesOrder == null) {
        throw new CustomException("Không tim thấy đơn bán hàng!", HttpStatus.NOT_FOUND);
      }
      ticket.setReferenceId(salesOrder.getSoId());
      List<SalesOrderDetail> salesOrderDetails = salesOrder.getSalesOrderDetails();
      for (SalesOrderDetail salesOrderDetail : salesOrderDetails) {
        IssueTicketDetail detail = new IssueTicketDetail();
        detail.setTicket(ticket);
        Item item = itemRepo.findById(salesOrderDetail.getItem().getItemId())
            .orElseThrow(() -> new CustomException("Không tìm thấy hàng hóa!", HttpStatus.NOT_FOUND));
        detail.setItem(item);
        detail.setQuantity(salesOrderDetail.getQuantity());
        detail.setNote(request.getNote());
        details.add(detail);
      }

    } else if (request.getIssueType().equals("Chuyển kho")) {
      TransferTicket transferTicket = transferTicketRepository.findByTicketCode(request.getReferenceCode());
      if (transferTicket == null) {
        throw new CustomException("Không tìm thấy đơn chuyển kho!", HttpStatus.NOT_FOUND);
      }
      ticket.setReferenceId(transferTicket.getTicketId());
      List<TransferTicketDetail> transferTicketDetails = transferTicket.getTransferTicketDetails();
      for (TransferTicketDetail transferTicketDetail : transferTicketDetails) {
        IssueTicketDetail detail = new IssueTicketDetail();
        detail.setTicket(ticket);
        Item item = itemRepo.findById(transferTicketDetail.getItem().getItemId())
            .orElseThrow(() -> new CustomException("Không tìm thấy hàng hóa!", HttpStatus.NOT_FOUND));
        detail.setItem(item);
        detail.setQuantity(transferTicketDetail.getQuantity());
        detail.setNote(request.getNote());
        details.add(detail);
      }
    } else {
      throw new CustomException("Loại phiếu xuất kho không hợp lệ!", HttpStatus.BAD_REQUEST);
    }

    ticket.setCreatedBy(request.getCreatedBy());
    ticket.setCreatedOn(LocalDateTime.now());
    ticket.setLastUpdatedOn(LocalDateTime.now());
    ticket.setStatus(request.getStatus());
    ticket.setFile(request.getFile());
    ticket.setIssueTicketDetails(details);

    IssueTicket issueTicket = ticketRepo.save(ticket);

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
        .orElseThrow(() -> new CustomException("Phiếu xuất Không tìm thấy kho!", HttpStatus.NOT_FOUND));
    return convertToDto(ticket);
  }

  public IssueTicketDto updateTicket(Long ticketId, IssueTicketRequest request) {
    IssueTicket ticket = ticketRepo.findById(ticketId)
        .orElseThrow(() -> new CustomException("Phiếu xuất Không tìm thấy kho!", HttpStatus.NOT_FOUND));
    if (ticket.getStatus().equals("Đã hoàn thành")) {
      throw new CustomException("Không thể cập nhật phiếu đã hoàn thành!", HttpStatus.BAD_REQUEST);
    }
    if (ticket.getStatus().equals("Đã hủy")) {
      throw new CustomException("Không thể cập nhật phiếu đã bị hủy!", HttpStatus.BAD_REQUEST);
    }

    ticket.setStatus(request.getStatus());
    ticket.setLastUpdatedOn(LocalDateTime.now());
    ticket.setCreatedBy(request.getCreatedBy());
    ticket.setIssueDate(request.getIssueDate());
    ticketRepo.save(ticket);
    return convertToDto(ticket);
  }

  public String generateTicketCode(Long companyId) {
    String prefix = "IT" + String.format("%04d", companyId);
    String year = String.valueOf(LocalDateTime.now().getYear()).substring(2);
    int count = ticketRepo.countByTicketCodeStartingWith(prefix);
    return prefix + year + String.format("%03d", count + 1);
  }

  public IssueTicketDto convertToDto(IssueTicket ticket) {
    IssueTicketDto dto = new IssueTicketDto();
    dto.setTicketId(ticket.getTicketId());
    dto.setCompanyId(ticket.getCompany().getCompanyId());
    dto.setTicketCode(ticket.getTicketCode());
    dto.setWarehouseId(ticket.getWarehouse().getWarehouseId());
    dto.setWarehouseCode(ticket.getWarehouse().getWarehouseCode());
    dto.setWarehouseName(ticket.getWarehouse().getWarehouseName());
    dto.setIssueDate(ticket.getIssueDate());
    dto.setReason(ticket.getReason());
    dto.setIssueType(ticket.getIssueType());
    dto.setReferenceId(ticket.getReferenceId());

    if (ticket.getIssueType().equals("Sản xuất")) {
      ManufactureOrder manufactureOrder = manufactureOrderRepository.findByMoId(ticket.getReferenceId());
      dto.setReferenceCode(manufactureOrder.getMoCode());
    } else if (ticket.getIssueType().equals("Bán hàng")) {
      SalesOrder salesOrder = salesOrderRepository.findBySoId(ticket.getReferenceId());
      dto.setReferenceCode(salesOrder.getSoCode());
    } else if (ticket.getIssueType().equals("Chuyển kho")) {
      TransferTicket transferTicket = transferTicketRepository.findByTicketId(ticket.getReferenceId());
      dto.setReferenceCode(transferTicket.getTicketCode());
    } else {
      dto.setReferenceCode("N/A");
    }

    dto.setCreatedBy(ticket.getCreatedBy());
    dto.setCreatedOn(ticket.getCreatedOn());
    dto.setLastUpdatedOn(ticket.getLastUpdatedOn());
    dto.setStatus(ticket.getStatus());
    dto.setFile(ticket.getFile());

    List<IssueTicketDetailDto> details = detailRepo.findByTicketTicketId(ticket.getTicketId())
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
