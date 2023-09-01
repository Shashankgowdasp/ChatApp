package com.example.chatapp;

public class HelperClassForAuth {

    String mail,password;

    public HelperClassForAuth(String mail, String password) {
        this.mail = mail;
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public HelperClassForAuth() {
    }
}
