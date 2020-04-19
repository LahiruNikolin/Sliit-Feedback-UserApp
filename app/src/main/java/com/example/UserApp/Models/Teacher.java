package com.example.UserApp.Models;

public class Teacher {

    String  id;
    String email;
    String fname;
    String image;
    String lname;


    public Teacher(){}

    public Teacher(String id, String email, String fname, String image, String lname) {
        this.id = id;
        this.email = email;
        this.fname = fname;
        this.image = image;
        this.lname = lname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }
}
