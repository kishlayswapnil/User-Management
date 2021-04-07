package com.bridgelabz.fundoonotes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Note> notes;


    public Label() {
    }
    // getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Set<Note> getNotes() {
        return notes;
    }

    public void setNotes(Set<Note> notes) {
        this.notes = notes;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    //
    public boolean addNote(Note note) {
        if(notes == null)
            notes = new HashSet<>();
        return notes.add(note);
    }

    public boolean removeNote(Note note) {
        return notes.remove(note);
    }
    //
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Label) {
            Label label = (Label) obj;
            if (this.name.equals(label.getName()))
                return true;
            else
                return false;
        }
        throw new IllegalArgumentException("Can't compare non-Label objects");
    }

    @Override
    public int hashCode() {
        return (name).hashCode();
    }
}
