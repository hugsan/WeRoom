package com.itcom202.weroom;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itcom202.weroom.account.LoginActivity;
import com.itcom202.weroom.account.profiles.DataBasePath;
import com.itcom202.weroom.account.profiles.Profile;

import com.itcom202.weroom.swipe.SwipeActivity;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Profile userProfile;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        Log.i(TAG,"User logged in: "+ firebaseAuth.getCurrentUser());
        //if there is no user logged in Firebase it starts LoginActivity
        if (firebaseAuth.getCurrentUser() == null){
            Log.i(TAG,"We are not logged as:");
            startActivity(LoginActivity.newIntent(this));
            finish();
        }//IF there is a user logged into Firebase it starts at AccountCreationActivity
        else{
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection(DataBasePath.USERS.getValue())
                    .document(FirebaseAuth.getInstance().getUid());
            Task getUser = docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    userProfile = documentSnapshot.toObject(Profile.class);
                    ProfileSingleton.initialize(userProfile);
                }
            });
            Log.i(TAG,"I am logged in: "+ firebaseAuth.getCurrentUser().getEmail());
            getUser.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (ProfileSingleton.isFinishedProfile()){
                        startActivity(SwipeActivity.newIntent(MainActivity.this));
                        finish();
                    }else
                        startActivity(LoginActivity.newIntent(MainActivity.this));
                }
            });

        }

    }
    public static Intent newIntent(Context myContext) {
        Intent i = new Intent(myContext, SwipeActivity.class);
        return i;
    }




}

