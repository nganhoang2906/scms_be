package com.scms.scms_be.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.scms.scms_be.model.dto.ProductItem;
import com.scms.scms_be.model.entity.Invoice;
import com.scms.scms_be.repository.InvoiceRepository;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // Thư mục lưu file PDF
    private static final String PDF_DIRECTORY = "src/main/resources/invoices/";
    private static final String FONT_PATH = "src/main/resources/fonts/Roboto.ttf";

    public ResponseEntity<String> generateAndSaveInvoice(Map<String, Object> invoiceData) {

        try {
            // Lấy dữ liệu từ request
            String email = (String) invoiceData.get("email");
            String name = (String) invoiceData.get("name");
            String address = (String) invoiceData.get("address");
            String company = (String) invoiceData.get("company");
            List<Map<String, Object>> products = (List<Map<String, Object>>) invoiceData.get("products");

            // Chuyển danh sách sản phẩm thành List<ProductItem>
            List<ProductItem> productItems = products.stream().map(p -> {
                String productName = (String) p.get("name");
                Integer quantity = (Integer) p.getOrDefault("quantity", 0);
                Double price = (Double) p.getOrDefault("price", 0.0);
                double total = quantity * price;
                return new ProductItem(productName, quantity, price, total);
            }).collect(Collectors.toList());

            // Chuyển List<ProductItem> thành JSON
            String productListJson = objectMapper.writeValueAsString(productItems);

            // Tạo thư mục lưu trữ nếu chưa tồn tại
            File directory = new File(PDF_DIRECTORY);
            if (!directory.exists()) {
                directory.mkdirs();
            }

             // Tạo tên file với timestamp
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                String fileName = "invoice_" + timestamp + ".pdf";
                String filePath = PDF_DIRECTORY + fileName;

            // Tạo file PDF trong bộ nhớ
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            // Khởi tạo font tiếng Việt
            BaseFont baseFont = BaseFont.createFont(FONT_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(baseFont, 12, Font.NORMAL);
            Font titleFont = new Font(baseFont, 18, Font.BOLD);

            // Tiêu đề hóa đơn
            Paragraph title = new Paragraph("HÓA ĐƠN MUA HÀNG", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Thông tin khách hàng
            document.add(new Paragraph("Email: " + email, font));
            document.add(new Paragraph("Tên khách hàng: " + name, font));
            document.add(new Paragraph("Công ty: " + company, font));
            document.add(new Paragraph("Địa chỉ: " + address, font));
            document.add(new Paragraph("Ngày tạo: " + java.time.LocalDate.now(), font));
            document.add(new Paragraph("--------------------------------------------------", font));

            // Tạo bảng danh sách sản phẩm
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);
            table.setSpacingAfter(10);

            // Tiêu đề cột
            String[] headers = {"Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            }

            // Thêm dữ liệu sản phẩm
            double totalAmount = 0;
            for (ProductItem product : productItems) {
                table.addCell(new Phrase(product.getProductName(), font));
                table.addCell(new Phrase(String.valueOf(product.getQuantity()), font));
                table.addCell(new Phrase(String.format("%,.2f", product.getPrice()), font));
                table.addCell(new Phrase(String.format("%,.2f", product.getTotal()), font));
                totalAmount += product.getTotal();
            }

            // Thêm tổng tiền
            PdfPCell summaryCell = new PdfPCell(new Phrase("Tổng tiền", font));
            summaryCell.setColspan(3);
            summaryCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(summaryCell);
            table.addCell(new Phrase(String.format("%,.2f", totalAmount), font));

            document.add(table);

            // Chân trang
            Paragraph footer = new Paragraph("Cảm ơn quý khách đã mua hàng!", font);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();

            // Lấy dữ liệu PDF từ bộ nhớ
            byte[] pdfBytes = baos.toByteArray();

            // Lưu file PDF vào src/main/resources với tên cố định là invoice1.pdf
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(pdfBytes);
            }

            // Lưu vào database
            Invoice invoice = new Invoice();
            invoice.setEmail(email);
            invoice.setName(name);
            invoice.setCompany(company);
            invoice.setPdfData(pdfBytes);
            invoice.setProductList(productListJson);
            invoice.setCreateAt(LocalDateTime.now());

            invoiceRepository.save(invoice);

            return ResponseEntity.ok("Hóa đơn đã được tạo, lưu vào DB và file PDF đã được lưu tại: " + filePath);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Đã xảy ra lỗi khi tạo hóa đơn: " + e.getMessage());
        }
    }

    // API lấy tất cả hóa đơn
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        try {
            List<Invoice> invoices = invoiceRepository.findAll();
            return ResponseEntity.ok(invoices);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }
}

