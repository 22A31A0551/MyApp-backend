package com.example.loanbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String name,String amount,String date) {
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
}