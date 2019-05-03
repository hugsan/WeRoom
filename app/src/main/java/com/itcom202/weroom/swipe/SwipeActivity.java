package com.itcom202.weroom.swipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Spinner;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.itcom202.weroom.ProfileSingleton;
import com.itcom202.weroom.R;
import com.itcom202.weroom.account.edit.EditProfileFragment;
import com.itcom202.weroom.account.profiles.DataBasePath;
import com.itcom202.weroom.account.profiles.Profile;
import com.itcom202.weroom.account.profiles.ProfileFragment;
import com.itcom202.weroom.account.profiles.RoomPosted;
import com.itcom202.weroom.chat.SelectChatFragment;

import java.util.ArrayList;
import java.util.List;


public class SwipeActivity extends AppCompatActivity {
    private ArrayList<Profile> mAllProfilesFromQuery = new ArrayList<>();
    private ArrayList<Profile> mNonTenantProfiles = new ArrayList<>();
    private ArrayList<RoomPosted> mLandlordsRooms = new ArrayList<>();
    private ArrayList<RoomPosted> mAllPostedRooms = new ArrayList<>();
    private Fragment swipingFragment;
    private Spinner mRoomSpinner;
    public static Intent newIntent(Context myContext) {
        Intent i = new Intent(myContext, SwipeActivity.class);
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onefragment_navigationbar);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fm = getSupportFragmentManager();
                switch (item.getItemId()) {
                    case R.id.action_profile:
                        fm.beginTransaction()
                                .replace(R.id.fragment_container, new ProfileInfoFragment())
                                .commit();
                        break;
                    case R.id.action_home:
                        fm.beginTransaction()
                                .replace(R.id.fragment_container, swipingFragment)
                                .commit();
                        break;
                    case R.id.action_chat:
                        mAllProfilesFromQuery.removeAll(mNonTenantProfiles);
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList(SelectChatFragment.KEY_ROOM_LANDLORD, mLandlordsRooms);
                        Fragment fragment = new SelectChatFragment();
                        fragment.setArguments(bundle);
                        fm.beginTransaction()
                                 .replace(R.id.fragment_container, fragment)
                                 .commit();
                        break;
                }
                return true;
            }
        });

        Profile p = ProfileSingleton.getInstance();
        if (p.getRole().equals("Landlord")) {

            Query tenantCandidateQuery = FirebaseFirestore.getInstance()
                    .collection(DataBasePath.USERS.getValue());

            Query removeNonTenant = FirebaseFirestore.getInstance()
                    .collection(DataBasePath.USERS.getValue())
                    .whereEqualTo("tenant", null);

            Query getLandlordsRooms = FirebaseFirestore.getInstance()
                    .collection(DataBasePath.ROOMS.getValue())
                    .whereEqualTo("landlordID",p.getUserID() );

            Task t1 = tenantCandidateQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (DocumentSnapshot d : queryDocumentSnapshots){
                        mAllProfilesFromQuery.add(d.toObject(Profile.class));
                    }
                }
            });
            Task t2 = removeNonTenant.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (DocumentSnapshot d : queryDocumentSnapshots) {
                        mNonTenantProfiles.add(d.toObject(Profile.class));
                    }
                }
            });

            Task t3 = getLandlordsRooms.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (DocumentSnapshot d : queryDocumentSnapshots) {
                        mLandlordsRooms.add(d.toObject(RoomPosted.class));
                    }
                }
            });

            Tasks.whenAllSuccess(t1, t2, t3).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                @Override
                public void onSuccess(List<Object> list) {
                    startFragmentFromLandlord();
                }
            });


        } else {
            Query getLandlordsRooms = FirebaseFirestore.getInstance()
                    .collection(DataBasePath.ROOMS.getValue());

            getLandlordsRooms.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (DocumentSnapshot d : queryDocumentSnapshots) {
                        mAllPostedRooms.add(d.toObject(RoomPosted.class));
                    }
                    startFragmentFromTenant();
                }
            });
        }


    }

    private void startFragmentFromTenant(){
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(SwipeFragment.KEY_ROOM_LIST_ALL, mAllPostedRooms);

        FragmentManager fm = getSupportFragmentManager();

        if (swipingFragment == null) {
            swipingFragment = new SwipeFragment();
        }
            swipingFragment.setArguments(bundle);
            fm.beginTransaction()
                    .add(R.id.fragment_container, swipingFragment)
                    .commit();

    }
    private void startFragmentFromLandlord(){
        mAllProfilesFromQuery.removeAll(mNonTenantProfiles);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(SwipeFragment.KEY_TENANT_LIST, mAllProfilesFromQuery);
        bundle.putParcelableArrayList(SwipeFragment.KEY_ROOM_LIST_LANDLORD, mLandlordsRooms);


        FragmentManager fm = getSupportFragmentManager();

        if (swipingFragment == null) {
            swipingFragment = new SwipeFragment();
        }
            swipingFragment.setArguments(bundle);
            fm.beginTransaction()
                    .add(R.id.fragment_container, swipingFragment)
                    .commit();

    }
    public void changeToProfileEditFragment(){
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.fragment_container, new EditProfileFragment())
                .commit();
    }
    public void changeToSettingFragment(){

    }

}


