package com.example.UserApp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.UserApp.Models.Student;

public class ProfileActivity extends AppCompatActivity {

    public static Student LOGGED_IN_STUDENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }
}
