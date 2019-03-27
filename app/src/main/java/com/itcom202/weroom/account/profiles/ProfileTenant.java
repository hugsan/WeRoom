package com.itcom202.weroom.account.profiles;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.itcom202.weroom.R;

public class ProfileTenant extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_tenant);
        TenantProfile christian = new TenantProfile.Builder(FirebaseAuth.getInstance().getUid())
                .hasLaundry(false)
                .withCity("Barcelona")
                .withRentRange(400,1)
                .withNationallity("Congo republic")
                .isFurnished(true)
                .distanceFromCenter(30)
                .build();
    }
}
