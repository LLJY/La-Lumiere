package com.lucas.lalumire.Models;

public class User {
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

    public String uid;
    public String Name;
    public String Email;

    public User(String uid, String name, String email) {
        this.uid = uid;
        Name = name;
        Email = email;
    }
}
