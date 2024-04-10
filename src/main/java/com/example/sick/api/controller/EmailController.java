package com.example.sick.api.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.sick.service.EmailService;

@RestController
public class EmailController {

    private final EmailService mailer;

    public EmailController() throws Exception {
        this.mailer = new EmailService();
    }

    @GetMapping("/sendEmail")
    public String sendEmail() {
        try {
            mailer.sendMail("Tarzanas", "Ka metu ta pataikau");
            return "Email sent successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send email: " + e.getMessage();
        }
    }
}
