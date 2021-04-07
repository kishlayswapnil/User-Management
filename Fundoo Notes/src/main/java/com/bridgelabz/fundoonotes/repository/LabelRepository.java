package com.bridgelabz.fundoonotes.repository;

import com.bridgelabz.fundoonotes.model.Label;
import com.bridgelabz.fundoonotes.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface LabelRepository extends JpaRepository<Label, Integer> {
        Optional<Label> findByNameAndUser(String name, User user);
        Optional<Label> findByIdAndUser(int id, User user);
        Set<Label> findAllByUser(User user);
}
