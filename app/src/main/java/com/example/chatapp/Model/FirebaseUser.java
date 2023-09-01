package com.example.chatapp.Model;

//DataSnapshot { key = 2023-08-17 10:08:54_Sj29AIX1g6O7qAVoX2WXVqAqLw92, value = {password=bravo333, mail=bravo@gmail.com, username=DJ Bravo} }
public class FirebaseUser {
    private String password;
    private String  mail;
    private String  username;
    private String  id;

    private Boolean isOnline;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    private String uuid;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public FirebaseUser() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUsername() {
        if(username == null) {
            return "";
        }
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public Boolean getOnline() {
        if(isOnline != null && isOnline) {
            return true;
        }
        else {
            return false;
        }
    }

    public void setOnline(Boolean online) {
        isOnline = online;
    }

    @Override
    public String toString() {
        return "FirebaseUser{" +
                "password='" + password + '\'' +
                ", mail='" + mail + '\'' +
                ", username='" + username + '\'' +
                ", id='" + id + '\'' +
                ", isOnline=" + isOnline +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}

