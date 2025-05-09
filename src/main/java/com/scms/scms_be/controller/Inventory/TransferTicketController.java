package com.scms.scms_be.controller.Inventory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.scms.scms_be.model.dto.Inventory.TransferTicketDto;
import com.scms.scms_be.model.request.Inventory.TransferTicketRequest;
import com.scms.scms_be.service.Inventory.TransferTicketService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TransferTicketController {

  @Autowired
  private TransferTicketService ticketService;

  @PostMapping("/user/create-transfer-ticket")
  public ResponseEntity<TransferTicketDto> createTicket(@RequestBody TransferTicketRequest request) {
    return ResponseEntity.ok(ticketService.createTicket(request));
  }

  @GetMapping("/user/get-transfer-ticket-by-id/{ticketId}")
  public ResponseEntity<TransferTicketDto> getTicketById(@PathVariable Long ticketId) {
    return ResponseEntity.ok(ticketService.getTicketById(ticketId));
  }

  @GetMapping("/user/get-all-transfer-ticket-in-com/{companyId}")
  public ResponseEntity<List<TransferTicketDto>> getAllByCompany(@PathVariable Long companyId) {
    return ResponseEntity.ok(ticketService.getAllByCompany(companyId));
  }

  @PutMapping("/user/update-transfer-ticket/{ticketId}")
  public ResponseEntity<TransferTicketDto> updateTicket(
      @PathVariable Long ticketId,
      @RequestBody TransferTicketRequest request) {
    return ResponseEntity.ok(ticketService.updateTicket(ticketId, request));
  }

}
