package com.example.loanbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // ✅ EXISTING: Loan creation email
    public void sendEmail(String to, String name, String amount, String date) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Loan Confirmation");
        message.setText(
                "Hello " + name + ",\n\n" +
                        "Your loan of ₹" + amount + " has been approved.\n\n" +
                        "Date: " + date + "\n\nThank you!"
        );

        mailSender.send(message);
    }

    // ✅ NEW: Reminder email
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

        mailSender.send(message);
    }
}