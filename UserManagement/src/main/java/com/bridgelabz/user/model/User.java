package com.bridgelabz.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

//A Database class for user information.
@Entity
@Table(name = "users")
@Data
public class User {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "user_id", updatable = false)
    private int id;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String country;
    private long mobileNumber;
    private String emailId;
    private String address;
    private String userName;
    private String password;
    private String userRole;
    private String profilePic;
    private LocalDateTime registrationDate;
    private LocalDateTime modifiedDate;
    private boolean isVerified;
    private boolean Logout;

    @JsonIgnore
    @OneToOne(mappedBy = "users")
    private Permissions permissions;
}
