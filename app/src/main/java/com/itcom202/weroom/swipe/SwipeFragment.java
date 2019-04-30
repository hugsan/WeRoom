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

import com.google.firebase.firestore.FirebaseFirestore;
import com.itcom202.weroom.ProfileSingleton;
import com.itcom202.weroom.R;
import com.itcom202.weroom.account.profiles.DataBasePath;
import com.itcom202.weroom.account.profiles.Profile;
import com.itcom202.weroom.account.profiles.RoomPosted;

import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import swipeable.com.layoutmanager.OnItemSwiped;
import swipeable.com.layoutmanager.SwipeableLayoutManager;
import swipeable.com.layoutmanager.SwipeableTouchHelperCallback;
import swipeable.com.layoutmanager.touchelper.ItemTouchHelper;

public class SwipeFragment extends Fragment {
    public static final String KEY_TENANT_LIST = "tenant_list";
    public static final String KEY_ROOM_LIST_LANDLORD = "room_list_landlord";
    public static final String KEY_ROOM_LIST_ALL = "room_list_all";

    private static final String TAG = "Swipe";
    private ListAdapter adapter;
    public static Fragment thisFragment;
    private Spinner mChosenRoomSpinner;
    private ArrayList<Profile> mTenantProfiles;
    private ArrayList<RoomPosted> mLandlordsRooms;
    private ArrayList<RoomPosted> mAllRooms;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_swipe, container, false);

        if (ProfileSingleton.getInstance().getRole().equals("Landlord" )&& getArguments() != null){
            mTenantProfiles = getArguments().getParcelableArrayList(KEY_TENANT_LIST);
            mLandlordsRooms = getArguments().getParcelableArrayList(KEY_ROOM_LIST_LANDLORD);
        }
        if (ProfileSingleton.getInstance().getRole().equals("Tenant") && getArguments() != null){
            mAllRooms = getArguments().getParcelableArrayList(KEY_ROOM_LIST_ALL);
        }

        mChosenRoomSpinner = v.findViewById(R.id.landlordRoomSelectionSpinner);
        thisFragment = this;
        final Profile p = ProfileSingleton.getInstance();
        if (p.getRole().equals("Landlord")){
            mChosenRoomSpinner.setAdapter(roomSpinnerAdapter());
        }else{
            mChosenRoomSpinner.setVisibility(View.GONE);
        }


        final RecyclerView recyclerView = v.findViewById(R.id.recycler_view);
        SwipeableTouchHelperCallback swipeableTouchHelperCallback =
                new SwipeableTouchHelperCallback(new OnItemSwiped() {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();


                    @Override
                    public void onItemSwiped() {

                    }

                    @Override
                    public void onItemSwipedLeft() {
                        Log.d(TAG, "LEFT");
                        //this when we swipe a room.
                        if (adapter.returnTopItemID().length() == 36 ){
                            p.getMatch().addDislike(adapter.returnTopItemID());
                            ProfileSingleton.update(p);
                        }//this when we swipe a tenant.
                        else{
                            RoomPosted r = mLandlordsRooms.get(mChosenRoomSpinner.getSelectedItemPosition());
                            r.getMatch().addDislike(adapter.returnTopItemID());
                            db.collection(DataBasePath.ROOMS.getValue())
                                    .document(r.getRoomID())
                                    .set(r);
                            Profile p = adapter.returnTopTenant();
                            p.getMatch().addExternalLikes(r.getRoomID());
                            db.collection(DataBasePath.USERS.getValue())
                                    .document(p.getUserID())
                                    .set(p);
                        }
                        adapter.removeTopItem();
                    }

                    @Override
                    public void onItemSwipedRight() {
                        Log.d(TAG, "RIGHT");
                        //this is when we swipe a room.
                        if (adapter.returnTopItemID().length() == 36 ){
                            p.getMatch().addLiked(adapter.returnTopItemID());
                            ProfileSingleton.update(p);

                            RoomPosted room = adapter.returnTopRoom();

                            room.getMatch().addExternalLikes(p.getUserID());

                            db.collection(DataBasePath.ROOMS.getValue())
                                    .document(room.getRoomID())
                                    .set(room);
                        }//this when we swipe a tenant.
                        else{
                            int position = mChosenRoomSpinner.getFirstVisiblePosition();
                           RoomPosted r = mLandlordsRooms.get(position);
                           r.getMatch().addLiked(adapter.returnTopItemID());
                            db.collection(DataBasePath.ROOMS.getValue())
                                    .document(r.getRoomID())
                                    .set(r);
                            Profile p = adapter.returnTopTenant();
                            p.getMatch().addExternalLikes(r.getRoomID());
                            db.collection(DataBasePath.USERS.getValue())
                                    .document(p.getUserID())
                                    .set(p);
                        }
                        adapter.removeTopItem();
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
        recyclerView.setAdapter(adapter = new ListAdapter(mTenantProfiles, mLandlordsRooms,mAllRooms, p));

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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.spinner_item , roomsName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        return adapter;
    }
}
