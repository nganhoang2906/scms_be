package com.scms.scms_be.controller.Inventory;

import com.scms.scms_be.model.dto.Inventory.IssueTicketDto;
import com.scms.scms_be.model.request.Inventory.IssueTicketRequest;
import com.scms.scms_be.service.Inventory.IssueTicketService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class IssueTicketController {

  @Autowired
  private IssueTicketService issueTicketService;

  @PostMapping("/user/create-issue-ticket")
  public ResponseEntity<IssueTicketDto> createIssueTicket(@RequestBody IssueTicketRequest request) {
    IssueTicketDto created = issueTicketService.createIssueTicket(request);
    return ResponseEntity.ok(created);
  }

  @GetMapping("/user/get-all-issue-ticket-in-com/{companyId}")
  public ResponseEntity<List<IssueTicketDto>> getAllIssueTicketsByCompany(@PathVariable Long companyId) {
    return ResponseEntity.ok(issueTicketService.getAllInCompany(companyId));
  }

  @GetMapping("/user/get-issue-ticket/{ticketId}")
  public ResponseEntity<IssueTicketDto> getIssueTicketById(@PathVariable Long ticketId) {
    return ResponseEntity.ok(issueTicketService.getById(ticketId));
  }

  @PutMapping("/user/update-issue-ticket/{ticketId}")
  public ResponseEntity<IssueTicketDto> updateIssueTicketStatus(
      @PathVariable Long ticketId,
      @RequestBody IssueTicketRequest request) {
    return ResponseEntity.ok(issueTicketService.updateTicket(ticketId, request));
  }
  
}
