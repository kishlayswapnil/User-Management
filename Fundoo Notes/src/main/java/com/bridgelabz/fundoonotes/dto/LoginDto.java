package com.bridgelabz.fundoonotes.dto;

import org.springframework.stereotype.Component;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

//A POJO class for login data.
@Component
public class LoginDto {

    //Variables.
    //@NotNull(message = "Field can not be empty")

    //@Email(message="Enter a valid email id")
    private String emailId;

    //@Pattern(regexp = "^[a-zA-z0-9]{7}",message = "Password Must contain 8 character no special characters")
    private String password;

    //Getters and setters.
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

}