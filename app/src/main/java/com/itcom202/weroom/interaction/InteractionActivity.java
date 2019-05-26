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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.itcom202.weroom.R;
import com.itcom202.weroom.account.models.Match;
import com.itcom202.weroom.account.models.Profile;
import com.itcom202.weroom.account.models.RoomPosted;
import com.itcom202.weroom.account.onboarding.views.flow.LandlordProfileFragment;
import com.itcom202.weroom.account.onboarding.views.flow.ProfileFragment;
import com.itcom202.weroom.account.onboarding.views.flow.ProfileTenantFragment;
import com.itcom202.weroom.framework.DataBasePath;
import com.itcom202.weroom.framework.PopUpExit;
import com.itcom202.weroom.framework.ProfileSingleton;
import com.itcom202.weroom.interaction.chat.views.SelectChatFragment;
import com.itcom202.weroom.interaction.profile.views.EditRoomsFragment;
import com.itcom202.weroom.interaction.profile.views.ProfileInfoFragment;
import com.itcom202.weroom.interaction.profile.views.SettingFragment;
import com.itcom202.weroom.interaction.swipe.controllers.profilefiltering.FilterController;
import com.itcom202.weroom.interaction.swipe.views.SwipeFragment;
import com.itcom202.weroom.services.NotificationService;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity that manage all the fragment used in the interaction package.
 * <p>
 * Also get all the values needed from the databases before starting the corresponding fragment.
 * <p>
 * Implements Navigation bar
 * <p>
 * Starts the Notification service if the requirements are met.
 */
public class InteractionActivity extends AppCompatActivity {
    private final String TAG = "InteractionActivity";
    private final String MY_PREFS_NAME = "settings_preferences";
    private final String NOTIFICATION_VALUE = "notification";
    private ArrayList<Profile> mAllProfilesFromQuery = new ArrayList<>( );
    private ArrayList<Profile> mNonTenantProfiles = new ArrayList<>( );
    private ArrayList<RoomPosted> mLandlordsRooms = new ArrayList<>( );
    private ArrayList<RoomPosted> mAllPostedRooms = new ArrayList<>( );
    private Fragment swipingFragment;
    private PopUpExit mExitPopUp = new PopUpExit( );
    private MenuItem mActiveBottomNavigationViewMenuItem;
    private long nanoStart;


    public static Intent newIntent( Context myContext ) {
        return new Intent( myContext, InteractionActivity.class );
    }

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        createNavigationBar( );

