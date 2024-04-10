package com.example.sick.api.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.sick.service.EmailService;

@RestController
public class EmailController {

    private final EmailService gMailer;

    public EmailController() throws Exception {
        this.gMailer = new EmailService();
    }

    @PostMapping("/sendEmail")
    public String sendEmail() {
        try {
            gMailer.sendMail("Tarzanas", "Ka metu ta pataikau");
            return "Email sent successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send email: " + e.getMessage();
        }
    }
}
