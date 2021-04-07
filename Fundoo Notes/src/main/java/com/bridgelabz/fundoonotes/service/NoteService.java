package com.bridgelabz.fundoonotes.service;

import com.bridgelabz.fundoonotes.dto.LabelDto;
import com.bridgelabz.fundoonotes.dto.NoteDto;
import com.bridgelabz.fundoonotes.dto.ReminderDto;
import com.bridgelabz.fundoonotes.model.Note;
import com.bridgelabz.fundoonotes.model.Response;
import java.util.List;

public interface NoteService {
    Response create(NoteDto noteDto, String token);
    Response update(NoteDto noteDto, String token, int noteId);
    Response delete(String token, int noteId);
    Object getNote(String token, int noteId);
    List<Note> getAllNotes(String token);
    Response archiveNote(String token, int noteId);
    Response pinNote(String token, int noteId);
    Response trashNote(String token, int noteId);
    Response addLabel(String token, int noteId,  LabelDto labelDto);
    Response removeLabel(String token, int noteId,  LabelDto labelDto);
    Response addReminder(String token, int noteId, ReminderDto reminderDto);
    Response removeReminder(String token, int noteId);
}
