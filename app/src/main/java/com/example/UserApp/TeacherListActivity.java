package com.example.UserApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.UserApp.Adapters.TeacherAdapter;
import com.example.UserApp.Models.Teacher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TeacherListActivity extends AppCompatActivity {

    private static String TAG = "sliit_bug";

    private Activity mActivity;
    private RecyclerView rvTeachers;
    private TeacherAdapter mAdapter;
    private ArrayList<Teacher> teachersList;

    private FirebaseFirestore fireDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);

        mActivity = TeacherListActivity.this;
        rvTeachers = findViewById(R.id.teacherList);
        teachersList = new ArrayList<Teacher>();

        // Access a Cloud Firestore instance from your Activity
        fireDB = FirebaseFirestore.getInstance();

        ArrayList<String> ids = new ArrayList<String>();
        ids.add("01zUdjxGo43Eszlbdmxj");
        ids.add("2aaSnukfyAQY0SynPEuY");
        ids.add("3m8RUg5MrZJ0kxtdZ7VJ");
        getTeacherList(ids);

        mAdapter = new TeacherAdapter(mActivity,teachersList);
        mAdapter.setItemClickListener(new TeacherAdapter.ListItemClickListener() {
            @Override
            public void onItemClick(final int position, View view) {

                switch (view.getId()) {
                    case R.id.btTeacherRate:
                        rateTeacher(teachersList.get(position).getId());
                        break;

                }
            }
        });

    }

    private void rateTeacher(String id) {
        Log.d(TAG,id);

    }

    private void getTeacherList(ArrayList<String> TeacherIds){
        Log.d(TAG,"req");
        teachersList.clear();
        fireDB.collection("teachers")
                .whereIn("id", TeacherIds)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Teacher teacher = document.toObject(Teacher.class);
                                teachersList.add(teacher);
                                rvTeachers.setLayoutManager(new LinearLayoutManager(mActivity));
                                rvTeachers.setAdapter(mAdapter);
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }
}
