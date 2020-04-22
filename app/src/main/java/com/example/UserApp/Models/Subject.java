package com.example.UserApp.Models;

public class Subject {

    private String id;
    private String name;
    private String semester;
    private String tid;
    private String year;

    private Subject(){}

    public Subject(String id, String name, String semester, String tid, String year) {
        this.id = id;
        this.name = name;
        this.semester = semester;
        this.tid = tid;
        this.year = year;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
