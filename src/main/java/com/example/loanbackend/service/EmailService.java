package com.example.loanbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    // ✅ EXISTING: Loan creation email
    @Async
    public void sendEmail(String to, String name, String amount, String date) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Loan Confirmation");
        message.setText(
                "Hello " + name + ",\n\n" +
                        "Your loan of ₹" + amount + " has been approved.\n\n" +
                        "Date: " + date + "\n\nThank you!"
        );
        try {
            mailSender.send(message);
            logger.info("Loan confirmation email sent successfully to {}", to);
        } catch (MailException e) {
            logger.error("Failed to send loan confirmation email to {}: {}", to, e.getMessage());
        }
    }

    // ✅ NEW: Reminder email
    @Async
    public void sendReminderEmail(String to, String name, String amount) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("⚠️ Loan Expiry Reminder");

        message.setText(
                "Hello " + name + ",\n\n" +
                        "Reminder: Your loan of ₹" + amount + " is nearing its expiry.\n\n" +
                        "Please take necessary action to avoid any issues.\n\n" +
                        "Thank you."
        );
        try {
            mailSender.send(message);
            logger.info("Reminder email sent successfully to {}", to);
        } catch (MailException e) {
            logger.error("Failed to send reminder email to {}: {}", to, e.getMessage());
        }
    }
}