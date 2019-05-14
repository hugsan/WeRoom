package com.itcom202.weroom.account.onboarding.views.flow;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.itcom202.weroom.R;
import com.itcom202.weroom.account.models.Profile;
import com.itcom202.weroom.account.models.RoomPosted;
import com.itcom202.weroom.account.onboarding.views.widget.MapFragment;
import com.itcom202.weroom.framework.DataBasePath;
import com.itcom202.weroom.framework.PopUpMessage;
import com.itcom202.weroom.framework.ProfileSingleton;
import com.itcom202.weroom.framework.SingleFragment;
import com.itcom202.weroom.framework.cameraandgallery.ImagePicker;
import com.itcom202.weroom.framework.queries.ImageController;
import com.itcom202.weroom.interaction.InteractionActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Fragment that allows the user to create rooms when they are a landlord.
 */
public class RoomCreationFragment extends SingleFragment {
    static final int REQUEST_CODE = 123;
    public final String TAG = "RoomCreationFragment";
    private EditText mRent;
    private EditText mDeposit;
    private EditText mRoomDescription;
    private Spinner mPeriodRenting;
    private EditText mRoomSize;
    private CheckBox mFurnished;
    private CheckBox mInternet;
    private CheckBox mCommonArea;
    private CheckBox mLaundry;
    private Button mConfirmRoom;
    private DatabaseReference mDatabaseReference;
    private ImageButton mTakeRoomPicture;
    private String mUserId;
    private Button mAddAnotherRoom;
    private PopUpMessage mPopUp = new PopUpMessage( );
    private List<Bitmap> mRoomPictures = new ArrayList<>( );
    private LinearLayout layoutMap;
    private ImageView[] mPictures = new ImageView[ 10 ];
    private MapFragment mapFragment;

    private String mAddressID;
    private String mAddressName;
    private double mAddressLatitude;
    private double mAddressLongitude;


