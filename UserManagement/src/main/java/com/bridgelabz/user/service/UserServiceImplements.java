package com.bridgelabz.user.service;

import com.bridgelabz.user.dto.*;
import com.bridgelabz.user.exception.UserException;
import com.bridgelabz.user.model.*;
import com.bridgelabz.user.repository.PermissionRepo;
import com.bridgelabz.user.repository.UserRepository;
import com.bridgelabz.user.utility.ResponseInfo;
import com.bridgelabz.user.utility.TokenGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@PropertySource("classpath:status.properties")
public class UserServiceImplements implements UserService {

    private User userInformation = new User();
    @Autowired
    private UserRepository userRepository;

    //Fetching the mail object class from model.
    @Autowired
    private MailObject mailObject;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MailService mailService;

    @Autowired
    private Environment environment;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private PermissionRepo permissionRepo;

    @Override
    public ResponseToken login(LoginDto loginDto) {

        Optional<User> user = userRepository.findUserByEmailId(loginDto.getEmailId());
        if (user.isPresent() && bCryptPasswordEncoder.matches(loginDto.getPassword(), user.get().getPassword())) {
            if (user.get().isVerified()) {
                String token = tokenGenerator.generateUserToken(user.get().getId());
                user.get().setModifiedDate(LocalDateTime.now());
                user.get().setLogout(false);
                userRepository.save(user.get());
                return ResponseInfo.getResponseToken(Integer.parseInt(environment.getProperty("status.success.code")),
                        environment.getProperty("status.login.success"), token);
            } else
                // user is not verified yet
                throw new UserException(Integer.parseInt(environment.getProperty("status.login.errorCode")), environment.getProperty("status.login.unVerifiedUser"));
        }
        throw new UserException(Integer.parseInt(environment.getProperty("status.login.errorCode")), environment.getProperty("status.login.invalidUser"));
    }

    @Override
    public Response register(RegisterDto registerDto) throws Exception, NullPointerException {

        //Fetching email from dto.
        Optional<User> userMail = userRepository.findUserByEmailId(registerDto.getEmailId());
        if (userMail.isPresent())
            throw new UserException(Integer.parseInt(environment.getProperty("status.register.errorCode")), environment.getProperty("status.register.duplicateEmailError"));
        //Added Mapping to connect dto and entity and then saving in DB.
        User userInformation = modelMapper.map(registerDto, User.class);
        String password = bCryptPasswordEncoder.encode(registerDto.getPassword());
        userInformation.setPassword(password);
        userInformation.setRegistrationDate(LocalDateTime.now());
        userInformation.setModifiedDate(LocalDateTime.now());
        userInformation.setVerified(true);
        try {
            userInformation = userRepository.save(userInformation);
        } catch (Exception e) {
            throw new UserException(Integer.parseInt(environment.getProperty("status.register.errorCode")), environment.getProperty("status.register.duplicateEmailError"));
        }
        String mailResponse = mailService.fromMessage("http://localhost:8080/user/verification",
                tokenGenerator.generateUserToken(userInformation.getId()));
        mailObject.setEmail(userInformation.getEmailId());
        mailObject.setMessage(mailResponse);
        mailService.sendMail(registerDto.getEmailId(), mailResponse);
        Response response = ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.success.code")), environment.getProperty("status.register.success"));
        return response;
    }

    @Override
    public Response forgotPassword(ForgotDto forgotDto) {
        Optional<User> user = userRepository.findUserByEmailId(forgotDto.getEmailId());
        if (!user.isPresent())
            throw new UserException(Integer.parseInt(environment.getProperty("status.forgotPassword.errorCode")), environment.getProperty("status.forgotPassword.invalidEmail"));
        String passwordResetLink = "http://localhost:8080/user/reset/";
        passwordResetLink = passwordResetLink + tokenGenerator.generateUserToken(user.get().getId());
        mailService.sendMail(forgotDto.getEmailId(), passwordResetLink);
        Response response = ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.success.code")), environment.getProperty("status.forgotPassword.success"));
        return response;
    }

