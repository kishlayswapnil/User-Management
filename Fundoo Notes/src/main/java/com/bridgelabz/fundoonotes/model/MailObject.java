package com.bridgelabz.fundoonotes.model;

import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class MailObject implements Serializable {

    private static final long serialVersionUID = 1L;

    private String Email;
    private String Message;
    private String Subject;
    public String getEmail() {
        return Email;
    }
    public void setEmail(String email) {
        Email = email;
    }
    public String getMessage() {
        return Message;
    }
    public void setMessage(String message) {
        Message = message;
    }
    public String getSubject() {
        return Subject;
    }
    public void setSubject(String subject) {
        Subject = subject;
    }
}
