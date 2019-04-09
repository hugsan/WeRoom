package com.itcom202.weroom.swipe;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.itcom202.weroom.SingleFragmentActivity;



public class SwipeActivity extends SingleFragmentActivity {


    public static Intent newIntent(Context myContext){
        Intent i = new Intent(myContext, SwipeActivity.class);
        return i;
    }

    @Override
    protected Fragment createFragment() {
        return new SwipeFragment();
    }
}
