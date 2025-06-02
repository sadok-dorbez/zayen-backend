package com.zayen.services.impl;

import com.zayen.entities.Item;
import com.zayen.entities.Order;
import com.zayen.util.EmailUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}") // Or any other property holding the sender address
    private String from;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);
        javaMailSender.send(message);
    }


    public void sendNewPubEmail(Item item) throws MessagingException, IOException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(EmailUtil.RECIPIENT_EMAIL);
        helper.setSubject(EmailUtil.NEW_PUB_SUBJECT);

        // Charger le contenu du fichier HTML
        ClassPathResource resource = new ClassPathResource("templates/email-template.html");
        InputStream inputStream = resource.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        StringBuilder htmlContent = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            htmlContent.append(line);
        }

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mm:ss ");
        sdf.setTimeZone(TimeZone.getDefault());

        // Utiliser String.format pour remplacer les balises de substitution
        String formattedHtmlContent = String.format(htmlContent.toString(),
                item.getRef(),
                item.getTitle(),
                item.getPrice(),
                item.getCategory(),
                item.getSize(),
                item.getColor(),
                item.getDescription(),
                sdf.format(date));

        // Utiliser le contenu du fichier HTML comme corps de l'e-mail
        helper.setText(formattedHtmlContent, true);

        javaMailSender.send(message);
    }

    public void sendVerificationEmail(String email, String otpCode) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject("Email Verification Code");
        helper.setText("Your verification code is: " + otpCode, true);
        javaMailSender.send(message);
    }


    public void sendOrderStatusUpdate(Order order) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(order.getClient().getEmail());
        message.setSubject("Order Status Updated");
        message.setText("Your order #" + order.getId() + " is now " + order.getStatus());

        javaMailSender.send(message); // use javaMailSender instead of undefined mailSender
    }



}
