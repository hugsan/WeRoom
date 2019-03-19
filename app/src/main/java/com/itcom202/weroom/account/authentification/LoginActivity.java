package com.itcom202.weroom.account.authentification;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.itcom202.weroom.SingleFragmentActivity;

public class LoginActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new LoginFragment();
    }

    public static Intent newIntent(Context myContext){
        Intent i = new Intent(myContext, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
    }

}
