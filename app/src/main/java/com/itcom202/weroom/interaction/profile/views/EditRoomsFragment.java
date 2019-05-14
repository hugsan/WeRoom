package com.itcom202.weroom.interaction.profile.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itcom202.weroom.R;
import com.itcom202.weroom.account.models.RoomPosted;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment that allows the user to edit Rooms that belong to them.
 *
 *
 */
public class EditRoomsFragment extends Fragment {
    public static final String KEY_ROOMS = "landlords_rooms";
    private ArrayList<RoomPosted> mLandlordsRoom;
    private List<Fragment> mRoomFragment = new ArrayList<>( );

    @Nullable
    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
        View v = inflater.inflate( R.layout.fragment_room_editing_tabs, container, false );

        if ( getArguments( ) != null )
            mLandlordsRoom = getArguments( ).getParcelableArrayList( KEY_ROOMS );

        TabLayout tabLayout = v.findViewById( R.id.tab_layout_swipe );
        for ( RoomPosted room : mLandlordsRoom ) {
            tabLayout.addTab( tabLayout.newTab( ).setText( room.getCompleteAddress( ) ) );
            Bundle bundle = new Bundle( );
            bundle.putParcelable( RoomEditFragment.KEY_ROOM, room );
            Fragment fragment = new RoomEditFragment( );
            fragment.setArguments( bundle );
            mRoomFragment.add( fragment );
        }
        for ( int i = 0 ; i < ( 3 - tabLayout.getTabCount( ) ) ; i++ ) {
            tabLayout.addTab( tabLayout.newTab( ).setText( R.string.add_more_rooms ) );
            mRoomFragment.add( new RoomEditFragment( ) );
        }

        FragmentManager fm = getChildFragmentManager( );
        fm.beginTransaction( )
                .replace( R.id.room_fragment_container, mRoomFragment.get( 0 ) )
                .commit( );

        tabLayout.setTabGravity( TabLayout.GRAVITY_FILL );

        tabLayout.addOnTabSelectedListener( new TabLayout.OnTabSelectedListener( ) {
            @Override
            public void onTabSelected( TabLayout.Tab tab ) {

                FragmentManager fm = getChildFragmentManager( );
                fm.beginTransaction( )
                        .replace( R.id.room_fragment_container, mRoomFragment.get( tab.getPosition( ) ) )
                        .commit( );
            }

            @Override
            public void onTabUnselected( TabLayout.Tab tab ) {
            }

            @Override
            public void onTabReselected( TabLayout.Tab tab ) {
            }
        } );

        return v;
    }
}