    @Nullable
    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState ) {
        View v = inflater.inflate( R.layout.add_room_fragment, container, false );

        mUserId = FirebaseAuth.getInstance( ).getUid( );

        mDatabaseReference = FirebaseDatabase.getInstance( ).getReference( );

        mRent = v.findViewById( R.id.rentInput );
        mDeposit = v.findViewById( R.id.depositroom );
        mPeriodRenting = v.findViewById( R.id.PeriodRentingRoomFragment );
        mRoomSize = v.findViewById( R.id.roomsizeroomfragment );
        mFurnished = v.findViewById( R.id.checkFurnished );
        mInternet = v.findViewById( R.id.checkBoxInternet );
        mCommonArea = v.findViewById( R.id.checkBoxCommonArea );
        mLaundry = v.findViewById( R.id.checkBoxLaundry );
        mConfirmRoom = v.findViewById( R.id.postRoomButton );
        mAddAnotherRoom = v.findViewById( R.id.addMoreRooms );
        layoutMap = v.findViewById( R.id.layoutmap );

        ConnectivityManager connManager = ( ConnectivityManager ) Objects.requireNonNull( getActivity( ) ).getSystemService( Context.CONNECTIVITY_SERVICE );
        final NetworkInfo wifi = connManager.getNetworkInfo( ConnectivityManager.TYPE_WIFI );

        for ( int i = 0 ; i < 10 ; i++ ) {
            String btnID = "picturepreviewnr" + ( i + 1 );
            int resID = getResources( ).getIdentifier( btnID, "id", Objects.requireNonNull( getActivity( ) ).getPackageName( ) );
            mPictures[ i ] = v.findViewById( resID );
            mPictures[ i ].setVisibility( View.GONE );
        }


        try {
            layoutMap.setVisibility( View.GONE );
        } catch ( Exception e ) {
            Log.d( TAG, "layout visible" );
        }

        final ArrayAdapter adapterPeriodRent = ArrayAdapter.createFromResource( getActivity( ), R.array.rending_period_array, R.layout.spinner_item );
        adapterPeriodRent.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        mPeriodRenting.setAdapter( adapterPeriodRent );

        mRoomDescription = v.findViewById( R.id.descriptionField );


        if ( wifi.isConnected( ) ) {
            mapFragment = new MapFragment( );
            final FragmentTransaction transaction = getChildFragmentManager( ).beginTransaction( );
            transaction.replace( R.id.showmapfragment, mapFragment ).commit( );
        }


        // Initialize Places. Validates with Google API key.
        Places.initialize( getApplicationContext( ), getString( R.string.google_cloud_api_key ) );

        // Create a new Places client instance.
        Places.createClient( getActivity( ) );

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = ( AutocompleteSupportFragment )
                getChildFragmentManager( ).findFragmentById( R.id.autocomplete_fragment );

        if ( autocompleteFragment != null ) {
            autocompleteFragment.setPlaceFields( Arrays.asList( Place.Field.LAT_LNG, Place.Field.NAME ) );
            autocompleteFragment.setOnPlaceSelectedListener( new PlaceSelectionListener( ) {
                @Override
                public void onPlaceSelected( @NonNull Place place ) {
                    mAddressName = place.getName( );
                    Log.i( TAG, "Place: " + place.getName( ) + ", " + place.getLatLng( ) );

                    if ( wifi.isConnected( ) ) {
                        try {
                            layoutMap.setVisibility( View.VISIBLE );
                            mapFragment.updateSite( place.getLatLng( ) );
                        } catch ( Exception e ) {
                            // Toast.makeText(getContext(), "No map", Toast.LENGTH_SHORT).show();
                            layoutMap.setVisibility( View.GONE );
                        }
                    }
                    mAddressID = place.getId( );

                    mAddressLatitude = Objects.requireNonNull( place.getLatLng( ) ).latitude;
                    mAddressLongitude = place.getLatLng( ).longitude;
                }

                @Override
                public void onError( @NonNull Status status ) {
                    Log.i( TAG, "An error occurred: " + status );
                    Toast.makeText( getActivity( ), R.string.room_creation_failed, Toast.LENGTH_SHORT ).show( );
                }
            } );
        }

        mConfirmRoom.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick( View v ) {
                if ( mRent.length( ) == 0 || Integer.parseInt( mRent.getText( ).toString( ) ) < 0 ) {
                    mRent.setError( getString( R.string.type_rent ) );
                    mRent.requestFocus( );
                } else if ( mDeposit.length( ) == 0 ) {
                    mDeposit.setError( getString( R.string.type_deposit ) );
                    mDeposit.requestFocus( );
                } else if ( mPeriodRenting.getSelectedItemPosition( ) == 0 ) {
                    TextView errorText = ( TextView ) mPeriodRenting.getSelectedView( );
                    errorText.setError( "" );
                    errorText.setTextColor( Color.RED );
                    errorText.setText( R.string.choose_period );
                } else if ( mAddressName == null ) {
                    Toast.makeText( getContext( ), R.string.type_address, Toast.LENGTH_SHORT ).show( );
                } else if ( mRoomSize.length( ) == 0 ) {
                    mRoomSize.setError( getString( R.string.type_room_size ) );
                    mRoomSize.requestFocus( );
                } else if ( mRoomDescription.length( ) == 0 ) {
                    Toast.makeText( getContext( ), R.string.type_description_room, Toast.LENGTH_SHORT ).show( );
                } else {
                    postRoom( true );
                }
            }
        } );

        mAddAnotherRoom.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick( View v ) {
                postRoom( false );
            }
        } );

        mTakeRoomPicture = v.findViewById( R.id.takeroompicture );
        mTakeRoomPicture.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick( View v ) {
                Intent chooseImageIntent = ImagePicker.getPickImageIntent( getActivity( ) );
                startActivityForResult( chooseImageIntent, REQUEST_CODE );
            }
        } );
        return v;
    }

    @Override
    public void onResume( ) {
        super.onResume( );
        Objects.requireNonNull( getActivity( ) ).setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
    }

    @Override
    public void onActivityResult( int requestCode, int resultCode, Intent data ) {
        Log.d( TAG, "request code: " + requestCode );
        Log.d( TAG, "result code: " + resultCode );
        if ( resultCode == RESULT_OK ) {
            if ( requestCode == REQUEST_CODE ) {
                Bitmap bitmap = null;
                try {
                    bitmap = ImagePicker.getImageFromResult( getActivity( ), resultCode, data );
                } catch ( IOException e ) {
                    //do sth
                }
                keepPicture( bitmap );
            } else {
                super.onActivityResult( requestCode, resultCode, data );
            }
        } else
            Log.d( TAG, "Error on camera/Gallery" );
    }

    /**
     * Method that post the room from the field in the fragment.
     *
     * @param once True to add only one room and move to InteractionActivity, false to keep adding rooms.
     */
    private void postRoom( final boolean once ) {
        if ( mRent.length( ) == 0 || Integer.parseInt( mRent.getText( ).toString( ) ) < 0 ) {
            mRent.setError( getString( R.string.type_rent ) );
            mRent.requestFocus( );
        } else if ( mDeposit.length( ) == 0 ) {
            mDeposit.setError( getString( R.string.type_deposit ) );
            mDeposit.requestFocus( );
        } else if ( mPeriodRenting.getSelectedItemPosition( ) == 0 ) {
            TextView errorText = ( TextView ) mPeriodRenting.getSelectedView( );
            errorText.setError( "" );
            errorText.setTextColor( Color.RED );
            errorText.setText( R.string.choose_period );
        } else if ( mAddressName == null ) {
            Toast.makeText( getContext( ), R.string.type_address, Toast.LENGTH_SHORT ).show( );
        } else if ( mRoomSize.length( ) == 0 ) {
            mRoomSize.setError( getString( R.string.type_room_size ) );
            mRoomSize.requestFocus( );
        } else if ( mRoomDescription.length( ) == 0 ) {
            Toast.makeText( getContext( ), R.string.type_description_room, Toast.LENGTH_SHORT ).show( );
        } else if ( mRoomPictures.size( ) < 3 ) {
            Toast.makeText( getContext( ), R.string.at_least_three_pictures, Toast.LENGTH_SHORT ).show( );
        } else {
            final RoomPosted input = new RoomPosted.Builder( mUserId )
                    .hasCommonAreas( mCommonArea.isChecked( ) )
                    .hasInternet( mInternet.isChecked( ) )
                    .hasLaundry( mLaundry.isChecked( ) )
                    .isFurnished( mFurnished.isChecked( ) )
                    .withAddress( mAddressID, mAddressName, mAddressLatitude, mAddressLongitude )
                    .withDeposit( Integer.parseInt( mDeposit.getText( ).toString( ) ) )
                    .withPeriodRenting( mPeriodRenting.getSelectedItemPosition( ) )
                    .withRent( Integer.parseInt( mRent.getText( ).toString( ) ) )
                    .withSize( Integer.parseInt( mRoomSize.getText( ).toString( ) ) )
                    .withDescription( mRoomDescription.getText( ).toString( ) )
                    .build( );

            final FirebaseFirestore db = FirebaseFirestore.getInstance( );

            db.collection( DataBasePath.USERS.getValue( ) )
                    .whereEqualTo( "userID", mUserId )
                    .get( ).addOnSuccessListener( new OnSuccessListener<QuerySnapshot>( ) {
                @Override
                public void onSuccess( QuerySnapshot queryDocumentSnapshots ) {
                    DocumentSnapshot doc = queryDocumentSnapshots.getDocuments( ).get( 0 );
                    Profile p = doc.toObject( Profile.class );
                    if ( Objects.requireNonNull( p ).getLandlord( ).getRoomsID( ).size( ) >= 3 ) {
                        mPopUp.showDialog( getActivity( ), getString( R.string.more_three_rooms_error ) );
                        mPopUp.changeToActivityOnClose( getActivity( ), InteractionActivity.newIntent( getActivity( ) ) );
                    } else {
                        if ( p.getLandlord( ).addRoomID( input.getRoomID( ) ) ) {
                            ProfileSingleton.update( p );
                        }
                        db.collection( DataBasePath.ROOMS.getValue( ) )
                                .document( input.getRoomID( ) )
                                .set( input );
                        uploadRoomPictures( input.getRoomID( ) );
                        if ( ! once ) {
                            mPopUp.showDialog( getActivity( ), getString( R.string.dialog_message ) );
                            changeFragment( new RoomCreationFragment( ) );
                        } else {
                            mPopUp.showDialog( getActivity( ), getString( R.string.dialog_title ) );
                            startActivity( InteractionActivity.newIntent( getActivity( ) ) );
                        }
                    }
                }
            } );
        }
    }

    /**
     * Upload to FirebaseStorage all the pictures taken in the fragment.
     *
     * @param roomID ID from the room to store it on the database.
     */
    private void uploadRoomPictures( String roomID ) {
        for ( int i = 0 ; i < mRoomPictures.size( ) ; i++ )
            ImageController.setRoomPicture( roomID, mRoomPictures.get( i ), i );
    }

    /**
     * Keep the bitmap picture in a list and display the taken picture in the ImageView from the fragmnet.
     *
     * @param bmp Picture to be stored on PictureList that will be uploaded when calling @uploadRoomPictures, and will be displayed in the fragment.
     */
    private void keepPicture( Bitmap bmp ) {
        int picturePosition = mRoomPictures.size( );
        if ( picturePosition < 10 ) {
            mPictures[ picturePosition ].setVisibility( View.VISIBLE );
            mPictures[ picturePosition ].setScaleType( ImageView.ScaleType.CENTER_CROP );
            mPictures[ picturePosition ].setImageBitmap( bmp );
            mRoomPictures.add( bmp );
        }
    }


}

