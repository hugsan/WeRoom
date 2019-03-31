package com.itcom202.weroom;


import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.itcom202.weroom.account.authentification.LoginActivity;
import com.itcom202.weroom.account.profiles.ProfileTenantActivity;
import com.itcom202.weroom.account.profiles.Profile_Activity;
import com.itcom202.weroom.account.profiles.TestActivity;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

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
            Log.i(TAG,"We are logged"+ firebaseAuth.getCurrentUser().getEmail());
            startActivity(Profile_Activity.newIntent(this));
            finish();
        }

    }


}

