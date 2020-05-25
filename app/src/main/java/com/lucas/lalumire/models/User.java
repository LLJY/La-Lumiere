package com.lucas.lalumire.models;

public class User {
    public String uid;
    public String Name;
    public String Email;
    public UserType userType;

    public User(String uid, String name, String email, UserType userType) {
        this.uid = uid;
        Name = name;
        Email = email;
        this.userType = userType;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }




}
