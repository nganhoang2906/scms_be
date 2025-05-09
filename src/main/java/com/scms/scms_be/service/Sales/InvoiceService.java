package com.scms.scms_be.service.Sales;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.dto.Sales.InvoiceDto;
import com.scms.scms_be.model.entity.Purchasing.PurchaseOrder;
import com.scms.scms_be.model.entity.Sales.Invoice;
import com.scms.scms_be.model.entity.Sales.SalesOrder;
import com.scms.scms_be.model.entity.Sales.SalesOrderDetail;
import com.scms.scms_be.model.request.Sales.InvoiceRequest;
import com.scms.scms_be.repository.Purchasing.PurchaseOrderRepository;
import com.scms.scms_be.repository.Sales.InvoiceRepository;
import com.scms.scms_be.repository.Sales.SalesOrderRepository;

import jakarta.transaction.Transactional;

@Service
public class InvoiceService {
  @Autowired
  private InvoiceRepository invoiceRepository;
  @Autowired
  private SalesOrderRepository salesOrderRepository;
  @Autowired
  private PurchaseOrderRepository purchaseOrderRepository;
  private static final String pdfDirectory = "invoices/";

  @Transactional
  public InvoiceDto generateInvoice(InvoiceRequest request) {
    SalesOrder salesOrder = salesOrderRepository.findById(request.getSoId())
        .orElseThrow(() -> new CustomException("Không tìm thấy đơn bán hàng!", HttpStatus.NOT_FOUND));

    PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(salesOrder.getPurchaseOrder().getPoId())
        .orElseThrow(() -> new CustomException("Không tìm thấy đơn mua hàng!", HttpStatus.NOT_FOUND));

    Invoice invoice = new Invoice();
    invoice.setSalesOrder(salesOrder);
    invoice.setSalesCompany(purchaseOrder.getSuplierCompany());
    invoice.setPurchaseCompany(purchaseOrder.getCompany());
    invoice.setInvoiceCode(generateInvoiceCode(salesOrder.getSoId()));

    invoice.setTaxRate(salesOrder.getTaxRate());
    invoice.setTotalAmount(salesOrder.getTotalPrice());
    invoice.setTaxAmount(salesOrder.getTaxRate() * salesOrder.getTotalPrice() / 100);
    invoice.setSubTotal(salesOrder.getTotalPrice() + invoice.getTaxAmount());
    invoice.setCreateBy(salesOrder.getCreatedBy());
    invoice.setCreatedOn(LocalDateTime.now());
    invoice.setPaymentMethod(request.getPaymentMethod());
    invoice.setStatus(request.getStatus());

    byte[] pdfBytes = generatePdf(invoice);
    invoice.setFile(pdfBytes);
    Invoice savedInvoice = invoiceRepository.save(invoice);

    return convertToDto(savedInvoice);
  }

  private String generateInvoiceCode(Long soId) {
    String prefix = "HD" + String.valueOf(soId).substring(0, Math.min(5, String.valueOf(soId).length()));
    String year = String.valueOf(LocalDateTime.now().getYear()).substring(2);
    return prefix + year + String.format("%04d", new Random().nextInt(10000));
  }

