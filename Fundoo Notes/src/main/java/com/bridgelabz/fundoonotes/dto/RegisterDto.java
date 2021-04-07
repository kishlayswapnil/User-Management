package com.bridgelabz.fundoonotes.dto;

import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class RegisterDto {
    @NotNull(message = "Field Should not be Empty")

    private String firstName;

    private String lastName;

    //@Email(message="Email Field should be proper")
    private String emailId;

    //@Pattern(regexp = "^[A-z]{1}[a-z]{6}",message = "password Must contain 7 character 1st Capital and remain small")
    private String password;

    //@NumberFormat(pattern = "^[987][0-9]{9}")
	//@Pattern(regexp = "^[987][0-9]{9}",message = "Phone must be 10 numbers start with 9 r 8 r 7")
    private long mobileNumber;

    /*public RegisterDto(@NotNull(message = "Field Should not be Empty") String firstName, String lastName, @Email(message = "Email Field should be proper") String emailId, @Pattern(regexp = "^[A-z]{1}[a-z]{6}", message = "password Must contain 7 character 1st Capital and remain small") String password, @Pattern(regexp = "^[987][0-9]{9}", message = "Phone must be 10 numbers start with 9 r 8 r 7") long mobileNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
        this.password = password;
        this.mobileNumber = mobileNumber;
    }*/

    public RegisterDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(long mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

}
