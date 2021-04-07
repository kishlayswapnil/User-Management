package com.bridgelabz.fundoonotes.controller;

import com.bridgelabz.fundoonotes.dto.LoginDto;
import com.bridgelabz.fundoonotes.dto.RegisterDto;
import com.bridgelabz.fundoonotes.dto.ResetDto;
import com.bridgelabz.fundoonotes.model.Response;
import com.bridgelabz.fundoonotes.model.ResponseToken;
import com.bridgelabz.fundoonotes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//For creating a REST api.
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    //Injecting the object dependency.
    @Autowired
    UserService userService;

    //Login API with mapping.
    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginDto loginDto) {
        ResponseToken response = userService.login(loginDto);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    //Registration API with mapping.
    @PostMapping("/registration")
    public ResponseEntity<Response> register(@RequestBody RegisterDto registerDto) throws Exception {
        Response response = userService.register(registerDto);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    //API for resetting the forgotten password.
    @PostMapping("/forgetPassword")
    public ResponseEntity<Response> forgetPassword(@RequestParam("email") String email) {
        Response response = userService.forgotPassword(email);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    //API for resetting the password.
    @PutMapping("/reset/{token}")
    public ResponseEntity<Response> resetPassword(@RequestBody ResetDto resetDto, @PathVariable String token) {
        Response response = userService.resetPassword(resetDto.getNewPassword(), token);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    //API for user verification.
    @GetMapping("/verification/{token}")
    public ResponseEntity<Response> verify(@PathVariable String token) {
        Response response = userService.userVerification(token);
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
