package com.example.chatapp;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MyApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("logged_in_user_cred").child(firebaseUser.getUid());

            Map<String, Object> data = new HashMap<>();
            data.put("is_online", true);
            databaseReference.updateChildren(data);

        }

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("logged_in_user_cred").child(firebaseUser.getUid());
            Map<String, Object> data = new HashMap<>();
            data.put("is_online", false);
            databaseReference.updateChildren(data);

        }
    }
}
