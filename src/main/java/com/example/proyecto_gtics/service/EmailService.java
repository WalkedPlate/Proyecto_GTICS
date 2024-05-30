package com.example.proyecto_gtics.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Async
    public void sendEmail(String to, String subject, String body, String password) {
        MimeMessage message = emailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("clinicarenacer.mail.service@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);

            Context context = new Context();
            context.setVariable("subject", subject);
            context.setVariable("link", body);
            context.setVariable("password", password); // Aquí se establece la contraseña generada

            String htmlContent = templateEngine.process("email/temporal_password", context);
            helper.setText(htmlContent, true);

            emailSender.send(message);
        } catch (MessagingException e) {
            // Manejar la excepción según sea necesario
            e.printStackTrace();
        }
    }
}
