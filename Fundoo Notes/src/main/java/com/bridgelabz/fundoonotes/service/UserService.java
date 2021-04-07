package com.bridgelabz.fundoonotes.service;

import com.bridgelabz.fundoonotes.dto.LoginDto;
import com.bridgelabz.fundoonotes.dto.RegisterDto;
import com.bridgelabz.fundoonotes.model.Response;
import com.bridgelabz.fundoonotes.model.ResponseToken;

public interface UserService {
    ResponseToken login(LoginDto user);

    Response register(RegisterDto userRecord) throws Exception;

    Response forgotPassword(String email);

    Response userVerification(String token);

    Response resetPassword(String newPassword, String token);
}
