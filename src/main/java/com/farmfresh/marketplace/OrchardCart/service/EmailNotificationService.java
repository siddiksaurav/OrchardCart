package com.farmfresh.marketplace.OrchardCart.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService {
    private final EmailSendingService emailSendingService;
    @Value("${spring.mail.username}")
    String COMPANY_EMAIL;

    public EmailNotificationService(EmailSendingService emailSendingService) {
        this.emailSendingService = emailSendingService;
    }

    @JmsListener(destination = "orderQueue")
    public void handleConfirmationOrder(String toEmail) {
        //TODO: send order details as document
        String subject = "Order Confirmation";
        String body = "Your order has been placed. We have started processing your order. Thank you for using our service";
        emailSendingService.sendSimpleEmail(toEmail, COMPANY_EMAIL, body, subject);
    }
}
