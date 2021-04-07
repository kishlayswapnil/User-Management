package com.bridgelabz.fundoonotes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

//A Database class for user information.
@Entity
public class User {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private int id;
    private String firstName;
    private String lastName;
    private String emailId;
    private String mobileNumber;
    private String password;
    private LocalDateTime registrationDate;
    private LocalDateTime modifiedDate;
    private boolean isVerified;
    // collaborator for notes
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private List<Note> collaboratedNotes;


    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String email) {
        this.emailId = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String phoneNumber) {
        this.mobileNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

    public List<Note> getCollaboratedNotes() {
        return collaboratedNotes;
    }
    public void setCollaboratedNotes(List<Note> collaboratedNotes) {
        this.collaboratedNotes = collaboratedNotes;
    }

    @Override
    public String toString() {
        return "User[ id = " + id + ", firstName = " + firstName + ", lastName = " + lastName + ", phoneNumber = "
                + mobileNumber + ", email = " + emailId + ", password = " + password + ", registrationDate = "
                + registrationDate + ", modifiedDate = " + modifiedDate + ", isVerified = " + isVerified + "]";

    }
}
