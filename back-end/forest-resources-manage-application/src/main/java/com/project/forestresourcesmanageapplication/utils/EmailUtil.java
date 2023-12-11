package com.project.forestresourcesmanageapplication.utils;

import java.util.Map;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailUtil {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public static final String UTF_8_ENCODING = "UTF-8";
    private static final String EMAIL_TEMPLATE = "emailTemplate";

    @Async
    public void sendOtpEmail(String username, String email, String otp) {
        // Xử lý giao diện html của email
        Context context = new Context();
        context.setVariables(
                Map.of("username", username, "otp", otp));
        String text = templateEngine.process(EMAIL_TEMPLATE, context);
        // Thêm phần tin nhắn vào email
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject("Xác thực OTP");
            helper.setFrom("caodanhkhang");
            helper.setTo(email);
            helper.setText(text, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
