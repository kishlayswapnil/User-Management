package com.bridgelabz.user.service;

import com.bridgelabz.user.dto.ForgotDto;
import com.bridgelabz.user.dto.LoginDto;
import com.bridgelabz.user.dto.RegisterDto;
import com.bridgelabz.user.dto.ResetDto;
import com.bridgelabz.user.model.Response;
import com.bridgelabz.user.model.ResponseList;
import com.bridgelabz.user.model.ResponseToken;

public interface UserService {
    ResponseToken login(LoginDto user);

    Response register(RegisterDto userRecord) throws Exception;

    Response forgotPassword(ForgotDto forgotDto);

    Response userVerification(String token);

    Response resetPassword(ResetDto resetDto, String token);

    Response logout(String token);

    Response addPermission(String token);

    ResponseList SearchUser(String token, String firstName);

    Response deleteUser (String token, int noteId);

    ResponseList getUser(String token);

    Response totalUser(String token);

    ResponseList activeUsers(String token);

    ResponseList inActiveUsers(String token);

    ResponseList onlineUsers(String token);

    ResponseList sortByRegistrationDate(String token);
}
