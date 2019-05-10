package com.itcom202.weroom;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itcom202.weroom.account.AuthenticationAndOnBoardingActivity;
import com.itcom202.weroom.framework.DataBasePath;
import com.itcom202.weroom.framework.ProfileSingleton;
import com.itcom202.weroom.account.models.Profile;

import com.itcom202.weroom.interaction.SwipeActivity;




public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Profile userProfile;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        Log.i(TAG,"User logged in: "+ firebaseAuth.getCurrentUser());
        //if there is no user logged in Firebase it starts AuthenticationAndOnBoardingActivity
        if (firebaseAuth.getCurrentUser() == null){
            Log.i(TAG,"We are not logged as:");
            startActivity(AuthenticationAndOnBoardingActivity.newIntent(this));
            finish();
        }//IF there is a user logged into Firebase it starts at AccountCreationActivity
        else{
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection(DataBasePath.USERS.getValue())
                    .document(FirebaseAuth.getInstance().getUid());
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    userProfile = documentSnapshot.toObject(Profile.class);
                    ProfileSingleton.initialize(userProfile);
                    if (ProfileSingleton.isFinishedProfile()){
                        startActivity(SwipeActivity.newIntent(MainActivity.this));
                        finish();
                    }else
                        startActivity(AuthenticationAndOnBoardingActivity.newIntent(MainActivity.this));
                }
            });
        }

    }
    public static Intent newIntent(Context myContext) {
        Intent i = new Intent(myContext, MainActivity.class);
        return i;
    }




}

