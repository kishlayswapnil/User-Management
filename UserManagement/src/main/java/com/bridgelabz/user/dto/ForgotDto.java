package com.bridgelabz.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class ForgotDto {
    @NotEmpty(message = "Enter your email address")
    @Email(message="Email Field should be proper")
    private String emailId;

    //Setters and Getters
    public String getEmailId() {
        return emailId;
    }
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
