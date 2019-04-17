package com.itcom202.weroom.swipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itcom202.weroom.MapFragment;
import com.itcom202.weroom.R;

import swipeable.com.layoutmanager.OnItemSwiped;
import swipeable.com.layoutmanager.SwipeableLayoutManager;
import swipeable.com.layoutmanager.SwipeableTouchHelperCallback;
import swipeable.com.layoutmanager.touchelper.ItemTouchHelper;

public class SwipeFragment extends Fragment {
    private static final String TAG = "Swipe";
    private ListAdapter adapter;
    public static Fragment thisFragment;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.swipe_activity, container, false);
        thisFragment = this;
        adapter = new ListAdapter();
        final RecyclerView recyclerView = v.findViewById(R.id.recycler_view);
        SwipeableTouchHelperCallback swipeableTouchHelperCallback =
                new SwipeableTouchHelperCallback(new OnItemSwiped() {
                    @Override
                    public void onItemSwiped() {
                        adapter.removeTopItem();


                    }

                    @Override
                    public void onItemSwipedLeft() {
                        Log.d(TAG, "LEFT");


                    }

                    @Override
                    public void onItemSwipedRight() {
                        Log.d(TAG, "RIGHT");

                    }

                    @Override
                    public void onItemSwipedUp() {
                        Log.d(TAG, "UP");

                    }

                    @Override
                    public void onItemSwipedDown() {
                        Log.d(TAG, "DOWN");
                    }
                }) {
                    @Override
                    public int getAllowedSwipeDirectionsMovementFlags(RecyclerView.ViewHolder viewHolder) {
                        return ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT ;
                    }
                };
        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeableTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(new SwipeableLayoutManager().setAngle(10)
                .setAnimationDuratuion(450)
                .setMaxShowCount(3)
                .setScaleGap(0.1f)
                .setTransYGap(0));
        recyclerView.setAdapter(adapter = new ListAdapter());

        return v;
    }
    public void goToInformationFragment(){
        final CardInfoFragment cardInfoFragment = new CardInfoFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, cardInfoFragment).commit();
    }
    @Override
    public void onResume() {
        super.onResume();

        //update whatever your list
        adapter.notifyDataSetChanged();
    }
}
