package com.itcom202.weroom.interaction.chat.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itcom202.weroom.R;
import com.itcom202.weroom.account.models.Profile;
import com.itcom202.weroom.account.models.RoomPosted;
import com.itcom202.weroom.framework.DataBasePath;
import com.itcom202.weroom.framework.ProfileSingleton;
import com.itcom202.weroom.framework.cameraandgallery.PictureConversion;
import com.itcom202.weroom.framework.queries.ImageController;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Fragment that displays all the users that the user can chat with.
 * <p>
 * When clicking on those users that are loaded in recyclerView it will open an new chat fragment.
 */
public class SelectChatFragment extends Fragment {
    public static final String KEY_ROOM_LANDLORD = "landlords_rooms";
    private RecyclerView mShowContactsView;
    private ChatAdapter mAdapter;
    private ArrayList<RoomPosted> mLandlordsRooms;
    private RoomPosted mCurrentSelectedRoom;

    @Nullable
    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
        View v = inflater.inflate( R.layout.fragment_selectchat, container, false );
        mShowContactsView = v.findViewById( R.id.contact_recycler_view );
        TabLayout tabLayout = v.findViewById( R.id.tab_layout_top_toolbar );

        mShowContactsView.setLayoutManager( new LinearLayoutManager( getActivity( ) ) );

        if ( ProfileSingleton.getInstance( ).getRole( ).equals( "Landlord" ) ) {
            if ( getArguments( ) != null ) {
                mLandlordsRooms = getArguments( ).getParcelableArrayList( KEY_ROOM_LANDLORD );
                if ( mLandlordsRooms != null ) {
                    mCurrentSelectedRoom = mLandlordsRooms.get( 0 );
                }
            }
            List<String> rooms = getRoomsStrings( );
            for ( String s : rooms ) {
                tabLayout.addTab( tabLayout.newTab( ).setText( s ) );
            }
            tabLayout.setTabGravity( TabLayout.GRAVITY_FILL );
            tabLayout.addOnTabSelectedListener( new TabLayout.OnTabSelectedListener( ) {
                @Override
                public void onTabSelected( TabLayout.Tab tab ) {
                    mCurrentSelectedRoom = mLandlordsRooms.get( tab.getPosition( ) );
                    updateUI( );
                }

                @Override
                public void onTabUnselected( TabLayout.Tab tab ) {

                }

                @Override
                public void onTabReselected( TabLayout.Tab tab ) {

                }
            } );
        } else {
            tabLayout.setVisibility( View.GONE );
        }

        updateUI( );