        fetchDataFromDatabaseAndStartSwipeFragment( );

    }

    /**
     * Start SwipeFragment from Tenant view
     */
    private void startFragmentFromTenant( ) {
        startNotificationService( );

        Bundle bundle = new Bundle( );
        ArrayList<RoomPosted> filteredRooms = FilterController.filterRoomsFromTenant( ProfileSingleton.getInstance( ), mAllPostedRooms );
        bundle.putParcelableArrayList( SwipeFragment.KEY_ROOM_LIST_ALL, filteredRooms );
        bundle.putLong(SwipeFragment.NANO_TIME_START, nanoStart);

        FragmentManager fm = getSupportFragmentManager( );

        if ( swipingFragment == null ) {
            swipingFragment = new SwipeFragment( );
        }
        swipingFragment.setArguments( bundle );
        fm.beginTransaction( )
                .add( R.id.fragment_container_top, swipingFragment )
                .commit( );

    }

    /**
     * Start SwipeFragment from Landlord view
     */
    private void startFragmentFromLandlord( ) {
        startNotificationService( );
        mAllProfilesFromQuery.removeAll( mNonTenantProfiles );
        Bundle bundle = new Bundle( );
        ArrayList<Profile> filteredTenants = FilterController.filterProfilesFromLandlord( ProfileSingleton.getInstance( ), mAllProfilesFromQuery );
        bundle.putParcelableArrayList( SwipeFragment.KEY_TENANT_LIST, filteredTenants );
        bundle.putParcelableArrayList( SwipeFragment.KEY_ROOM_LIST_LANDLORD, mLandlordsRooms );
        bundle.putLong(SwipeFragment.NANO_TIME_START, nanoStart);


        FragmentManager fm = getSupportFragmentManager( );

        if ( swipingFragment == null ) {
            swipingFragment = new SwipeFragment( );
        }
        swipingFragment.setArguments( bundle );
        fm.beginTransaction( )
                .add( R.id.fragment_container_top, swipingFragment )
                .commit( );

    }

    public void changeToProfileEditFragment( ) {
        FragmentManager fm = getSupportFragmentManager( );
        Bundle bundle = new Bundle( );
        bundle.putBoolean( ProfileFragment.KEY_IS_EDIT, true );
        Fragment fragment = new ProfileFragment( );
        fragment.setArguments( bundle );
        fm.beginTransaction( )
                .replace( R.id.fragment_container_top, fragment )
                .commit( );
    }

    public void changeToSettingFragment( ) {
        FragmentManager fm = getSupportFragmentManager( );

        fm.beginTransaction( )
                .replace( R.id.fragment_container_top, new SettingFragment( ) )
                .commit( );
    }

    public void changeToPorifleFragment( ) {
        FragmentManager fm = getSupportFragmentManager( );

        fm.beginTransaction( )
                .replace( R.id.fragment_container_top, new ProfileInfoFragment( ) )
                .commit( );
    }

    public void changeToTenantEditFragment( ) {
        FragmentManager fm = getSupportFragmentManager( );
        Bundle bundle = new Bundle( );
        bundle.putBoolean( ProfileTenantFragment.KEY_INITIALIZE, true );
        Fragment fragment = new ProfileTenantFragment( );
        fragment.setArguments( bundle );
        fm.beginTransaction( )
                .replace( R.id.fragment_container_top, fragment )
                .commit( );
    }

    public void changeToLandlordEditFragment( ) {
        FragmentManager fm = getSupportFragmentManager( );
        Bundle bundle = new Bundle( );
        bundle.putBoolean( LandlordProfileFragment.KET_INITIALIZE, true );
        Fragment fragment = new LandlordProfileFragment( );
        fragment.setArguments( bundle );
        fm.beginTransaction( )
                .replace( R.id.fragment_container_top, fragment )
                .commit( );
    }

    public void changeToRoomEditing( ) {
        FragmentManager fm = getSupportFragmentManager( );
        Bundle bundle = new Bundle( );
        bundle.putParcelableArrayList( EditRoomsFragment.KEY_ROOMS, mLandlordsRooms );
        Fragment fragment = new EditRoomsFragment( );
        fragment.setArguments( bundle );
        fm.beginTransaction( )
                .replace( R.id.fragment_container_top, fragment )
                .commit( );
    }

    public void changeToChatSelection(){
        mAllProfilesFromQuery.removeAll( mNonTenantProfiles );
        Bundle bundle = new Bundle( );
        bundle.putParcelableArrayList( SelectChatFragment.KEY_ROOM_LANDLORD, mLandlordsRooms );
        Fragment fragment = new SelectChatFragment( );
        fragment.setArguments( bundle );
        FragmentManager fm = getSupportFragmentManager( );
        fm.beginTransaction( )
                .replace( R.id.fragment_container_top, fragment )
                .commit( );
    }

    /**
     * Adds a room to the list of rooms of the landlord that is share among all the fragment in the package.
     *
     * @param rooms new PostedRoom of the current landlord.
     */
    public void addLandlordRoom( RoomPosted rooms ) {
        if ( mLandlordsRooms.contains( rooms ) ) {
            int position = mLandlordsRooms.indexOf( rooms );
            mLandlordsRooms.set( position, rooms );
        } else {
            mLandlordsRooms.add( rooms );
        }
    }

    /**
     * Remove a room to the list of rooms of the landlord that is share among all the fragment in the package.
     *
     * @param room removes a room from the current landlord.
     */
    public void removeLandlordRoom( RoomPosted room ) {
        mLandlordsRooms.remove( room );
    }

    /**
     * Start a notification Service that is listening to changes in the Database creating
     * a new notification every time the user is having a new match with another room/tenant
     */
    public void startNotificationService( ) {
        if ( getBatteryLevel( ) > 10 && getNotificationOption( ) ) {
            ArrayList<Match> matches = new ArrayList<>( );
            ArrayList<String> ids = new ArrayList<>( );
            Profile p = ProfileSingleton.getInstance( );
            if ( p.getRole( ).equals( "Landlord" ) ) {
                for ( RoomPosted r : mLandlordsRooms ) {
                    matches.add( r.getMatch( ) );
                    ids.add( r.getRoomID( ) );
                }
            } else {
                matches.add( p.getMatch( ) );
                ids.add( p.getUserID( ) );
            }
            startService( NotificationService.getIntent( this, matches, ids ) );
        }
    }

    /**
     * Gets the current battery level as a integer from 0 to 100 representing the percentage.
     *
     * @return current percentage of battery as a integer
     */
    public int getBatteryLevel( ) {
        Intent BATTERYintent = this.registerReceiver( null, new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED ) );
        int level = 0;
        if ( BATTERYintent != null ) {
            level = BATTERYintent.getIntExtra( BatteryManager.EXTRA_LEVEL, - 1 );
        }
        Log.v( TAG, "LEVEL" + level );
        return level;
    }

    /**
     * Changes the notification options.
     *
     * @param option boolean if the notification service should be initialize at the start of the application.
     */
    public void changeNotificationOption( boolean option ) {
        SharedPreferences.Editor editor = getSharedPreferences( MY_PREFS_NAME, MODE_PRIVATE ).edit( );
        editor.putBoolean( NOTIFICATION_VALUE, option );
        editor.apply( );
    }

    /**
     * Value if notificationService should be initialize.
     *
     * @return value if the notification service should be initialize.
     */
    public boolean getNotificationOption( ) {
        SharedPreferences prefs = getSharedPreferences( MY_PREFS_NAME, MODE_PRIVATE );
        return prefs.getBoolean( NOTIFICATION_VALUE, true );
    }

    @Override
    public void onBackPressed( ) {
        mExitPopUp.showDialog( this, getString( R.string.close_app_msg ) );
    }

    /**
     * Creates the NavigationBar for the activity.
     */
    private void createNavigationBar( ) {
        setContentView( R.layout.activity_onefragment_navigationbar );
        final BottomNavigationView bottomNavigationView = findViewById( R.id.bottom_navigation );
        Menu bottomNavigationViewMenu = bottomNavigationView.getMenu( );
        bottomNavigationViewMenu.findItem( R.id.action_profile ).setChecked( false );
        mActiveBottomNavigationViewMenuItem = bottomNavigationViewMenu.findItem( R.id.action_home ).setChecked( true );

        bottomNavigationView.setOnNavigationItemSelectedListener( new BottomNavigationView.OnNavigationItemSelectedListener( ) {

            @Override
            public boolean onNavigationItemSelected( @NonNull MenuItem item ) {

                FragmentManager fm = getSupportFragmentManager( );

                switch ( item.getItemId( ) ) {

                    case R.id.action_profile:
                        changeToPorifleFragment( );
                        break;
                    case R.id.action_home:
                        fm.beginTransaction( )
                                .replace( R.id.fragment_container_top, swipingFragment )
                                .commit( );
                        break;
                    case R.id.action_chat:
                        changeToChatSelection();
                        break;
                }
                if ( item != mActiveBottomNavigationViewMenuItem ) {
                    mActiveBottomNavigationViewMenuItem.setChecked( false );
                    mActiveBottomNavigationViewMenuItem = item;
                }
                return true;
            }
        } );
    }

    private void fetchDataFromDatabaseAndStartSwipeFragment( ) {
        nanoStart = System.nanoTime();
        Log.i(TAG, "Start time cards: " + nanoStart);

        Profile p = ProfileSingleton.getInstance( );
        if ( p.getRole( ).equals( "Landlord" ) ) {
            startSwipeFragmentFromLandlord( );
        } else {
            startSwipeFragmentFromTenant( );
        }
    }

    /**
     * Reads the needed data form the databases, and on complete start the SwipeFragment from
     * Tenant view.
     */
    private void startSwipeFragmentFromTenant( ) {
        Query getLandlordsRooms = FirebaseFirestore.getInstance( )
                .collection( DataBasePath.ROOMS.getValue( ) );

        Task t1 = getLandlordsRooms.get( ).addOnSuccessListener( new OnSuccessListener<QuerySnapshot>( ) {
            @Override
            public void onSuccess( QuerySnapshot queryDocumentSnapshots ) {
                for ( DocumentSnapshot d : queryDocumentSnapshots ) {
                    mAllPostedRooms.add( d.toObject( RoomPosted.class ) );
                }
            }
        } );
        Tasks.whenAllSuccess( t1 ).addOnSuccessListener( new OnSuccessListener<List<Object>>( ) {
            @Override
            public void onSuccess( List<Object> list ) {
                startFragmentFromTenant( );
            }
        } );
    }

    /**
     * Reads the needed data form the databases, and on complete start the SwipeFragment from
     * Landlord view.
     */
    private void startSwipeFragmentFromLandlord( ) {
        Profile p = ProfileSingleton.getInstance( );

        Query tenantCandidateQuery = FirebaseFirestore.getInstance( )
                .collection( DataBasePath.USERS.getValue( ) );

        Query removeNonTenant = FirebaseFirestore.getInstance( )
                .collection( DataBasePath.USERS.getValue( ) )
                .whereEqualTo( "tenant", null );

        Query getLandlordsRooms = FirebaseFirestore.getInstance( )
                .collection( DataBasePath.ROOMS.getValue( ) )
                .whereEqualTo( "landlordID", p.getUserID( ) );

        Task t1 = tenantCandidateQuery.get( ).addOnSuccessListener( new OnSuccessListener<QuerySnapshot>( ) {
            @Override
            public void onSuccess( QuerySnapshot queryDocumentSnapshots ) {
                for ( DocumentSnapshot d : queryDocumentSnapshots ) {
                    mAllProfilesFromQuery.add( d.toObject( Profile.class ) );
                }
            }
        } );
        Task t2 = removeNonTenant.get( ).addOnSuccessListener( new OnSuccessListener<QuerySnapshot>( ) {
            @Override
            public void onSuccess( QuerySnapshot queryDocumentSnapshots ) {
                for ( DocumentSnapshot d : queryDocumentSnapshots ) {
                    mNonTenantProfiles.add( d.toObject( Profile.class ) );
                }
            }
        } );

        Task t3 = getLandlordsRooms.get( ).addOnSuccessListener( new OnSuccessListener<QuerySnapshot>( ) {
            @Override
            public void onSuccess( QuerySnapshot queryDocumentSnapshots ) {
                for ( DocumentSnapshot d : queryDocumentSnapshots ) {
                    mLandlordsRooms.add( d.toObject( RoomPosted.class ) );
                }
            }
        } );

        Tasks.whenAllSuccess( t1, t2, t3 ).addOnSuccessListener( new OnSuccessListener<List<Object>>( ) {
            @Override
            public void onSuccess( List<Object> list ) {
                startFragmentFromLandlord( );
            }
        } );

    }
}


