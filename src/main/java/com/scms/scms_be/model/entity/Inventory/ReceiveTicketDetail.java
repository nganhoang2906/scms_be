package com.scms.scms_be.model.entity.Inventory;

import com.scms.scms_be.model.entity.General.Item;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "receive_ticket_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiveTicketDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long RTdetailId;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private ReceiveTicket ticket;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    private Double quantity;
    private String note;
}
