package com.example.proyecto_gtics.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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
    public void sendEmail(String to, String subject, String body, String password, String pathToImage, String imageId) { // 1: contraseña temporal, 2: reestablecer contraseña
        MimeMessage message = emailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("clinicarenacer.mail.service@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            // Embebiendo imagen
            ClassPathResource image = new ClassPathResource("static/img/Login/icono.png");
            helper.addInline("image001", image);

            Context context = new Context();
            context.setVariable("subject", subject);
            context.setVariable("link", body);
            context.setVariable("password", password); // Aquí se establece la contraseña generada
            String htmlContent;

            htmlContent = templateEngine.process("email/temporal_password", context);

            helper.setText(htmlContent, true);

            emailSender.send(message);
        } catch (MessagingException e) {
            // Manejar la excepción según sea necesario
            e.printStackTrace();
        }
    }


    @Async
    public void sendEmailPasswordChange(String to, String subject, String body, String password, String pathToImage, String imageId) { // 1: contraseña temporal, 2: reestablecer contraseña
        MimeMessage message = emailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("clinicarenacer.mail.service@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            // Embebiendo imagen
            ClassPathResource image = new ClassPathResource("static/img/Login/icono.png");
            helper.addInline("image001", image);

            Context context = new Context();
            context.setVariable("subject", subject);
            context.setVariable("link", body);
            context.setVariable("password", password); // Aquí se establece la contraseña generada
            String htmlContent;

            htmlContent = templateEngine.process("email/recuperarContra", context);

            helper.setText(htmlContent, true);

            emailSender.send(message);
        } catch (MessagingException e) {
            // Manejar la excepción según sea necesario
            e.printStackTrace();
        }
    }
}

