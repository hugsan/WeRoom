package com.itcom202.weroom.swipe;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.itcom202.weroom.ProfileSingleton;
import com.itcom202.weroom.R;
import com.itcom202.weroom.account.profiles.Profile;
import com.itcom202.weroom.account.profiles.RoomPosted;

import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import swipeable.com.layoutmanager.OnItemSwiped;
import swipeable.com.layoutmanager.SwipeableLayoutManager;
import swipeable.com.layoutmanager.SwipeableTouchHelperCallback;
import swipeable.com.layoutmanager.touchelper.ItemTouchHelper;

public class SwipeFragment extends Fragment {
    public static final String KEY_TENANT_LIST = "tenant_list";
    public static final String KEY_ROOM_LIST = "room_list";

    private static final String TAG = "Swipe";
    private ListAdapter adapter;
    public static Fragment thisFragment;
    private Spinner mPeriodRenting;

    private ArrayList<Profile> mTenantProfiles;
    private ArrayList<RoomPosted> mLandlordsRooms;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.swipe_activity, container, false);

        if (ProfileSingleton.getInstance().getRole().equals("Landlord" )&& getArguments() != null){
            mTenantProfiles = getArguments().getParcelableArrayList(KEY_TENANT_LIST);
            mLandlordsRooms = getArguments().getParcelableArrayList(KEY_ROOM_LIST);
        }

        mPeriodRenting = v.findViewById(R.id.landlordRoomSelectionSpinner);
        thisFragment = this;
        final Profile p = ProfileSingleton.getInstance();
        if (p.getRole().equals("Landlord")){
            mPeriodRenting.setAdapter(roomSpinnerAdapter());
        }else{
            mPeriodRenting.setEnabled(false);
        }


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
                        p.getMatch().addLiked(adapter.returnTopItemID());
                        //this is a room.
                        if (adapter.returnTopItemID().length() == 36 ){

                        }

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
        recyclerView.setAdapter(adapter = new ListAdapter(mTenantProfiles, mLandlordsRooms, p));

        return v;
    }



    public void goToInformationFragment(){
        final CardInfoTenantFragment cardInfoTenantFragment = new CardInfoTenantFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, cardInfoTenantFragment).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //update whatever your list
        adapter.notifyDataSetChanged();
    }

    private SpinnerAdapter roomSpinnerAdapter(){


        List<String> roomsName = new ArrayList<>();
        for (RoomPosted p : mLandlordsRooms)
            roomsName.add(p.getCompleteAddress());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item , roomsName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        return adapter;
    }
}
