package com.bridgelabz.user.controller;

import com.bridgelabz.user.dto.ForgotDto;
import com.bridgelabz.user.dto.LoginDto;
import com.bridgelabz.user.dto.RegisterDto;
import com.bridgelabz.user.dto.ResetDto;
import com.bridgelabz.user.model.Response;
import com.bridgelabz.user.model.ResponseList;
import com.bridgelabz.user.model.ResponseToken;
import com.bridgelabz.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

//For creating a REST api.
@RestController
@RequestMapping("/user")
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
    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody RegisterDto registerDto) throws Exception {
        Response response = userService.register(registerDto);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    //API for resetting the forgotten password.
    @PostMapping("/forgetpassword")
    public ResponseEntity<Response> forgetPassword(@RequestBody ForgotDto forgotDto) {
        Response response = userService.forgotPassword(forgotDto);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    //API for resetting the password.
    @PutMapping("/reset/{token}")
    public ResponseEntity<Response> resetPassword(@RequestBody ResetDto resetDto, @RequestHeader(required=false) String token) {
        Response response = userService.resetPassword(resetDto, token);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    //API for user verification.
    @GetMapping("/verification/{token}")
    public ResponseEntity<Response> verify(@PathVariable String token) {
        Response response = userService.userVerification(token);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    //API for user logout.
    @PostMapping("/logout")
    public ResponseEntity<Response> logout(@RequestHeader String token) {
        Response response = userService.logout(token);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    //API for adding permission.
    @PostMapping("/addpermission")
    public ResponseEntity<Response> permission(@RequestHeader String token) {
        Response response = userService.addPermission(token);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    //API for searching user.
    @GetMapping("/search/{name}")
    public ResponseEntity<ResponseList> searchName(@RequestHeader String token, @PathVariable String name) {
        ResponseList response = userService.SearchUser(token, name);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    //API for deleting user.
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteUser(@RequestHeader String token, @PathVariable int id) {
        Response response = userService.deleteUser(token, id);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    //API for user list.
    @GetMapping("/userslist")
    public ResponseEntity<ResponseList> listUsers(@RequestHeader String token) {
        ResponseList response = userService.getUser(token);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    //API for getting total user.
    @GetMapping("/totalusers")
    public ResponseEntity<Response> totalUsers(@RequestHeader String token) {
        Response response = userService.totalUser(token);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    //API for getting active user.
    @GetMapping("/activeusers")
    public ResponseEntity<ResponseList> activeUsers(@RequestHeader String token) {
        ResponseList response = userService.activeUsers(token);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    //API for getting inactive user.
    @GetMapping("/inactiveusers")
    public ResponseEntity<ResponseList> inActiveUsers(@RequestHeader String token) {
        ResponseList response = userService.inActiveUsers(token);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    //API for getting online user.
    @GetMapping("/onlineusers")
    public ResponseEntity<ResponseList> onlineUsers(@RequestHeader String token) {
        ResponseList response = userService.onlineUsers(token);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    //API for getting sorted user.
    @GetMapping("/sorting")
    public ResponseEntity<ResponseList> sortByRegisterDate(@RequestHeader String token) {
        ResponseList response = userService.sortByRegistrationDate(token);
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
