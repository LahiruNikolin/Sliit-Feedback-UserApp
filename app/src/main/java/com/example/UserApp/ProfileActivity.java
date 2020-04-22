package com.example.UserApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.UserApp.Constants.Constants;
import com.example.UserApp.Models.Student;
import com.example.UserApp.Models.SubSem;
import com.example.UserApp.Models.Subject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    public static Student LOGGED_IN_STUDENT;

    private String TAG = "sliit_bug";
    private FirebaseFirestore fireDB;

    private TextView name,itno,year,sem;
    private Button but,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Access a Cloud Firestore instance from your Activity
        fireDB = FirebaseFirestore.getInstance();

        name = (TextView)findViewById(R.id.tvName);
        itno = (TextView)findViewById(R.id.tvItno);
        year = (TextView) findViewById(R.id.tvYear);
        sem = (TextView) findViewById(R.id.tvSem);
        but = (Button) findViewById(R.id.button);
        logout = (Button) findViewById(R.id.logout);

        name.setText(LOGGED_IN_STUDENT.getFname() + " " +LOGGED_IN_STUDENT.getLname());
        itno.setText(LOGGED_IN_STUDENT.getItnum());
        year.setText(LOGGED_IN_STUDENT.getYear());
        sem.setText(LOGGED_IN_STUDENT.getSemester());

        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedback();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });



    }


    private void feedback(){
        //IF FEEDBACK ENABLED
        DocumentReference docRef = fireDB.collection("flags").document("feedbacks");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "Document exists!");
                        String enabled = document.get("enabled").toString();

                        if(Boolean.parseBoolean(enabled)){
                            getSubjectList();
                        }
                    } else {
                        Log.d(TAG, "Failed to login, Please check your UserName");
                        Toast.makeText(ProfileActivity.this, "Feedback option is disabled at this moment", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG, "Failed with: ", task.getException());
                }
            }
        });

    }

    private void getSubjectList(){
        String year_sem = LOGGED_IN_STUDENT.getYear() + "_" + LOGGED_IN_STUDENT.getSemester();

        DocumentReference docRef = fireDB.collection("subsInYear").document(year_sem);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "Document exists!");
                        SubSem subsem = document.toObject(SubSem.class);
                        ArrayList<String> subs = subsem.getSubs();
                        getTeacherList(subs);
                    } else {
                        Log.d(TAG, "Failed");
                        Toast.makeText(ProfileActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG, "Failed with: ", task.getException());
                }
            }
        });

    }

    private void getTeacherList(ArrayList<String> subs){
        fireDB.collection("subjects")
                .whereIn("id", subs)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> TeacherIds = new ArrayList<String>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Subject subject = document.toObject(Subject.class);
                                TeacherIds.add(subject.getTid());
                            }
                            gotoFeedback(TeacherIds);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void gotoFeedback(ArrayList<String> teachers){
        Intent intent = new Intent(getApplicationContext(), Main3Activity.class);
        intent.putStringArrayListExtra(Constants.TEACHER_IDS, teachers);
        startActivity(intent);
    }



}
