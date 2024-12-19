package com.example.chatroom.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class EmailService {

    protected JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetEmail(String toEmail, String name, String resetLink) throws MessagingException, IOException {
        String htmlContent = loadTemplate("templates/email/password_reset.html")
                .replace("{{name}}", name)
                .replace("{{resetLink}}", resetLink);

        sendHtmlEmail(toEmail, "Password Reset Request", htmlContent);
    }

    public void sendVerificationEmail(String toEmail, String name, String verificationLink) throws MessagingException, IOException {
        String htmlContent = loadTemplate("templates/email/verify_email.html")
                .replace("{{name}}", name)
                .replace("{{verificationLink}}", verificationLink);

        sendHtmlEmail(toEmail, "Email Verification", htmlContent);
    }

    private String loadTemplate(String path) throws IOException {
        Path templatePath = new ClassPathResource(path).getFile().toPath();
        return Files.readString(templatePath);
    }

    private void sendHtmlEmail(String toEmail, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // `true` indicates HTML content

        mailSender.send(message);
    }

}
