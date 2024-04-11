package com.example.sick.api.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.sick.service.EmailService;

@RestController
public class EmailController {

    private final EmailService mailer;

    public EmailController() throws Exception {
        this.mailer = new EmailService();
    }

    @GetMapping("/email/{email}")
    public String sendEmail(@PathVariable String email) {
        try {
            mailer.sendMail(email,"TLizingas Loan",  """
                    Hey there!,
                    
                    Thank you for using the TLizingas loan calculator!
                    We've successfully received your application.
                    
                    TLizingas staff will get in touch with you shortly!
                    
                    Have a great day!
                    TLizingas Team
                    """);       return "Email sent successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send email: " + e.getMessage();
        }
    }
}
