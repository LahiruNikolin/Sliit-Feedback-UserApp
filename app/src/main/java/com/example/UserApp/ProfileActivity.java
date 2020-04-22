package com.example.UserApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

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

        addNotificationListerner();

    }


    private void feedback(){
        //IF FEEDBACK ENABLED
        DocumentReference docRef = fireDB.collection("flags").document("svDqsKxgDCFqn3lJWB29");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "Document exists!");
                        String enabled = document.get("feedback").toString();

                        if(Boolean.parseBoolean(enabled)){
                            getSubjectList();
                        }else{
                            Log.d(TAG, "Failed to login, Please check your UserName");
                            Toast.makeText(ProfileActivity.this, "Feedback option is disabled at this moment", Toast.LENGTH_SHORT).show();
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
                            gotoFeedback(TeacherIds,0);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void gotoFeedback(ArrayList<String> teachers,int flag){
        Intent intent = new Intent(getApplicationContext(), TeacherListActivity.class);
        intent.putStringArrayListExtra(Constants.TEACHER_IDS, teachers);

        startActivity(intent);
    }


    public void addNotificationListerner(){


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("flags")
                .document("svDqsKxgDCFqn3lJWB29")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {


                    Intent intent= new Intent(getApplicationContext(),ProfileActivity.class);


                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            // Log.d(TAG, "Current data: " + snapshot.getData().get("feedback"));
                            if(snapshot.getData().get("feedback").equals(true)){
                                Log.d(TAG, "Current data: " +"it is true");
                                showNotification(getApplicationContext(),"Feedback Required","Please click here to give feeedbacks to lecturers",intent);
                            }


                        } else {
                            Log.d(TAG, "Current data: null");
                        }
                    }
                });

    }

    public void showNotification(Context context, String title, String body, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
    }


}
