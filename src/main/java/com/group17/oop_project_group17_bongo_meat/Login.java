package com.group17.oop_project_group17_bongo_meat;

public class Login {

    private String emailOrPhone;
    private String password;
    private String userType;


    public Login(String emailOrPhone, String password, String userType) {
        this.emailOrPhone = emailOrPhone;
        this.password = password;
        this.userType = userType;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailOrPhone() {
        return emailOrPhone;
    }

    public void setEmailOrPhone(String emailOrPhone) {
        this.emailOrPhone = emailOrPhone;
    }

    @Override
    public String toString() {
        return "Login{" +
                "emailOrPhone='" + emailOrPhone + '\'' +
                ", password='" + password + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }
}
