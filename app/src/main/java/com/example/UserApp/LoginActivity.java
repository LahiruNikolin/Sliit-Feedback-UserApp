package com.example.UserApp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.UserApp.Models.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    private String TAG = "sliit_bug";
    private FirebaseFirestore fireDB;

    private EditText etItnum, etPassword;
    private Button bTLogin;
    private  String itNum,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Access a Cloud Firestore instance from your Activity
        fireDB = FirebaseFirestore.getInstance();

        etItnum = (EditText) findViewById(R.id.eTitnum);
        etPassword = (EditText) findViewById(R.id.etPassword);

        bTLogin = (Button) findViewById(R.id.button);




        bTLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itNum = etItnum.getText().toString();
                password = etPassword.getText().toString();

                if(!itNum.equals("") && !password.equals("")) {
                    login(itNum, password);
                }else{
                    Toast.makeText(LoginActivity.this, "Check your inputs", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void login(String itnum, final String password){
        DocumentReference docRef = fireDB.collection("students").document(itnum);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "Document exists!");
                        Student student = document.toObject(Student.class);

                        if(student.getPassword().equals(password)){
                            Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, student.getFname() + "logged in");
                            ProfileActivity.LOGGED_IN_STUDENT=student;
                            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(LoginActivity.this, "Incorrect password!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d(TAG, "Failed to login, Please check your UserName");
                        Toast.makeText(LoginActivity.this, "Failed to login, Please check your UserName", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Log.d(TAG, "Failed with: ", task.getException());
                }
            }
        });



    }
}
