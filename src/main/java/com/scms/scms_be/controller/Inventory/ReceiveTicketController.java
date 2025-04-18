package com.scms.scms_be.controller.Inventory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping("/comad/create-receive-ticket")
    public ResponseEntity<ReceiveTickeDto> createReceiveTicket(@RequestBody ReceiveTicketRequest request) {
        ReceiveTickeDto response = receiveTicketService.create(request);
            return ResponseEntity.ok(response);
    }

    @GetMapping("/comad/get-receive-ticket/{ticketId}")
    public ResponseEntity<ReceiveTickeDto> getReceiveTicket(@PathVariable Long ticketId) {
        ReceiveTickeDto response = receiveTicketService.getById(ticketId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/comad/get-all-receive-ticket-in-company/{companyId}")
    public ResponseEntity<List<ReceiveTickeDto>> getAllReceiveTicketInCompany(Long companyId) {
        List<ReceiveTickeDto> response = receiveTicketService.getAllInCompany(companyId);
        return ResponseEntity.ok(response);
    }        

    @PutMapping("/comad/update-receive-ticket/{ticketId}")
    public ResponseEntity<ReceiveTickeDto> updateReceiveTicket(
            @PathVariable Long ticketId, 
            @RequestParam("Status") String status) {
        ReceiveTickeDto response = receiveTicketService.updateStatus(ticketId, status);
        return ResponseEntity.ok(response);

    }
}
