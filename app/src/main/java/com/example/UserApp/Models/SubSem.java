package com.example.UserApp.Models;

import java.util.ArrayList;

public class SubSem {

    private ArrayList<String> subs = new ArrayList<String>();


    public SubSem() {
    }

    public SubSem(ArrayList<String> subs) {
        this.subs = subs;
    }

    public ArrayList<String> getSubs() {
        return subs;
    }

    public void setSubs(ArrayList<String> subs) {
        this.subs = subs;
    }
}
