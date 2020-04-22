package com.example.UserApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.UserApp.Constants.Constants;
import com.example.UserApp.Models.Feedback;
import com.example.UserApp.Models.Teacher;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class RatingActivity extends AppCompatActivity {
    private static String TAG = "sliit_bug";
    public static Teacher teacher;

    private ImageView teacherpic;
    private TextView teachername;
    private RatingBar presentation,preparation,punctuality,interaction;
    private float presentationVal,preparationVal,punctualityVal,interactionVal;

    private FirebaseFirestore fireDB;

    Button backButton, submitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        // Access a Cloud Firestore instance from your Activity
        fireDB = FirebaseFirestore.getInstance();

        teacherpic = (ImageView)findViewById(R.id.teacherPic);
        teachername = (TextView) findViewById(R.id.teacherName);

        presentation = (RatingBar) findViewById(R.id.ratingPresent);
        preparation = (RatingBar) findViewById(R.id.ratingPreparation);
        punctuality = (RatingBar) findViewById(R.id.ratingPunctuality);
        interaction = (RatingBar) findViewById(R.id.ratingInteraction);


        backButton = findViewById(R.id.buttBack);
        submitButton = findViewById(R.id.buttSubmit);


        teachername.setText(teacher.getFname() + " " + teacher.getLname() );
        Picasso.get().load(teacher.getImage()).into(teacherpic);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presentationVal = presentation.getRating();
                preparationVal = preparation.getRating();
                punctualityVal = punctuality.getRating();
                interactionVal = interaction.getRating();

                if(presentationVal!=0 && preparationVal!=0 && punctualityVal!=0 && interactionVal!=0){

                    Float totalScore = presentationVal + preparationVal + punctualityVal + interactionVal;

                    Feedback feedback = new Feedback(teacher.getId(),totalScore);
                    submitFeedback(feedback);

                }else{
                    Toast.makeText(RatingActivity.this, "Rating can not be 0", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void submitFeedback(Feedback feedback) {
        fireDB.collection("feedbacks")
                .add(feedback)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(RatingActivity.this, "Successfully submitted", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(RatingActivity.this, "Please try again.. Failed to submit", Toast.LENGTH_SHORT).show();
                    }
                });


    }
}
