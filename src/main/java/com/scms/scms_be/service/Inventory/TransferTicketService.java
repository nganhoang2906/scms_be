package com.scms.scms_be.service.Inventory;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import com.scms.scms_be.model.entity.Inventory.TransferTicket;
import com.scms.scms_be.model.entity.Inventory.TransferTicketDetail;
import com.scms.scms_be.model.request.Inventory.TransferTicketDetailRequest;
import com.scms.scms_be.model.request.Inventory.TransferTicketRequest;
import com.scms.scms_be.repository.General.CompanyRepository;
import com.scms.scms_be.repository.General.ItemRepository;
import com.scms.scms_be.repository.General.WarehouseRepository;
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

  public TransferTicketDto createTicket(TransferTicketRequest request) {
    TransferTicket ticket = new TransferTicket();

    Company company = companyRepo.findById(request.getCompanyId())
        .orElseThrow(() -> new CustomException("Không tìm thấy công ty!", HttpStatus.NOT_FOUND));

    Warehouse fromWarehouse = warehouseRepo.findById(request.getFromWarehouseId())
        .orElseThrow(() -> new CustomException("Không tìm thấy kho xuất!", HttpStatus.NOT_FOUND));

    Warehouse toWarehouse = warehouseRepo.findById(request.getToWarehouseId())
        .orElseThrow(() -> new CustomException("Không tìm thấy kho nhập!", HttpStatus.NOT_FOUND));

    ticket.setCompany(company);
    ticket.setTicketCode(generateTransferTicketCode(request.getCompanyId()));
    ticket.setFromWarehouse(fromWarehouse);
    ticket.setToWarehouse(toWarehouse);
    ticket.setReason(request.getReason());
    ticket.setCreatedBy(request.getCreatedBy());
    ticket.setCreatedOn(LocalDateTime.now());
    ticket.setLastUpdatedOn(LocalDateTime.now());
    ticket.setStatus(request.getStatus());
    ticket.setFile(request.getFile());

    TransferTicket savedTicket = ticketRepo.save(ticket);

    if (request.getTransferTicketDetails() == null || request.getTransferTicketDetails().isEmpty()) {
      throw new CustomException("Danh sách hàng hóa trong phiếu chuyển kho không được để trống!",
          HttpStatus.BAD_REQUEST);
    }

    if (request.getTransferTicketDetails() != null) {
      for (TransferTicketDetailRequest detailRequest : request.getTransferTicketDetails()) {

        Item item = itemRepo.findById(detailRequest.getItemId())
            .orElseThrow(() -> new CustomException("Không tìm thấy hàng hóa!", HttpStatus.NOT_FOUND));

        TransferTicketDetail detail = new TransferTicketDetail();
        detail.setTicket(savedTicket);
        detail.setItem(item);
        detail.setQuantity(detailRequest.getQuantity());
        detail.setNote(detailRequest.getNote());
        detailRepo.save(detail);
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

  public TransferTicketDto updateTicket(Long id, TransferTicketRequest request) {
    TransferTicket ticket = ticketRepo.findById(id)
        .orElseThrow(() -> new CustomException("Không tìm thấy phiếu chuyển kho!", HttpStatus.NOT_FOUND));

    if ("Đã hoàn thành".equals(ticket.getStatus())) {
      throw new CustomException("Không thể cập nhật phiếu đã hoàn thành!", HttpStatus.BAD_REQUEST);
    }
    if ("Đã hủy".equals(ticket.getStatus())) {
      throw new CustomException("Không thể cập nhật phiếu đã bị hủy!", HttpStatus.BAD_REQUEST);
    }

    if (request.getTransferTicketDetails() == null || request.getTransferTicketDetails().isEmpty()) {
      throw new CustomException("Danh sách hàng hóa không được để trống!", HttpStatus.BAD_REQUEST);
    }

    List<TransferTicketDetailRequest> detailRequests = request.getTransferTicketDetails();
    List<TransferTicketDetail> existingDetails = detailRepo.findByTicketTicketId(id);

    List<Long> itemIds = detailRequests.stream()
        .map(TransferTicketDetailRequest::getItemId)
        .collect(Collectors.toList());

    Set<Long> uniqueItemIds = new HashSet<>(itemIds);
    if (uniqueItemIds.size() < itemIds.size()) {
      throw new CustomException("Hàng hóa trong phiếu bị trùng lặp!", HttpStatus.BAD_REQUEST);
    }

    ticket.setStatus(request.getStatus());
    ticket.setLastUpdatedOn(LocalDateTime.now());
    ticket.setReason(request.getReason());
    TransferTicket updatedTicket = ticketRepo.save(ticket);

    for (TransferTicketDetailRequest newDetail : detailRequests) {
      Item item = itemRepo.findById(newDetail.getItemId())
          .orElseThrow(() -> new CustomException("Không tìm thấy hàng hóa!", HttpStatus.NOT_FOUND));

      TransferTicketDetail matchedDetail = existingDetails.stream()
          .filter(detail -> detail.getItem().getItemId().equals(newDetail.getItemId()))
          .findFirst()
          .orElse(null);

      if (matchedDetail != null) {
        matchedDetail.setQuantity(newDetail.getQuantity());
        matchedDetail.setNote(newDetail.getNote());
        detailRepo.save(matchedDetail);
      } else {
        TransferTicketDetail detail = new TransferTicketDetail();
        detail.setTicket(updatedTicket);
        detail.setItem(item);
        detail.setQuantity(newDetail.getQuantity());
        detail.setNote(newDetail.getNote());
        detailRepo.save(detail);
      }
    }

    List<Long> newItemIds = itemIds;
    for (TransferTicketDetail existingDetail : existingDetails) {
      if (!newItemIds.contains(existingDetail.getItem().getItemId())) {
        detailRepo.delete(existingDetail);
      }
    }

    return convertToDto(updatedTicket);
  }

  public String generateTransferTicketCode(Long companyId) {
    String prefix = "TT" + companyId;
    String year = String.valueOf(LocalDateTime.now().getYear()).substring(2);
    int count = ticketRepo.countByTicketCodeStartingWith(prefix);
    return prefix + year + String.format("%04d", count + 1);
  }

  private TransferTicketDto convertToDto(TransferTicket ticket) {
    TransferTicketDto dto = new TransferTicketDto();
    dto.setTicketId(ticket.getTicketId());
    dto.setCompanyId(ticket.getCompany().getCompanyId());

    dto.setTicketCode(ticket.getTicketCode());

    dto.setFromWarehouseId(ticket.getFromWarehouse().getWarehouseId());
    dto.setFromWarehouseName(ticket.getFromWarehouse().getWarehouseName());
    dto.setFromWarehouseCode(ticket.getFromWarehouse().getWarehouseCode());

    dto.setToWarehouseId(ticket.getToWarehouse().getWarehouseId());
    dto.setToWarehouseName(ticket.getToWarehouse().getWarehouseName());
    dto.setToWarehouseCode(ticket.getToWarehouse().getWarehouseCode());

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
