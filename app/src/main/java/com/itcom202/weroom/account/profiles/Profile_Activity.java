package com.itcom202.weroom.account.profiles;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.itcom202.weroom.SingleFragmentActivity;
import com.itcom202.weroom.account.authentification.LoginActivity;

public class Profile_Activity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ProfileFragment();
    }

    public static Intent newIntent(Context myContext){
        Intent i = new Intent(myContext, Profile_Activity.class);
        return i;
    }
}
