package com.bridgelabz.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailService {
    @Autowired
    JavaMailSender javaMailSender;

    public SimpleMailMessage sendMail(String email, String response) {
            SimpleMailMessage simpleMailMsg = new SimpleMailMessage();
            simpleMailMsg.setTo(email);
            simpleMailMsg.setSubject("Mail Verification");
            simpleMailMsg.setText(response);
            javaMailSender.send(simpleMailMsg);
        return simpleMailMsg;
    }

    public String fromMessage(String url,String token) {
        return  url +"/" +token;
    }

    public void sendEmail(SimpleMailMessage mail) {
        javaMailSender.send(mail);
    }
}
