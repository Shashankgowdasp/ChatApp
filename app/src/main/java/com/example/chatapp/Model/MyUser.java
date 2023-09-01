package com.example.chatapp.Model;

public class MyUser {
    private String id;
    private String myusername;
    private String profileImageResource;

    public MyUser() {
    }

    @Override
    public String toString() {
        return "MyUser{" +
                "id='" + id + '\'' +
                ", myusername='" + myusername + '\'' +
                ", profileImageResource='" + profileImageResource + '\'' +
                '}';
    }

    public MyUser(String id, String myusername, String profileImageResource) {
        this.id=id;
        this.myusername = myusername;
        this.profileImageResource = profileImageResource;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getProfileImage() {
        return profileImageResource;
    }

    public void setProfileImage(String profileImage) {
        this.profileImageResource = profileImage;
    }

    public String getMyusername() {
        return myusername;
    }

    public void setMyusername(String myusername) {
        this.myusername = myusername;
    }

    public String getProfileImageResource() {
        return profileImageResource;
    }

    public void setProfileImageResource(String profileImageResource) {
        this.profileImageResource = profileImageResource;
    }
}

