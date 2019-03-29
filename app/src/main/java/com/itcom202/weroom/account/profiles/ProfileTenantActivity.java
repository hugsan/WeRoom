package com.itcom202.weroom.account.profiles;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.itcom202.weroom.R;
import com.itcom202.weroom.SingleFragmentActivity;

public class ProfileTenantActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ProfileTenantFragment();
    }

    public static Intent newIntent(Context myContext){
        Intent i = new Intent(myContext, ProfileTenantActivity.class);
        return i;
    }
}
