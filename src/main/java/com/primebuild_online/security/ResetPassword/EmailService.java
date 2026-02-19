package com.primebuild_online.security.ResetPassword;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendResetPin(String toEmail, String pin) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Password Reset PIN");
        message.setText("Your temporary PIN is: " + pin + "\nIt expires in 10 minutes.");
        mailSender.send(message);
    }
}