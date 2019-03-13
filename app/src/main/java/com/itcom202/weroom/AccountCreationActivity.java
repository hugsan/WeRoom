package com.itcom202.weroom;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;


public class AccountCreationActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new AccountCreationFragment();
    }
    public static Intent newIntent(Context myContext){
        Intent i = new Intent(myContext, AccountCreationActivity.class);
        return i;
    }
}