    @Override
    public Response userVerification(String token) {
        int id = tokenGenerator.retrieveIdFromToken(token);
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent())
            throw new UserException(Integer.parseInt(environment.getProperty("status.user.errorCode")), environment.getProperty("status.user.existError"));
        String verifiedUser = "http://localhost:8080/user/login/";
        verifiedUser = verifiedUser + tokenGenerator.generateUserToken(user.get().getId());
        mailService.sendMail(user.get().getEmailId(), verifiedUser);
        Response response = ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.success.code")),
                environment.getProperty("status.user.success"));
        return response;
    }

    @Override
    public Response resetPassword(ResetDto resetDto, String token) {
        int id = tokenGenerator.retrieveIdFromToken(token);
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent())
            throw new UserException(Integer.parseInt(environment.getProperty("status.user.errorCode")), environment.getProperty("status.user.invalidUser"));
        user.get().setPassword(bCryptPasswordEncoder.encode(resetDto.getNewPassword()));
        user.get().setModifiedDate(LocalDateTime.now());
        if (userRepository.save(user.get()) == null)
            throw new UserException(Integer.parseInt(environment.getProperty("status.dataSaving.errorCode")), environment.getProperty("status.saveError"));
        return ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.success.code")), environment.getProperty("status.resetPassword.success"));
    }

    @Override
    public Response logout(String token) {
        int id = tokenGenerator.retrieveIdFromToken(token);
        //Check weather user is present or not
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent())
            throw new UserException(Integer.parseInt(environment.getProperty("status.user.errorCode")), environment.getProperty("status.user.invalidUser"));
        user.get().setLogout(true);
        userRepository.save(user.get());
        return new Response(Integer.parseInt(environment.getProperty("status.success.code")),
                environment.getProperty("status.logout.success"));
    }

    @Override
    public Response addPermission(String token) {
        int id = tokenGenerator.retrieveIdFromToken(token);
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent())
            throw new UserException(Integer.parseInt(environment.getProperty("status.user.errorCode")), environment.getProperty("status.user.existError"));
        Permissions permissions = new Permissions();
        if (user.get().getUserRole().equals("Admin")) {
            permissions.setAddDashboard(true);
            permissions.setDeleteDashboard(true);
            permissions.setModifyDashboard(true);
            permissions.setReadDashboard(true);
            permissions.setAddSetting(true);
            permissions.setDeleteSetting(true);
            permissions.setModifySetting(true);
            permissions.setReadSetting(true);
            permissions.setAddUserInformation(true);
            permissions.setDeleteUserInformation(true);
            permissions.setModifyUserInformation(true);
            permissions.setReadUserInformation(true);
            permissions.setUsers(user.get());
            permissionRepo.save(permissions);
            return new Response(Integer.parseInt(environment.getProperty("status.success.code")),
                    environment.getProperty("Sucessfully Added Admin Permisssion "));
        } else {
            permissions.setAddUserInformation(true);
            permissions.setDeleteUserInformation(true);
            permissions.setModifyUserInformation(true);
            permissions.setReadUserInformation(true);
            permissions.setUsers(user.get());
            permissionRepo.save(permissions);
            return new Response(Integer.parseInt(environment.getProperty("status.success.code")),
                    environment.getProperty( "Sucessfully Added User Permisssion "));
        }
    }

    @Override
    public ResponseList SearchUser(String token, String firstName) {
        List<User> userList = userRepository.findAll();
        int id = tokenGenerator.retrieveIdFromToken(token);
        System.out.println(firstName);
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new UserException(Integer.parseInt(environment.getProperty("status.dataSaving.errorCode")), environment.getProperty("status.saveError"));
        } else {
            System.out.println(user.get().getFirstName());
            System.out.println(userList);
            List<User> list = userList.stream()
                    .filter(users -> users.getFirstName().contains(firstName)
                            || (user.get().getMiddleName().contains(firstName)) || (user.get().getLastName().contains(firstName))
                            || (user.get().getEmailId().contains(firstName)))
                    .collect(Collectors.toList());
            return new ResponseList(Integer.parseInt(environment.getProperty("status.success.code")),
                    environment.getProperty( " Searching the User"), list);
        }
    }

    @Override
    public Response deleteUser (String token, int id){
        int userId = tokenGenerator.retrieveIdFromToken(token);
        Optional<User> userData = userRepository.findById(userId);
        if (!(userData.isPresent() && userData.get().isVerified()))
            return new Response(
                    Integer.parseInt(environment.getProperty("status.login.errorCode")),
                    environment.getProperty("status.user.existError"));
        userRepository.deleteById(id);
        if (!userRepository.findById(id).isPresent()) {
            return new Response(Integer.parseInt(environment.getProperty("status.success.code")),
                    environment.getProperty("status.note.delete.success"));
        }else {
            return new Response(Integer.parseInt(environment.getProperty("status.note.errorCode")),
                    environment.getProperty("status.note.delete.error"));
        }
    }

    @Override
    public ResponseList getUser(String token) {
        int userId = tokenGenerator.retrieveIdFromToken(token);
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new UserException(Integer.parseInt(environment.getProperty("status.dataSaving.errorCode")), environment.getProperty("status.saveError"));
        }
        if (userRepository.findAll().isEmpty()) {
            throw new UserException(Integer.parseInt(environment.getProperty("status.dataSaving.errorCode")), environment.getProperty("status.saveError"));
        } else {
            return new ResponseList(Integer.parseInt(environment.getProperty("status.success.code")),
                    environment.getProperty("Successfully showing the user"), userRepository.findAll());
        }
    }

    @Override
    public Response totalUser(String token) {
        int userId = tokenGenerator.retrieveIdFromToken(token);
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new UserException(Integer.parseInt(environment.getProperty("status.dataSaving.errorCode")), environment.getProperty("status.saveError"));
        }
        userRepository.findAll().size();
        return new Response(Integer.parseInt(environment.getProperty("status.success.code")),
                environment.getProperty( "Successfully showing the total number of User"));
    }

    public ResponseList activeUsers(String token) {
        int userId = tokenGenerator.retrieveIdFromToken(token);
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            return new ResponseList(Integer.parseInt(environment.getProperty("status.dataSaving.errorCode")),
                    environment.getProperty( "status.user.invalidUser"), false);
        }
        List<User> activeUser = userRepository.findAll();
        if (!user.isPresent()) {
            throw new UserException(Integer.parseInt(environment.getProperty("status.dataSaving.errorCode")), environment.getProperty("status.saveError"));
        }
        List<User> active = activeUser.stream().filter(note1
                ->note1.isVerified() == true).collect(Collectors.toList());
        return new ResponseList(Integer.parseInt(environment.getProperty("status.success.code")),
                environment.getProperty("Successfully Showing the Active Users"),active);
    }

    public ResponseList inActiveUsers(String token) {
        int userId = tokenGenerator.retrieveIdFromToken(token);
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            return new ResponseList(Integer.parseInt(environment.getProperty("status.dataSaving.errorCode")),
                    environment.getProperty( "status.user.invalidUser"),false);
        }
        List<User> inActiveUser = userRepository.findAll();
        if (!user.isPresent()) {
            throw new UserException(Integer.parseInt(environment.getProperty("status.dataSaving.errorCode")), environment.getProperty("status.saveError"));
        }
        List<User> inActive = inActiveUser.stream().filter(note1
                ->note1.isVerified() == false).collect(Collectors.toList());
        return new ResponseList(Integer.parseInt(environment.getProperty("status.success.code")),
                environment.getProperty( "Successfully Showing the InActive Users"),inActive);
    }

    public ResponseList onlineUsers(String token) {
        int userId = tokenGenerator.retrieveIdFromToken(token);
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            return new ResponseList(Integer.parseInt(environment.getProperty("status.dataSaving.errorCode")),
                    environment.getProperty( "status.user.invalidUser"),false);
        }
        List<User> onlineUser = userRepository.findAll();
        if (!user.isPresent()) {
            throw new UserException(Integer.parseInt(environment.getProperty("status.dataSaving.errorCode")), environment.getProperty("status.user.invalidUser"));
        }
        List<User> online = onlineUser.stream().filter(note1
                ->note1.getModifiedDate() == user.get().getModifiedDate()).collect(Collectors.toList());
        return new ResponseList(Integer.parseInt(environment.getProperty("status.success.code")),
                environment.getProperty("Successfully Showing the Online Users"), online.size());
    }

    @Override
    public ResponseList sortByRegistrationDate(String token) {
        int userId = tokenGenerator.retrieveIdFromToken(token);
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent())
            throw  new UserException(Integer.parseInt(environment.getProperty("status.dataSaving.errorCode")), environment.getProperty("status.user.invalidUser"));
        List<User> sort = userRepository.findAll();
        List<User> users = sort.stream().sorted(Comparator.comparing(User::getRegistrationDate)).collect(Collectors.toList());
        return new ResponseList(Integer.parseInt(environment.getProperty("status.success.code")),
                environment.getProperty("Successfully Showing the sorted Users"), users);
    }
}