  private byte[] generatePdf(Invoice invoice) {
    try {
      File directory = new File(pdfDirectory);
      if (!directory.exists()) {
        directory.mkdirs();
      }

      String fileName = "invoice_" + invoice.getInvoiceCode() + ".pdf";
      String filePath = pdfDirectory + fileName;

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      Document document = new Document();
      PdfWriter.getInstance(document, baos);
      document.open();

      BaseFont baseFont = BaseFont.createFont("fonts/Roboto.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
      Font font = new Font(baseFont, 12);
      Font titleFont = new Font(baseFont, 16, Font.BOLD);

      document.add(new Paragraph("HÓA ĐƠN BÁN HÀNG", titleFont));
      document.add(new Paragraph("Mã hóa đơn: " + invoice.getInvoiceCode(), font));
      document.add(new Paragraph("Người tạo: " + invoice.getCreateBy(), font));
      document.add(new Paragraph("Ngày tạo: " + LocalDate.now(), font));
      document.add(new Paragraph("Công ty bán: " + invoice.getSalesCompany().getCompanyName(), font));
      document.add(new Paragraph("Công ty mua: " + invoice.getPurchaseCompany().getCompanyName(), font));
      document.add(new Paragraph("Phương thức thanh toán: " + invoice.getPaymentMethod(), font));
      document.add(new Paragraph("Tình trạng: " + invoice.getStatus(), font));
      document.add(new Paragraph("--------------------------------------------------", font));

      PdfPTable table = new PdfPTable(5);
      table.setWidthPercentage(100);
      table.setSpacingBefore(10);

      String[] headers = { "Mã sản phẩm", "Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền" };
      for (String header : headers) {
        PdfPCell cell = new PdfPCell(new Phrase(header, font));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
      }

      double totalAmount = 0;
      for (SalesOrderDetail detail : invoice.getSalesOrder().getSalesOrderDetails()) {
        String code = detail.getItem().getItemCode();
        String name = detail.getItem().getItemName();
        double quantity = detail.getQuantity();
        double price = detail.getItem().getExportPrice();
        double total = quantity * price;
        totalAmount += total;

        table.addCell(new Phrase(code, font));
        table.addCell(new Phrase(name, font));
        table.addCell(new Phrase(String.valueOf(quantity), font));
        table.addCell(new Phrase(String.format("%,.2f", price), font));
        table.addCell(new Phrase(String.format("%,.2f", total), font));
      }

      PdfPCell totalCell = new PdfPCell(new Phrase("Tổng cộng", font));
      totalCell.setColspan(4);
      totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
      table.addCell(totalCell);
      table.addCell(new Phrase(String.format("%,.2f", totalAmount), font));

      document.add(table);
      document.add(new Paragraph("Thuế: " + invoice.getTaxRate() + "%", font));
      document.add(new Paragraph("Tiền thuế: " + String.format("%,.2f", invoice.getTaxAmount()), font));
      document.add(new Paragraph("Tổng tiền thanh toán: " + String.format("%,.2f", invoice.getSubTotal()), font));
      document.close();

      byte[] pdfBytes = baos.toByteArray();
      try (FileOutputStream fos = new FileOutputStream(filePath)) {
        fos.write(pdfBytes);
      }
      return baos.toByteArray();
    } catch (Exception e) {
      throw new CustomException("Lỗi khi tạo file PDF: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private InvoiceDto convertToDto(Invoice invoice) {
    InvoiceDto dto = new InvoiceDto();
    dto.setInvoiceId(invoice.getInvoiceId());
    dto.setInvoiceCode(invoice.getInvoiceCode());
    dto.setSalesCompanyId(invoice.getSalesCompany().getCompanyId());
    dto.setSalesCompanyName(invoice.getSalesCompany().getCompanyName());
    dto.setPurchaseCompanyId(invoice.getPurchaseCompany().getCompanyId());
    dto.setPurchaseCompanyName(invoice.getPurchaseCompany().getCompanyName());
    dto.setSoId(invoice.getSalesOrder().getSoId());
    dto.setSoCode(invoice.getSalesOrder().getSoCode());
    dto.setSubTotal(invoice.getSubTotal());
    dto.setTaxRate(invoice.getTaxRate());
    dto.setTaxAmount(invoice.getTaxAmount());
    dto.setTotalAmount(invoice.getTotalAmount());
    dto.setPaymentMethod(invoice.getPaymentMethod());
    dto.setCreateBy(invoice.getCreateBy());
    dto.setCreatedOn(invoice.getCreatedOn());
    dto.setStatus(invoice.getStatus());
    return dto;
  }
}
