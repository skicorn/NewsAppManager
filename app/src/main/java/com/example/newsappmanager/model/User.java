package com.example.newsappmanager.model;

import java.io.Serializable;

public class User implements Serializable {
    private String Email;
    private String Password;
    private String UID;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }


    public User(String email, String password,String UID) {
        Email = email;
        Password = password;
        this.UID = UID;
    }

    public User(){}

}
