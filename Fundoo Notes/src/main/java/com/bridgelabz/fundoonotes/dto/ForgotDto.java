package com.bridgelabz.fundoonotes.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class ForgotDto implements Serializable {
    private static final long serialVersionUID = 1L;

    //@NotEmpty(message = "Enter your email address")
    //@Pattern(regexp = "^[\\\\w-\\\\+]+(\\\\.[\\\\w]+)*@[\\\\w-]+(\\\\.[\\\\w]+)*(\\\\.[a-z]{2,})$", message="Enter valid email address.!")
    private String emailId;

    //default constructor
    public ForgotDto() {
    }

    //Setters and Getters
    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
