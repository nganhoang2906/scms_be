package com.scms.scms_be.service.General;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpToEmail(String to, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Hệ thống quản lý chuỗi cung ứng SCMS");
            message.setText("Mã OTP của bạn là: " + otp + ". Vui lòng nhập mã này để xác thực tài khoản.");
            mailSender.send(message);
            System.out.println(" Email đã được gửi thành công đến: " + to);
        } catch (Exception e) {
            System.err.println(" Lỗi khi gửi email: " + e.getMessage());
        }
    }
}

