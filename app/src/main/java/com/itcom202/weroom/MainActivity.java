package com.itcom202.weroom;

import android.content.SharedPreferences;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;


import android.os.Bundle;


import com.google.firebase.auth.FirebaseAuth;



public class MainActivity extends SingleFragmentActivity {

    SharedPreferences mSettings;
    SharedPreferences.Editor mPrefEditor;

    @Override
    protected Fragment createFragment() {
        return new SingupFragment();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }
}

