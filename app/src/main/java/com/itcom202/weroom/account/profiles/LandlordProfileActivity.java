package com.itcom202.weroom.account.profiles;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.itcom202.weroom.SingleFragmentActivity;

public class LandlordProfileActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new LandlordProfileFragment();
    }

    public static Intent newIntent(Context myContext){
        Intent i = new Intent(myContext, LandlordProfileActivity.class);
        return i;
    }
}
