package com.bridgelabz.user.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Permissions {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private boolean addDashboard = false;
    private boolean deleteDashboard = false;
    private boolean modifyDashboard = false;
    private boolean readDashboard = false;
    private boolean addSetting = false;
    private boolean deleteSetting = false;
    private boolean modifySetting = false;
    private boolean readSetting = false;
    private boolean addUserInformation = false;
    private boolean deleteUserInformation = false;
    private boolean modifyUserInformation = false;
    private boolean readUserInformation = false;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User users;
}
