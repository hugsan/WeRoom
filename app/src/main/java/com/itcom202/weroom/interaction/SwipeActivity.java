package com.itcom202.weroom.interaction;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.itcom202.weroom.interaction.profile.views.EditRoomsFragment;
import com.itcom202.weroom.interaction.swipe.controllers.FilterController;
import com.itcom202.weroom.interaction.profile.views.ProfileInfoFragment;
import com.itcom202.weroom.interaction.profile.views.SettingFragment;
import com.itcom202.weroom.interaction.swipe.views.SwipeFragment;
import com.itcom202.weroom.services.NotificationService;
import com.itcom202.weroom.framework.ProfileSingleton;
import com.itcom202.weroom.R;
import com.itcom202.weroom.framework.DataBasePath;
import com.itcom202.weroom.account.onboarding.views.flow.LandlordProfileFragment;
import com.itcom202.weroom.account.models.Match;
import com.itcom202.weroom.account.models.Profile;
import com.itcom202.weroom.account.onboarding.views.flow.ProfileFragment;
import com.itcom202.weroom.account.onboarding.views.flow.ProfileTenantFragment;
import com.itcom202.weroom.account.models.RoomPosted;
import com.itcom202.weroom.interaction.chat.views.SelectChatFragment;

import java.util.ArrayList;
import java.util.List;


public class SwipeActivity extends AppCompatActivity {
    private final String TAG = "SwipeActivity";
    private final String MY_PREFS_NAME = "settings_preferences";
    private final String NOTIFICATION_VALUE = "notification";
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
        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Menu bottomNavigationViewMenu = bottomNavigationView.getMenu();
        bottomNavigationViewMenu.findItem(R.id.action_profile).setChecked(false);
        mActiveBottomNavigationViewMenuItem = bottomNavigationViewMenu.findItem(R.id.action_home).setChecked(true);




        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                FragmentManager fm = getSupportFragmentManager();


                switch (item.getItemId()) {

                    case R.id.action_profile:
                        changeToPorifleFragment();
                        break;
                    case R.id.action_home:
                        fm.beginTransaction()
                                .replace(R.id.fragment_container_top, swipingFragment)
                                .commit();
                        break;
                    case R.id.action_chat:
                        mAllProfilesFromQuery.removeAll(mNonTenantProfiles);
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList(SelectChatFragment.KEY_ROOM_LANDLORD, mLandlordsRooms);
                        Fragment fragment = new SelectChatFragment();
                        fragment.setArguments(bundle);
                        fm.beginTransaction()
                                 .replace(R.id.fragment_container_top, fragment)
                                 .commit();
                        break;


                }
                if (item != mActiveBottomNavigationViewMenuItem){
                    mActiveBottomNavigationViewMenuItem.setChecked(false);
                    mActiveBottomNavigationViewMenuItem = item;
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

            Task t1 = getLandlordsRooms.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (DocumentSnapshot d : queryDocumentSnapshots) {
                        mAllPostedRooms.add(d.toObject(RoomPosted.class));
                        //startFragmentFromTenant();
                    }
                }
            });
            Tasks.whenAllSuccess(t1).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                @Override
                public void onSuccess(List<Object> list) {
                    startFragmentFromTenant();
                }
            });
            /*t1.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    startFragmentFromTenant();
                }
            });*/
        }

    }
    private MenuItem mActiveBottomNavigationViewMenuItem;

    private void startFragmentFromTenant(){
        startNotificationService();

        Bundle bundle = new Bundle();
        ArrayList<RoomPosted> filteredRooms = FilterController.filterRoomsFromTenant(ProfileSingleton.getInstance(),mAllPostedRooms);
        bundle.putParcelableArrayList(SwipeFragment.KEY_ROOM_LIST_ALL, filteredRooms);

        FragmentManager fm = getSupportFragmentManager();

        if (swipingFragment == null) {
            swipingFragment = new SwipeFragment();
        }
            swipingFragment.setArguments(bundle);
            fm.beginTransaction()
                    .add(R.id.fragment_container_top, swipingFragment)
                    .commit();

    }
    private void startFragmentFromLandlord(){
        startNotificationService();
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
                    .add(R.id.fragment_container_top, swipingFragment)
                    .commit();

    }
    public void changeToProfileEditFragment(){
        FragmentManager fm = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ProfileFragment.KEY_IS_EDIT,true);
        Fragment fragment = new ProfileFragment();
        fragment.setArguments(bundle);
        fm.beginTransaction()
                .replace(R.id.fragment_container_top, fragment)
                .commit();
    }
    public void changeToSettingFragment(){
        FragmentManager fm = getSupportFragmentManager();

        fm.beginTransaction()
                .replace(R.id.fragment_container_top, new SettingFragment())
                .commit();
    }
    public void changeToPorifleFragment(){
        FragmentManager fm = getSupportFragmentManager();

        fm.beginTransaction()
                .replace(R.id.fragment_container_top, new ProfileInfoFragment())
                .commit();
    }
    public void changeToTenantEditFragment(){
        FragmentManager fm = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ProfileTenantFragment.KEY_INITIALIZE,true);
        Fragment fragment = new ProfileTenantFragment();
        fragment.setArguments(bundle);
        fm.beginTransaction()
                .replace(R.id.fragment_container_top, fragment)
                .commit();
    }
    public void changeToLandlordEditFragment(){
        FragmentManager fm = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putBoolean(LandlordProfileFragment.KET_INITIALIZE,true);
        Fragment fragment = new LandlordProfileFragment();
        fragment.setArguments(bundle);
        fm.beginTransaction()
                .replace(R.id.fragment_container_top, fragment)
                .commit();
    }
    public void changeToRoomEditing(){
        FragmentManager fm = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(EditRoomsFragment.KEY_ROOMS,mLandlordsRooms);
        Fragment fragment = new EditRoomsFragment();
        fragment.setArguments(bundle);
        fm.beginTransaction()
                .replace(R.id.fragment_container_top, fragment)
                .commit();
    }

    public void addLandlordRoom(RoomPosted rooms){
        if (mLandlordsRooms.contains(rooms)){
            int position = mLandlordsRooms.indexOf(rooms);
            mLandlordsRooms.set(position,rooms);
        }else{
            mLandlordsRooms.add(rooms);
        }
    }
    public void removeLandlordRoom(RoomPosted room){
        mLandlordsRooms.remove(room);
    }
    public void startNotificationService(){
        if (getBatteryLevel() > 10 && getNotificationOption()){
            ArrayList<Match> matches = new ArrayList<>();
            ArrayList<String> ids = new ArrayList<>();
            Profile p = ProfileSingleton.getInstance();
            if (p.getRole().equals("Landlord")){
                for (RoomPosted r : mLandlordsRooms){
                    matches.add(r.getMatch());
                    ids.add(r.getRoomID());
                }
            }else{
                matches.add(p.getMatch());
                ids.add(p.getUserID());
            }
            startService(NotificationService.getIntent(this,matches,ids));
        }
    }
    public int getBatteryLevel(){
        Intent BATTERYintent = this.registerReceiver(null, new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED));
        int level = BATTERYintent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        Log.v(TAG, "LEVEL" + level);
        return level;
    }
    public void changeNotificationOption(boolean option){
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(NOTIFICATION_VALUE,option);
        editor.apply();
    }
    public boolean getNotificationOption(){
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        return prefs.getBoolean(NOTIFICATION_VALUE,true);
    }

}


