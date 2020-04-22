package com.example.UserApp.Models;

public class Student {

    String itnum;
    String fname;
    String lname;
    String password;
    String year;
    String semester;

    public Student(){

    }

    public Student(String itnum, String fname, String lname, String password, String year, String semester) {
        this.itnum = itnum;
        this.fname = fname;
        this.lname = lname;
        this.password = password;
        this.year = year;
        this.semester = semester;
    }

    public String getItnum() {
        return itnum;
    }

    public void setItnum(String itnum) {
        this.itnum = itnum;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getYearSem(){
        return  this.year+"_"+this.semester;
    }
}
