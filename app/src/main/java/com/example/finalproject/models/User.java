package com.example.finalproject.models;

public class User {

    private String password;
    private String name;
    private String photo;
    private String email;
    private String phone;

    public User() {

    }

    public User(String email, String password) {
        this.password = password;
        this.name = "";
        this.photo = "";
        this.email = email;
        this.phone = "";
    }

    public User(String login, String password, String name, String photo, String email, String phone) {
        this.password = password;
        this.name = name;
        this.photo = photo;
        this.email = email;
        this.phone = phone;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}