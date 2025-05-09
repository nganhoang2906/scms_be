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
import com.scms.scms_be.model.entity.Inventory.ReceiveTicket;
import com.scms.scms_be.model.entity.Inventory.ReceiveTicketDetail;
import com.scms.scms_be.model.entity.Inventory.TransferTicket;
import com.scms.scms_be.model.entity.Inventory.TransferTicketDetail;
import com.scms.scms_be.model.entity.Manufacturing.ManufactureOrder;
import com.scms.scms_be.model.entity.Purchasing.PurchaseOrder;
import com.scms.scms_be.model.entity.Purchasing.PurchaseOrderDetail;
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
        .orElseThrow(() -> new CustomException("Không tìm thấy công ty!", HttpStatus.NOT_FOUND));
    Warehouse warehouse = warehouseRepo.findById(request.getWarehouseId())
        .orElseThrow(() -> new CustomException("Không tìm thấy kho!", HttpStatus.NOT_FOUND));

    ReceiveTicket ticket = new ReceiveTicket();
    ticket.setCompany(company);
    ticket.setWarehouse(warehouse);
    ticket.setTicketCode(generateTicketCode(company.getCompanyId()));
    ticket.setReceiveDate(request.getReceiveDate());
    ticket.setReason(request.getReason());
    ticket.setReceiveType(request.getReceiveType());

    List<ReceiveTicketDetail> details = new ArrayList<>();

    if (request.getReceiveType().equals("Sản xuất")) {
      ManufactureOrder manufactureOrder = manufactureOrderRepository.findByMoCode(request.getReferenceCode());
      if (manufactureOrder == null) {
        throw new CustomException("Không tìm thấy công lệnh sản xuất!", HttpStatus.NOT_FOUND);
      }
      ticket.setReferenceId(manufactureOrder.getMoId());
      ReceiveTicketDetail detail = new ReceiveTicketDetail();
      detail.setTicket(ticket);
      Item item = itemRepo.findById(manufactureOrder.getItem().getItemId())
          .orElseThrow(() -> new CustomException("Không tìm thấy hàng hóa!", HttpStatus.NOT_FOUND));
      detail.setItem(item);
      detail.setQuantity(manufactureOrder.getQuantity());
      details.add(detail);
    } else if (request.getReceiveType().equals("Mua hàng")) {
      PurchaseOrder purchaseOrder = purchaseOrderRepository.findByPoCode(request.getReferenceCode());
      if (purchaseOrder == null) {
        throw new CustomException("Không tìm thấy đơn mua hàng!", HttpStatus.NOT_FOUND);
      }
      ticket.setReferenceId(purchaseOrder.getPoId());
      for (PurchaseOrderDetail purchaseOrderDetail : purchaseOrder.getPurchaseOrderDetails()) {
        ReceiveTicketDetail detail = new ReceiveTicketDetail();
        detail.setTicket(ticket);
        Item item = itemRepo.findById(purchaseOrderDetail.getItem().getItemId())
            .orElseThrow(() -> new CustomException("Không tìm thấy hàng hóa!", HttpStatus.NOT_FOUND));
        detail.setItem(item);
        detail.setQuantity(purchaseOrderDetail.getQuantity());
        detail.setNote(request.getNote());
        details.add(detail);
      }
    } else if (request.getReceiveType().equals("Chuyển kho")) {
      TransferTicket transferTicket = transferTicketRepository.findByTicketCode(request.getReferenceCode());
      if (transferTicket == null) {
        throw new CustomException("Không tìm thấy đơn chuyển kho!", HttpStatus.NOT_FOUND);
      }
      ticket.setReferenceId(transferTicket.getTicketId());
      for (TransferTicketDetail transferTicketDetail : transferTicket.getTransferTicketDetails()) {
        ReceiveTicketDetail detail = new ReceiveTicketDetail();
        detail.setTicket(ticket);
        Item item = itemRepo.findById(transferTicketDetail.getItem().getItemId())
            .orElseThrow(() -> new CustomException("Không tìm thấy hàng hóa!", HttpStatus.NOT_FOUND));
        detail.setItem(item);
        detail.setQuantity(transferTicketDetail.getQuantity());
        detail.setNote(request.getNote());
        details.add(detail);
      }
    } else {
      throw new CustomException("Loại phiếu nhập kho không hợp lệ!", HttpStatus.BAD_REQUEST);
    }

    ticket.setCreatedBy(request.getCreatedBy());
    ticket.setCreatedOn(LocalDateTime.now());
    ticket.setLastUpdatedOn(LocalDateTime.now());
    ticket.setStatus(request.getStatus());
    ticket.setFile(request.getFile());
    ticket.setReceiveTicketDetails(details);

    ReceiveTicket receiveTicket = ticketRepo.save(ticket);

    return convertToDto(receiveTicket);
  }

  public List<ReceiveTickeDto> getAllInCompany(Long companyId) {
    return ticketRepo.findByCompany_CompanyId(companyId)
        .stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

  public ReceiveTickeDto getById(Long ticketId) {
    ReceiveTicket ticket = ticketRepo.findById(ticketId)
        .orElseThrow(() -> new CustomException("Phiếu nhập Không tìm thấy kho!", HttpStatus.NOT_FOUND));
    return convertToDto(ticket);
  }

  public ReceiveTickeDto update(Long ticketId, ReceiveTicketRequest request) {
    ReceiveTicket ticket = ticketRepo.findById(ticketId)
        .orElseThrow(() -> new CustomException("Phiếu nhập Không tìm thấy kho!", HttpStatus.NOT_FOUND));
    if (ticket.getStatus().equals("Đã hoàn thành")) {
      throw new CustomException("Không thể cập nhật phiếu đã hoàn thành!", HttpStatus.BAD_REQUEST);
    }
    if (ticket.getStatus().equals("Đã hủy")) {
      throw new CustomException("Không thể cập nhật phiếu đã bị hủy!", HttpStatus.BAD_REQUEST);
    }
    ticket.setStatus(request.getStatus());
    ticket.setLastUpdatedOn(LocalDateTime.now());
    ticket.setCreatedBy(request.getCreatedBy());
    ticket.setReceiveDate(request.getReceiveDate());

    ticketRepo.save(ticket);
    return convertToDto(ticket);
  }

  public String generateTicketCode(Long companyId) {
    String prefix = "RT" + String.format("%04d", companyId);
    String year = String.valueOf(LocalDateTime.now().getYear()).substring(2);
    int count = ticketRepo.countByTicketCodeStartingWith(prefix);
    return prefix + year + String.format("%03d", count + 1);
  }

  public ReceiveTickeDto convertToDto(ReceiveTicket ticket) {
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

    if (ticket.getReceiveType().equals("Sản xuất")) {
      ManufactureOrder manufactureOrder = manufactureOrderRepository.findByMoId(ticket.getReferenceId());
      dto.setReferenceCode(manufactureOrder != null ? manufactureOrder.getMoCode() : "N/A");
    } else if (ticket.getReceiveType().equals("Mua hàng")) {
      PurchaseOrder purchaseOrder = purchaseOrderRepository.findByPoId(ticket.getReferenceId());
      dto.setReferenceCode(purchaseOrder != null ? purchaseOrder.getPoCode() : "N/A");
    } else if (ticket.getReceiveType().equals("Chuyển kho")) {
      TransferTicket transferTicket = transferTicketRepository.findByTicketId(ticket.getReferenceId());
      dto.setReferenceCode(transferTicket != null ? transferTicket.getTicketCode() : "N/A");
    } else {
      dto.setReferenceCode("N/A");
    }

    dto.setCreatedBy(ticket.getCreatedBy());
    dto.setCreatedOn(ticket.getCreatedOn());
    dto.setLastUpdatedOn(ticket.getLastUpdatedOn());
    dto.setStatus(ticket.getStatus());
    dto.setFile(ticket.getFile());

    List<ReceiveTicketDetailDto> details = detailRepo.findByTicketTicketId(ticket.getTicketId())
        .stream()
        .map(this::convertToDetailDto)
        .collect(Collectors.toList());
    dto.setReceiveTicketDetails(details);

    return dto;
  }

  public ReceiveTicketDetailDto convertToDetailDto(ReceiveTicketDetail detail) {
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