        return v;
    }

    /**
     * Updates the value of the chatAdapter with all existing matches from the current user.
     */
    private void updateUI( ) {
        List<String> contacts;
        Profile p = ProfileSingleton.getInstance( );
        if ( p.getRole( ).equals( "Landlord" ) ) {
            contacts = mCurrentSelectedRoom.getMatch( ).getMatch( );
        } else {//the user has a tenant rol.
            contacts = p.getMatch( ).getMatch( );
        }
        mAdapter = new ChatAdapter( contacts );
        mShowContactsView.setAdapter( mAdapter );
    }

    /**
     * Creates a List of string with the CompleteAdress of mLandlordsRooms.
     *
     * @return List<String> of mLandlordsRooms complete address.
     */
    private List<String> getRoomsStrings( ) {
        List<String> roomsName = new ArrayList<>( );
        for ( RoomPosted p : mLandlordsRooms )
            roomsName.add( p.getCompleteAddress( ) );

        return roomsName;
    }

    /**
     * Create unique chat ID combining two different ID's
     *
     * @param ID_one ID from one user.
     * @param ID_two ID from second user.
     * @return Unique chat ID combining ID_one and ID_two
     */
    private String createChatID( String ID_one, String ID_two ) {
        String chat_ID;
        if ( ID_one.compareTo( ID_two ) < 0 )
            chat_ID = ID_one + "_" + ID_two;
        else
            chat_ID = ID_two + "_" + ID_one;
        return chat_ID;
    }

    /**
     * Adapter that reads get all matches and displays in a RecyclerView all matched contacts.
     */
    private class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<String> mChatPartnerIDS;

        ChatAdapter( List<String> chatPartnerIDS ) {
            this.mChatPartnerIDS = chatPartnerIDS;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder( @NonNull ViewGroup viewGroup, int i ) {
            LayoutInflater layoutInflater = LayoutInflater.from( getActivity( ) );
            if ( i == 1 )//tenant case
                return new ProfileHolder( layoutInflater, viewGroup );
            else
                return new RoomHolder( layoutInflater, viewGroup );//room case


        }

        @Override
        public void onBindViewHolder( @NonNull RecyclerView.ViewHolder viewHolder, int i ) {
            String contact = mChatPartnerIDS.get( i );

            if ( viewHolder instanceof ProfileHolder )
                ( ( ProfileHolder ) viewHolder ).bind( contact );
            if ( viewHolder instanceof RoomHolder )
                ( ( RoomHolder ) viewHolder ).bind( contact );
        }

        @Override
        public int getItemCount( ) {
            return mChatPartnerIDS.size( );
        }

        @Override
        public int getItemViewType( int position ) {
            String contact = mChatPartnerIDS.get( position );
            if ( contact.length( ) != 36 )
                return 1; //tenant case
            else
                return 2; //landlord case
        }

        /**
         * Holder for contacts that are Tenants.
         */
        private class ProfileHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView mProfileName;
            private TextView mProfileAge;
            private ImageView mProfilePicture;

            private String mContact;

            ProfileHolder( LayoutInflater inflater, ViewGroup parent ) {
                super( inflater.inflate( R.layout.list_contacts, parent, false ) );
                itemView.setOnClickListener( this );

                mProfileName = itemView.findViewById( R.id.list_contact_name );
                mProfileAge = itemView.findViewById( R.id.list_contact_age );
                mProfilePicture = itemView.findViewById( R.id.list_contact_profile );
            }

            @Override
            public void onClick( View v ) {
                FragmentTransaction transaction;
                if ( getFragmentManager( ) != null ) {
                    transaction = getFragmentManager( ).beginTransaction( );
                    Fragment chatFragment = new ChatFragment( );
                    Bundle bundle = new Bundle( );
                    bundle.putString( ChatFragment.KET_CHAT_ID, createChatID( mContact, mCurrentSelectedRoom.getRoomID( ) ) );
                    chatFragment.setArguments( bundle );
                    transaction.replace( R.id.fragment_container_top, chatFragment );
                    transaction.commit( );
                }

            }

            void bind( String contact ) {
                FirebaseFirestore.getInstance( )
                        .collection( DataBasePath.USERS.getValue( ) )
                        .document( contact )
                        .get( ).addOnSuccessListener( new OnSuccessListener<DocumentSnapshot>( ) {
                    @Override
                    public void onSuccess( DocumentSnapshot documentSnapshot ) {
                        Profile p = documentSnapshot.toObject( Profile.class );
                        if ( p != null ) {
                            mProfileName.setText( p.getName( ) );
                            mProfileAge.setText( String.format( Locale.getDefault( ), "%d", p.getAge( ) ) );
                        }
                    }
                } );
                mContact = contact;
                Task t = ImageController.getProfilePicture( mContact );
                t.addOnSuccessListener( new OnSuccessListener<byte[]>( ) {
                    @Override
                    public void onSuccess( final byte[] bytes ) {
                        mProfilePicture.setImageBitmap( PictureConversion.byteArrayToBitmap( bytes ) );
                    }
                } );
            }

        }

        /**
         * Holder for contacts that are Landlords.
         */
        private class RoomHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView mLandlordName;
            private TextView mLandlordAge;
            private TextView mAddressTextView;
            private ImageView mProfilePicture;

            private String mContact;
            private RoomPosted mRoomPosted;

            RoomHolder( LayoutInflater inflater, ViewGroup parent ) {
                super( inflater.inflate( R.layout.list_contacts, parent, false ) );
                itemView.setOnClickListener( this );

                mLandlordName = itemView.findViewById( R.id.list_contact_name );
                mLandlordAge = itemView.findViewById( R.id.list_contact_age );
                mAddressTextView = itemView.findViewById( R.id.list_contacts_address );
                mProfilePicture = itemView.findViewById( R.id.list_contact_profile );
            }

            @Override
            public void onClick( View v ) {
                FragmentTransaction transaction;
                if ( getFragmentManager( ) != null ) {
                    transaction = getFragmentManager( ).beginTransaction( );
                    Fragment chatFragment = new ChatFragment( );
                    Bundle bundle = new Bundle( );
                    bundle.putString( ChatFragment.KET_CHAT_ID, createChatID( mContact, ProfileSingleton.getInstance( ).getUserID( ) ) );
                    chatFragment.setArguments( bundle );
                    transaction.replace( R.id.fragment_container_top, chatFragment );
                    transaction.commit( );
                }
            }

            void bind( String contact ) {
                mContact = contact;
                Task t1 = FirebaseFirestore.getInstance( )
                        .collection( DataBasePath.ROOMS.getValue( ) )
                        .document( contact )
                        .get( ).addOnSuccessListener( new OnSuccessListener<DocumentSnapshot>( ) {
                            @Override
                            public void onSuccess( DocumentSnapshot documentSnapshot ) {
                                mRoomPosted = documentSnapshot.toObject( RoomPosted.class );
                                if ( mRoomPosted != null ) {
                                    mAddressTextView.setText( mRoomPosted.getCompleteAddress( ) );
                                }

                            }
                        } );
                t1.addOnCompleteListener( new OnCompleteListener( ) {
                    @Override
                    public void onComplete( @NonNull Task task ) {
                        FirebaseFirestore.getInstance( ).collection( DataBasePath.USERS.getValue( ) )
                                .document( mRoomPosted.getLandlordID( ) )
                                .get( ).addOnSuccessListener( new OnSuccessListener<DocumentSnapshot>( ) {
                            @Override
                            public void onSuccess( DocumentSnapshot documentSnapshot ) {
                                Profile p = documentSnapshot.toObject( Profile.class );
                                if ( p != null ) {
                                    mLandlordName.setText( p.getName( ) );
                                    mLandlordAge.setText( String.format( Locale.getDefault( ), "%d", p.getAge( ) ) );
                                    Task t = ImageController.getProfilePicture( p.getUserID( ) );
                                    t.addOnSuccessListener( new OnSuccessListener<byte[]>( ) {
                                        @Override
                                        public void onSuccess( final byte[] bytes ) {
                                            mProfilePicture.setImageBitmap( PictureConversion.byteArrayToBitmap( bytes ) );
                                        }
                                    } );

                                }
                            }
                        } );
                    }
                } );

            }
        }
    }

}
