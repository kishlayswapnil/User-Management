package com.bridgelabz.user.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

//A POJO class for login data.
@Component
@Getter
@Setter
public class LoginDto {

    //Variables.
    @NotNull(message = "Field can not be empty")
    @Email(message="Enter a valid email id")
    private String emailId;

    @Pattern(regexp = "^[a-zA-z0-9]{7}",message = "Password Must contain 8 character no special characters")
    private String password;

}