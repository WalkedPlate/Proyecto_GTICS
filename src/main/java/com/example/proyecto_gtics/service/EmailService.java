package com.example.proyecto_gtics.service;

import com.example.proyecto_gtics.entity.DetallesOrden;
import com.example.proyecto_gtics.entity.Ordenes;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.WebContext;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private ServletContext servletContext;

    @Async
    public void sendEmail(String to, String subject, String body, String password, String pathToImage, String imageId) { // 1: contraseña temporal, 2: reestablecer contraseña
        MimeMessage message = emailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("clinicarenacer.mail.service@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);

            // Adjuntar la imagen (opcional)
            //FileSystemResource file = new FileSystemResource(new File("/static/img/Login/icono.png"));
            //helper.addInline("image001", file);


            Context context = new Context();
            context.setVariable("subject", subject);
            context.setVariable("link", body);
            context.setVariable("linklogin", "www.clinicarenacer.xyz/login");
            context.setVariable("password", password); // Aquí se establece la contraseña generada
            String htmlContent;

            htmlContent = templateEngine.process("email/temporal_password", context);

            helper.setText(htmlContent, true);

            // Embebiendo imagen
            ClassPathResource image = new ClassPathResource("/static/img/Login/icono.png");
            helper.addInline("icono.png", image);

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

            // Adjuntar la imagen (opcional)
            //FileSystemResource file = new FileSystemResource(new File("/static/img/Login/icono.png"));
            //helper.addInline("image001", file);

            Context context = new Context();
            context.setVariable("subject", subject);
            context.setVariable("link", body);
            context.setVariable("linklogin", "www.clinicarenacer.xyz/login");
            String htmlContent;

            htmlContent = templateEngine.process("email/recuperarContra", context);

            helper.setText(htmlContent, true);

            // Embebiendo imagen
            ClassPathResource image = new ClassPathResource("/static/img/Login/icono.png");
            helper.addInline("icono.png", image);

            emailSender.send(message);
        } catch (MessagingException e) {
            // Manejar la excepción según sea necesario
            e.printStackTrace();
        }
    }

    @Async
    public void sendOrderConfirmationEmail(String to, String subject, Optional<Ordenes> opt, List<DetallesOrden> listaDetalles, String pathToImage, String imageId, HttpServletRequest request, HttpServletResponse response) {
        MimeMessage message = emailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("clinicarenacer.mail.service@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);

            Context context = new Context();
            context.setVariable("subject", subject);
            context.setVariable("ordenActual", opt.get());
            context.setVariable("orderDetails", listaDetalles);
            context.setVariable("linklogin", "www.clinicarenacer.xyz/login");
            String htmlContent = templateEngine.process("email/confirmacion_orden", context);

            helper.setText(htmlContent, true);

            ClassPathResource image = new ClassPathResource(pathToImage);
            helper.addInline(imageId, image);

            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}

