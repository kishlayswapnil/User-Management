package com.bridgelabz.user.dto;

import javax.validation.constraints.Pattern;

public class ResetDto {

    //variable.
    @Pattern(regexp = "^[a-zA-z0-9]{7}",message = "Password Must contain 8 character no special characters")
    private String newPassword;

    //Getter And Setter.
    public String getNewPassword() {
        return newPassword;
    }
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
