package com.example.proyecto_gtics.restcontroller;

import com.example.proyecto_gtics.service.EmailService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public String sendEmail(@RequestBody EmailRequest emailRequest) {
        try {
            emailService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getBody(), emailRequest.getPassword(),
                    emailRequest.getPathToImage(), emailRequest.getImageId());
            return "Email sent successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error sending email";
        }
    }

    @Getter
    @Setter
    static class EmailRequest {
        private String to;
        private String subject;
        private String body;
        private String password;
        private String pathToImage;
        private String imageId;
    }
}
