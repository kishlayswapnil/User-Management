package com.bridgelabz.fundoonotes.service;

import com.bridgelabz.fundoonotes.dto.LoginDto;
import com.bridgelabz.fundoonotes.dto.RegisterDto;
import com.bridgelabz.fundoonotes.exception.UserException;
import com.bridgelabz.fundoonotes.model.MailObject;
import com.bridgelabz.fundoonotes.model.Response;
import com.bridgelabz.fundoonotes.model.ResponseToken;
import com.bridgelabz.fundoonotes.model.User;
import com.bridgelabz.fundoonotes.repository.UserRepository;
import com.bridgelabz.fundoonotes.utility.ResponseInfo;
import com.bridgelabz.fundoonotes.utility.TokenGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@PropertySource("classpath:status.properties")
public class UserServiceImplements implements UserService {

    private User userInformation = new User();
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private UserRepository userRepository;

    //Fetching the mail object class from model.
    @Autowired
    private MailObject mailObject;

    //@Autowired
    //private ModelMapper modelMapper;

    @Autowired
    private MailService mailService;

    @Autowired
    private Environment environment;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public ResponseToken login(LoginDto loginDto) {

        Optional<User> user = userRepository.findUserByEmailId(loginDto.getEmailId());
        if (user.isPresent() && bCryptPasswordEncoder.matches(loginDto.getPassword(), user.get().getPassword())) {
            if (user.get().isVerified()) {
                String token = tokenGenerator.generateUserToken(user.get().getId());
                return ResponseInfo.getResponseToken(Integer.parseInt(environment.getProperty("status.success.code")),
                        environment.getProperty("status.login.success"),token);
            }
            else
                // user is not verified yet
                throw new UserException(Integer.parseInt(environment.getProperty("status.login.errorCode")), environment.getProperty("status.login.unVerifiedUser"));
        }
        throw new UserException(Integer.parseInt(environment.getProperty("status.login.errorCode")), environment.getProperty("status.login.invalidUser"));
    }

    @Override
    public Response register(RegisterDto registerDto) throws Exception, NullPointerException {

        //Fetching email from dto.
        Optional<User> userMail = userRepository.findUserByEmailId(registerDto.getEmailId());
        if (userMail == null) {

            //Added Mapping to connect dto and entity and then saving in DB.
            User userInformation = modelMapper.map(registerDto, User.class);
            String password = bCryptPasswordEncoder.encode(registerDto.getPassword());
            userInformation.setPassword(password);
            userInformation.setVerified(true);
            String mailResponse = mailService.fromMessage("http://localhost:8080/verification/",
                    tokenGenerator.generateUserToken(userInformation.getId()));
            mailObject.setEmail(registerDto.getEmailId());
            mailObject.setMessage(mailResponse);
            mailService.sendMail(registerDto.getEmailId(),mailResponse);

            userRepository.save(userInformation);
            throw new UserException(Integer.parseInt(environment.getProperty("status.success.code")), environment.getProperty("status.register.success"));
        }
        throw new UserException(Integer.parseInt(environment.getProperty("status.register.errorCode")), environment.getProperty("status.register.duplicateEmailError"));

    }

    @Override
    public Response forgotPassword(String email) {
        Optional<User> user = userRepository.findUserByEmailId(email);
        if(!user.isPresent())
            throw new UserException(Integer.parseInt(environment.getProperty("status.forgotPassword.errorCode")), environment.getProperty("status.forgotPassword.invalidEmail"));
        String passwordResetLink = "http://localhost:8080/verification/";
        passwordResetLink = tokenGenerator.generateUserToken(user.get().getId());
        mailService.sendMail(user.get().getEmailId(), passwordResetLink);
        Response response = ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.success.code")), environment.getProperty("status.forgotPassword.success"));
        return response;
    }

    @Override
    public Response userVerification(String token) {
        int id = tokenGenerator.retrieveIdFromToken(token);
        Optional<User> user = userRepository.findById(id);
        if(!user.isPresent())
            throw new UserException(Integer.parseInt(environment.getProperty("status.user.errorCode")), environment.getProperty("status.user.existError"));
        String passwordResetLink = "http://localhost:8080/verification/";
        passwordResetLink = passwordResetLink + tokenGenerator.generateUserToken(user.get().getId());
        mailService.sendMail(user.get().getEmailId(), passwordResetLink);
        Response response = ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.success.code")),
                environment.getProperty("status.user.success"));
        return response;
    }

    @Override
    public Response resetPassword(String newPassword, String token) {
        int id = tokenGenerator.retrieveIdFromToken(token);
        Optional<User> user = userRepository.findById(id);
        if(!user.isPresent())
            throw new UserException(Integer.parseInt(environment.getProperty("status.user.errorCode")), environment.getProperty("status.user.invalidUser"));
        user.get().setPassword(bCryptPasswordEncoder.encode(newPassword));
        if(userRepository.save(user.get()) == null)
            throw new UserException(Integer.parseInt(environment.getProperty("status.dataSaving.errorCode")), environment.getProperty("status.saveError"));
        return ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.success.code")),environment.getProperty("status.resetPassword.success"));
    }
}
