package com.group17.oop_project_group17_bongo_meat.fahim.Admin;

import java.io.Serializable;
public class UserManagement implements Serializable {
    private String userId;
    private String userName;
    private String userRole;
    private String userStatus;
    private String password;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserManagement(String userId, String userName, String userRole, String userStatus, String password) {
        this.userId = userId;
        this.userName = userName;
        this.userRole = userRole;
        this.userStatus = userStatus;
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserManagement{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userRole='" + userRole + '\'' +
                ", userStatus='" + userStatus + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
