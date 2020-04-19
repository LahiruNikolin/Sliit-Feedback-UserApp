package com.example.UserApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RatingActivity extends AppCompatActivity {

    Button secondbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        //this onclick is used to move to the Main3Activity
        secondbutton = findViewById(R.id.button5);
        secondbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(RatingActivity.this, TeacherListActivity.class);
                startActivity(intent2);
            }
        });
    }
}
