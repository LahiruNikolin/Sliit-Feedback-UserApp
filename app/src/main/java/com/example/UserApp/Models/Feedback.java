package com.example.UserApp.Models;

public class Feedback {

    private String teacher_id;
    private Float score;

    public Feedback(){

    }

    public Feedback(String teacher_id, Float score) {
        this.teacher_id = teacher_id;
        this.score = score;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }
}
