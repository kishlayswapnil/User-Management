package com.bridgelabz.fundoonotes.service;

import com.bridgelabz.fundoonotes.dto.LabelDto;
import com.bridgelabz.fundoonotes.exception.LabelException;
import com.bridgelabz.fundoonotes.exception.UserException;
import com.bridgelabz.fundoonotes.model.Label;
import com.bridgelabz.fundoonotes.model.Response;
import com.bridgelabz.fundoonotes.model.User;
import com.bridgelabz.fundoonotes.repository.LabelRepository;
import com.bridgelabz.fundoonotes.repository.UserRepository;
import com.bridgelabz.fundoonotes.utility.ResponseInfo;
import com.bridgelabz.fundoonotes.utility.TokenGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Set;

@Service
@PropertySource("classpath:status.properties")
public class LabelServiceImplimentation implements LabelService{

    @Autowired
    private TokenGenerator tokenGenerator;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private Environment environment;
    private Optional<User> userData;
    private Response response;

    @Override
    public Label create(LabelDto labelDto, String token) {
        int userId = tokenGenerator.retrieveIdFromToken(token);
        userData = userRepository.findById(userId);
        if(!(userData.isPresent() && userData.get().isVerified()))
            throw new UserException(Integer.parseInt(environment.getProperty("status.login.errorCode")),environment.getProperty("status.user.existError"));
        User user = userData.get();
        if(labelRepository.findByNameAndUser(labelDto.getName().toLowerCase(), user).isPresent())
            throw new LabelException(environment.getProperty("status.label.duplicate.error"), Integer.parseInt(environment.getProperty("status.label.errorCode")));
        Label label = modelMapper.map(labelDto, Label.class);
        label.setName(label.getName());
        label.setUser(user);
        label = labelRepository.save(label);
        if(label == null)
            throw new LabelException(environment.getProperty("status.label.create.error"), Integer.parseInt(environment.getProperty("status.label.errorCode")));
        return label;
    }

    @Override
    public Response update(LabelDto labelDto, int id, String token) {
        int userId = tokenGenerator.retrieveIdFromToken(token);
        Optional<User> userData = userRepository.findById(userId);
        if(!(userData.isPresent() && userData.get().isVerified()))
            return ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.login.errorCode")),environment.getProperty("status.user.existErrorr"));

        //Check existence
        if(!labelRepository.findByIdAndUser(id, userData.get()).isPresent())
            return  ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.label.errorCode")), environment.getProperty("status.label.exists.error"));

        //Duplicity
        if(labelRepository.findByNameAndUser(labelDto.getName(), userData.get()).isPresent())
            response = ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.label.errorCode")), environment.getProperty("status.label.duplicate.error"));
        Label label = labelRepository.findByIdAndUser(id,userData.get()).get();
        modelMapper.map(labelDto, label);
        label.setName(label.getName());
        label = labelRepository.save(label);
        if(label == null)
            response = ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.label.errorCode")), environment.getProperty("status.label.update.error"));
        else
            response = ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.success.code")),environment.getProperty("status.label.update.success"));
        return response;
    }

    @Override
    public Response delete(int id, String token) {
        int userId = tokenGenerator.retrieveIdFromToken(token);
        Optional<User> userData = userRepository.findById(userId);
        if(!(userData.isPresent() && userData.get().isVerified()))
            return  ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.login.errorCode")),environment.getProperty("status.user.existError"));
        //Check existence
        Optional<Label> labelData = labelRepository.findByIdAndUser(id, userData.get());
        if(!labelData.isPresent())
            return  ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.label.errorCode")), environment.getProperty("status.label.exists.error"));

        //Remove the label
        labelRepository.deleteById(id);

        //Operation is successful or not
        if(labelRepository.findByIdAndUser(id, userData.get()).isPresent())
            response = ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.label.errorCode")),environment.getProperty("status.label.delete.error"));
        else
            response = ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.success.code")), environment.getProperty("status.label.delete.success"));
        return response;
    }

    @Override
    public Set<Label> getAllLabels(String token) throws UserException{
        int userId = tokenGenerator.retrieveIdFromToken(token);
        Optional<User> userData = userRepository.findById(userId);
        if(!(userData.isPresent() && userData.get().isVerified()))
            throw new UserException(Integer.parseInt(environment.getProperty("status.login.errorCode")),environment.getProperty("status.user.existError"));
        return labelRepository.findAllByUser(userData.get());
    }

    public Label getLabel(int id, String token) {
        int userId = tokenGenerator.retrieveIdFromToken(token);
        Optional<User> userData = userRepository.findById(userId);
        Label label = labelRepository.findByIdAndUser(id, userData.get()).get();
        return label;
    }
}
