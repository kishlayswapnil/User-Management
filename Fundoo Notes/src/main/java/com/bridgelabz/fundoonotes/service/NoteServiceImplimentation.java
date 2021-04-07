package com.bridgelabz.fundoonotes.service;

import com.bridgelabz.fundoonotes.dto.LabelDto;
import com.bridgelabz.fundoonotes.dto.NoteDto;
import com.bridgelabz.fundoonotes.dto.ReminderDto;
import com.bridgelabz.fundoonotes.exception.NoteException;
import com.bridgelabz.fundoonotes.exception.UserException;
import com.bridgelabz.fundoonotes.model.*;
import com.bridgelabz.fundoonotes.repository.LabelRepository;
import com.bridgelabz.fundoonotes.repository.NoteRepository;
import com.bridgelabz.fundoonotes.repository.UserRepository;
import com.bridgelabz.fundoonotes.utility.ResponseInfo;
import com.bridgelabz.fundoonotes.utility.TokenGenerator;
import com.sun.tools.javac.resources.CompilerProperties;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@PropertySource("classpath:status.properties")
public class NoteServiceImplimentation implements NoteService {

    @Autowired
    private TokenGenerator tokenGenerator;
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private Environment environment;
    private Response response;
    private Optional<User> userData;


    @Override
    public Response create(NoteDto noteDto, String token) {
        int id = tokenGenerator.retrieveIdFromToken(token);
        userData = userRepository.findById(id);
        if (!(userData.isPresent() && userData.get().isVerified()))
            return response = ResponseInfo.getResponse(
                    Integer.parseInt(environment.getProperty("status.login.errorCode")),
                    environment.getProperty("status.user.existError"));
        Note noteData = modelMapper.map(noteDto, Note.class);
        noteData.setDescription(noteDto.getDescription());
        noteData.setTitle(noteDto.getTitle());
        noteData = noteRepository.save(noteData);

        if (noteData != null) {
            response = ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.success.code")),
                    environment.getProperty("status.note.create.success"));
        }else {
            response = ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.note.errorCode")),
                    environment.getProperty("status.note.errorMessage"));
        }
        return response;
    }


    @Override
    public Response update(NoteDto noteDto, String token, int noteId) {
        int userId = tokenGenerator.retrieveIdFromToken(token);
        userData = userRepository.findById(userId);
        if (!(userData.isPresent() && userData.get().isVerified()))
            return response = ResponseInfo.getResponse(
                    Integer.parseInt(environment.getProperty("status.login.errorCode")),
                    environment.getProperty("status.user.existError"));
        User user = userData.get();
        Note noteUpdate = null;
        if (noteRepository.findByIdAndUser(noteId, user).isPresent()) {
            noteUpdate = noteRepository.findByIdAndUser(noteId, user).get();
        } else if (noteUpdate == null) {
            return response = ResponseInfo.getResponse(
                    Integer.parseInt(environment.getProperty("status.note.errorCode")),
                    environment.getProperty("status.note.exists.error"));
        }
        if (!(noteDto.getTitle() == null || noteDto.getTitle().equals("")))
            noteUpdate.setTitle(noteDto.getTitle());
        if (!(noteDto.getDescription() == null || noteDto.getDescription().equals("")))
            noteUpdate.setDescription(noteDto.getDescription());
        noteUpdate = noteRepository.save(noteUpdate);
        if (noteUpdate != null) {
            response = ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.success.code")),
                    environment.getProperty("status.note.update.success"));
        } else {
            response = ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.note.errorCode")),
                    environment.getProperty("status.note.update.error"));
        }
        return response;
    }




    @Override
    public Response delete (String token,int noteId){
        int userId = tokenGenerator.retrieveIdFromToken(token);
        userData = userRepository.findById(userId);
        if (!(userData.isPresent() && userData.get().isVerified()))
            return response = ResponseInfo.getResponse(
                    Integer.parseInt(environment.getProperty("status.login.errorCode")),
                    environment.getProperty("status.user.existError"));
        User user = userData.get();
        Optional<Note> noteData = noteRepository.findByIdAndUser(noteId, user);
        if (!noteData.isPresent())
            return response = ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.note.errorCode")),
                    environment.getProperty("status.note.exists.error"));
        noteRepository.deleteById(noteId);
        if (!noteRepository.findByIdAndUser(noteId, user).isPresent()) {
            response = ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.success.code")),
                    environment.getProperty("status.note.delete.success"));
        }else {
            response = ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.note.errorCode")),
                    environment.getProperty("status.note.delete.error"));
        }
        return response;
    }

    @Override
    public Object getNote(String token, int noteId) {
        int id = tokenGenerator.retrieveIdFromToken(token);
        userData = userRepository.findById(id);
        if (!(userData.isPresent() && userData.get().isVerified()))
            throw new UserException(Integer.parseInt(environment.getProperty("status.login.errorCode")),
                    environment.getProperty("status.user.existError"));
        User user = userData.get();
        Optional<Note> note = noteRepository.findByIdAndUser(noteId, user);
        if (!note.isPresent())
            throw new NoteException(Integer.parseInt(environment.getProperty("status.note.errorCode")),
                    environment.getProperty("status.note.exists.error"));
        // return note.get();
        return new Response(Integer.parseInt(environment.getProperty("status.note.errorCode")),
                environment.getProperty("status.note.create.error"));
    }

    @Override
    public List<Note> getAllNotes(String token) {
        int id = tokenGenerator.retrieveIdFromToken(token);
        Optional<User> userData = userRepository.findById(id);
        if (!(userData.isPresent() && userData.get().isVerified()))
            throw new UserException(Integer.parseInt(environment.getProperty("status.login.errorCode")),
                    environment.getProperty("status.user.existError"));
        User user = userData.get();
        List<Note> allNotes = noteRepository.findAllByUser(user).stream()
                .filter(u -> !(u.isArchived() || u.isTrashed())).collect(Collectors.toList());
        List<Note> noteList = user.getCollaboratedNotes();
        allNotes.addAll(noteList);
        return allNotes;
    }

    @Override
    public Response archiveNote(String token, int noteId) {
        int id = tokenGenerator.retrieveIdFromToken(token);
        Optional<User> userData = userRepository.findById(id);
        if (!(userData.isPresent() && userData.get().isVerified()))
            return response = ResponseInfo.getResponse(
                    Integer.parseInt(environment.getProperty("status.login.errorCode")),
                    environment.getProperty("status.user.existError"));
        User user = userData.get();
        if (!noteRepository.findByIdAndUser(noteId, user).isPresent())
            return response = ResponseInfo.getResponse(
                    Integer.parseInt(environment.getProperty("status.note.errorCode")),
                    environment.getProperty("status.note.exists.error"));
        Note note = noteRepository.findByIdAndUser(noteId, user).get();
        note.setArchived(!note.isArchived());
        note = noteRepository.save(note);
        if (note != null) {
            response = ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.success.code")),
                    environment.getProperty("status.note.archived.success"));
        } else {
            response = ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.note.errorCode")),
                    environment.getProperty("status.note.archived.error"));
        }
        return response;
    }

    @Override
    public Response pinNote(String token, int noteId) {
        int id = tokenGenerator.retrieveIdFromToken(token);
        Optional<User> userData = userRepository.findById(id);
        if (!(userData.isPresent() && userData.get().isVerified()))
            return response = ResponseInfo.getResponse(
                    Integer.parseInt(environment.getProperty("status.login.errorCode")),
                    environment.getProperty("status.user.existError"));
        User user = userData.get();
        if (!noteRepository.findByIdAndUser(noteId, user).isPresent())
            return response = ResponseInfo.getResponse(
                    Integer.parseInt(environment.getProperty("status.note.errorCode")),
                    environment.getProperty("status.note.exists.error"));
        Note note = noteRepository.findByIdAndUser(noteId, user).get();
        note.setPinned(!note.isPinned());
        note = noteRepository.save(note);
        if (note != null) {
            response = ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.success.code")),
                    environment.getProperty("status.note.pinned.success"));
        } else {
            response = ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.note.errorCode")),
                    environment.getProperty("status.note.pinned.error"));
        }
        return response;
    }

    @Override
    public Response trashNote(String userToken, int noteId) {
        int userId = tokenGenerator.retrieveIdFromToken(userToken);
        Optional<User> userData = userRepository.findById(userId);
        if (!(userData.isPresent() && userData.get().isVerified()))
            return response = ResponseInfo.getResponse(
                    Integer.parseInt(environment.getProperty("status.login.errorCode")),
                    environment.getProperty("status.user.existError"));
        User user = userData.get();
        if (!noteRepository.findByIdAndUser(noteId, user).isPresent())
            return response = ResponseInfo.getResponse(
                    Integer.parseInt(environment.getProperty("status.note.errorCode")),
                    environment.getProperty("status.note.exists.error"));
        Note note = noteRepository.findByIdAndUser(noteId, user).get();
        note.setTrashed(!note.isTrashed());
        note = noteRepository.save(note);
        if (note != null) {
            response = ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.success.code")),
                    environment.getProperty("status.note.trashed.success"));
        } else {
            response = ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.note.errorCode")),
                    environment.getProperty("status.note.trashed.error"));
        }
        return response;
    }

    @Override
    public Response addLabel(String userToken, int noteId, LabelDto labelDto) {
        int userId = tokenGenerator.retrieveIdFromToken(userToken);
        Optional<User> opUser = userRepository.findById(userId);
        if (!(opUser.isPresent() && opUser.get().isVerified()))
            return response = ResponseInfo.getResponse(
                    Integer.parseInt(environment.getProperty("status.login.errorCode")),
                    environment.getProperty("status.user.existError"));
        User user = opUser.get();
        if (!noteRepository.findByIdAndUser(noteId, user).isPresent())
            return response = ResponseInfo.getResponse(
                    Integer.parseInt(environment.getProperty("status.note.errorCode")),
                    environment.getProperty("status.note.exists.error"));
        Note note = noteRepository.findByIdAndUser(noteId, user).get();
        Label label = modelMapper.map(labelDto, Label.class);
        Optional<Label> opLabel = labelRepository.findByNameAndUser(labelDto.getName(), user);
        if (opLabel.isPresent()) {
            label = opLabel.get();
        } else {
            // add a new label
            label.setCreatedDate(LocalDateTime.now());
            label.setModifiedDate(LocalDateTime.now());
            label.setUser(user);
            label.addNote(note);
            label = labelRepository.save(label);
        }
        note.addLabel(label);
        note = noteRepository.save(note);
        if (note != null) {
            response = ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.success.code")),
                    environment.getProperty("status.note.update.success"));
        } else {
            response = ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.note.errorCode")),
                    environment.getProperty("status.note.update.error"));
        }
        return response;
    }

    @Override
    public Response removeLabel(String token, int noteId, LabelDto labelDto) {
        int userId = tokenGenerator.retrieveIdFromToken(token);
        Optional<User> userData = userRepository.findById(userId);
        if (!(userData.isPresent() && userData.get().isVerified()))
            return response = ResponseInfo.getResponse(
                    Integer.parseInt(environment.getProperty("status.login.errorCode")),
                    environment.getProperty("status.user.existError"));
        User user = userData.get();
        if (!noteRepository.findByIdAndUser(noteId, user).isPresent())
            return response = ResponseInfo.getResponse(
                    Integer.parseInt(environment.getProperty("status.note.errorCode")),
                    environment.getProperty("status.note.exists.error"));
        Note note = noteRepository.findByIdAndUser(noteId, user).get();
        Label label = labelRepository.findByNameAndUser(labelDto.getName(), user).get();
        note.removeLabel(label);
        note = noteRepository.save(note);
        if (note != null) {
            response = ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.success.code")),
                    environment.getProperty("status.note.update.success"));
        } else {
            response = ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.note.errorCode")),
                    environment.getProperty("status.note.update.error"));
        }
        return response;
    }

    @Override
    public Response addReminder(String userToken, int noteId, ReminderDto reminderDto) {
        int userId = tokenGenerator.retrieveIdFromToken(userToken);
        User user = userRepository.findById(userId).get();
        Note note = noteRepository.findByIdAndUser(noteId, user).get();
        note.setReminder(reminderDto.getReminder());
        note.setRepeatReminder(reminderDto.getRepeatReminder());
        note = noteRepository.save(note);
        if (note != null) {
            response = ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.success.code")),
                    environment.getProperty("status.note.update.success"));
        } else {
            response = ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.note.errorCode")),
                    environment.getProperty("status.note.update.error"));
        }
        return response;
    }

    @Override
    public Response removeReminder(String userToken, int noteId) {
        int userId = tokenGenerator.retrieveIdFromToken(userToken);
        User user = userRepository.findById(userId).get();
        Note note = noteRepository.findByIdAndUser(noteId, user).get();
        note.setReminder(null);
        note.setRepeatReminder(null);
        note = noteRepository.save(note);
        if (note != null) {
            response = ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.success.code")),
                    environment.getProperty("status.note.update.success"));
        } else {
            response = ResponseInfo.getResponse(Integer.parseInt(environment.getProperty("status.note.errorCode")),
                    environment.getProperty("status.note.update.error"));
        }
        return response;
    }


}
