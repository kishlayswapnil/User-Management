package com.bridgelabz.fundoonotes.repository;

import com.bridgelabz.fundoonotes.model.Note;
import com.bridgelabz.fundoonotes.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Integer> {
    Optional<Note> findByIdAndUser(int id, User user);
    List<Note> findAllByUser(User user);
}
