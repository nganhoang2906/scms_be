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

import com.scms.scms_be.model.dto.Inventory.ReceiveTickeDto;
import com.scms.scms_be.model.request.Inventory.ReceiveTicketRequest;
import com.scms.scms_be.service.Inventory.ReceiveTicketService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReceiveTicketController {

  @Autowired
  private final ReceiveTicketService receiveTicketService;

  @PostMapping("/user/create-receive-ticket")
  public ResponseEntity<ReceiveTickeDto> createReceiveTicket(@RequestBody ReceiveTicketRequest request) {
    return ResponseEntity.ok(receiveTicketService.create(request));
  }

  @GetMapping("/user/get-receive-ticket/{ticketId}")
  public ResponseEntity<ReceiveTickeDto> getReceiveTicket(@PathVariable Long ticketId) {
    return ResponseEntity.ok(receiveTicketService.getById(ticketId));
  }

  @GetMapping("/user/get-all-receive-ticket-in-com/{companyId}")
  public ResponseEntity<List<ReceiveTickeDto>> getAllReceiveTicketInCompany(@PathVariable Long companyId) {
    return ResponseEntity.ok(receiveTicketService.getAllInCompany(companyId));
  }

  @PutMapping("/user/update-receive-ticket/{ticketId}")
  public ResponseEntity<ReceiveTickeDto> updateReceiveTicket(
      @PathVariable Long ticketId,
      @RequestBody ReceiveTicketRequest request) {
    return ResponseEntity.ok(receiveTicketService.update(ticketId, request));
  }

}
