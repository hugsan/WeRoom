package com.itcom202.weroom.swipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.itcom202.weroom.R;
import com.itcom202.weroom.SingleFragmentActivity;
import com.itcom202.weroom.account.profiles.Profile_Activity;

import swipeable.com.layoutmanager.OnItemSwiped;
import swipeable.com.layoutmanager.SwipeableLayoutManager;
import swipeable.com.layoutmanager.SwipeableTouchHelperCallback;
import swipeable.com.layoutmanager.touchelper.ItemTouchHelper;

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
