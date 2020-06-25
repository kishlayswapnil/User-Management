package com.bridgelabz.user.dto;

import lombok.Data;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;


@Data
public class RegisterDto {
    //@NotNull(message = "Field Should not be Empty")
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String country;

    //@NumberFormat(pattern = "^[987][0-9]{9}")
    //@Pattern(regexp = "^[987][0-9]{9}",message = "Phone must be 10 numbers start with 9 r 8 r 7")
    private long mobileNumber;

    //@Email(message="Email Field should be proper")
    private String emailId;
    private String address;
    private String userName;

    //@Pattern(regexp = "^[A-z]{1}[a-z]{6}",message = "password Must contain 7 character 1st Capital and remain small")
    private String password;
    private String userRole;
}
