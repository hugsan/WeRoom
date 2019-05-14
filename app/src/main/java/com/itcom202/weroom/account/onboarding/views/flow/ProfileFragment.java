package com.itcom202.weroom.account.onboarding.views.flow;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itcom202.weroom.R;
import com.itcom202.weroom.account.models.Profile;
import com.itcom202.weroom.account.onboarding.controllers.tagDescription.TagModel;
import com.itcom202.weroom.account.onboarding.controllers.tagDescription.TagSeparator;
import com.itcom202.weroom.account.onboarding.controllers.tagDescription.TagView;
import com.itcom202.weroom.framework.DataBasePath;
import com.itcom202.weroom.framework.ProfileSingleton;
import com.itcom202.weroom.framework.SingleFragment;
import com.itcom202.weroom.framework.cameraandgallery.ImagePicker;
import com.itcom202.weroom.framework.cameraandgallery.PictureConversion;
import com.itcom202.weroom.framework.queries.ImageController;
import com.itcom202.weroom.interaction.InteractionActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

/**
 * Fragment that allows the user creating a new profile. First fragment from the account creation flow.
 */
public class ProfileFragment extends SingleFragment {
    public static final String KEY_IS_EDIT = "editable";
    static final int REQUEST_CODE = 123;
    private static final String TAG = "ProfileFragment";
    public Spinner mRole;
    private String userID = Objects.requireNonNull( FirebaseAuth.getInstance( ).getCurrentUser( ) ).getUid( );
    private FirebaseAuth mFirebaseAuth;
    private EditText mUserName;
    private EditText mAge;
    private TextView mRoleTextView;
    private Button mCreateProfile;
    private Spinner mGender;
    private Spinner mCountry;
    private TagView mTag;
    private List<String> tags = new ArrayList<>( );
    private ImageView mProfilePhoto;
    private Bitmap mPicture;
    private boolean mEditable = false;

    @Nullable
    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
        View v = inflater.inflate( R.layout.profile_fragment, container, false );

        mFirebaseAuth = FirebaseAuth.getInstance( );

        mUserName = v.findViewById( R.id.username );
        mAge = v.findViewById( R.id.list_contact_age );
        mCreateProfile = v.findViewById( R.id.createprofile );
        mGender = v.findViewById( R.id.spinnerGender );
        mCountry = v.findViewById( R.id.spinnerCountry );
        mRole = v.findViewById( R.id.spinnerRole );
        mTag = v.findViewById( R.id.tags );
        mRoleTextView = v.findViewById( R.id.spinnerTextfieldRole);

        mTag.setHint( getString( R.string.description ) );
        mTag.addTagSeparator( TagSeparator.ENTER_SEPARATOR );
        String[] tagList = new String[]{ getString( R.string.hint_1 ), getString( R.string.hint_2 ), getString( R.string.hint_3 ) };
        mTag.setTagList( tagList );

        ArrayAdapter<CharSequence> adapterGender = ArrayAdapter.createFromResource( Objects.requireNonNull( getActivity( ) ), R.array.gender_array, R.layout.spinner_item );
        adapterGender.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        mGender.setAdapter( adapterGender );

        ArrayAdapter<CharSequence> adapterRole = ArrayAdapter.createFromResource( getActivity( ), R.array.role_array, R.layout.spinner_item );
        adapterRole.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        mRole.setAdapter( adapterRole );

        mCountry.setAdapter( countryAdapter( ) );

        mCreateProfile.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick( View v ) {
                if ( mPicture == null ) {
                    Toast.makeText( getContext( ), R.string.please_picture, Toast.LENGTH_SHORT ).show( );
                } else if ( mUserName.getText( ).toString( ).equals( "" ) ) {
                    mUserName.setError( getString( R.string.type_name ) );
                    mUserName.requestFocus( );
                } else if ( mAge.getText( ).toString( ).isEmpty( ) ) {
                    mAge.setError( getString( R.string.type_age ) );
                    mAge.requestFocus( );
                } else if ( Integer.parseInt( mAge.getText( ).toString( ) ) < 17 ) {
                    mAge.setError( getString( R.string.too_young ) );
                    mAge.requestFocus( );
                } else if ( Integer.parseInt( mAge.getText( ).toString( ) ) > 99 ) {
                    mAge.setError( getString( R.string.too_old ) );
                    mAge.requestFocus( );
                } else if ( mGender.getSelectedItemPosition( ) == 0 ) {
                    TextView errorText = ( TextView ) mGender.getSelectedView( );
                    errorText.setError( "" );
                    errorText.setTextColor( Color.RED );
                    errorText.setText( R.string.select_gender );

                } else if ( mCountry.getSelectedItemPosition( ) == 0 ) {
                    TextView errorText = ( TextView ) mCountry.getSelectedView( );
                    errorText.setError( "" );
                    errorText.setTextColor( Color.RED );
                    errorText.setText( R.string.select_country );
                } else {
                    for ( TagModel model : mTag.getSelectedTags( ) ) {
                        tags.add( model.getTagText( ) );
                    }
                    createProfile( );
                    if ( mEditable ) {
                        ( ( InteractionActivity ) Objects.requireNonNull( getActivity( ) ) ).changeToPorifleFragment( );
                    } else {
                        if ( mRole.getSelectedItemId( ) == 0 ) {
                            changeFragment( new LandlordProfileFragment( ) );
                        } else if ( mRole.getSelectedItemId( ) == 1 ) {
                            changeFragment( new ProfileTenantFragment( ) );
                        }
                    }

                }
            }
        } );

        mProfilePhoto = v.findViewById( R.id.profilePhoto );
        mProfilePhoto.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick( View v ) {

                Intent chooseImageIntent = ImagePicker.getPickImageIntent( getActivity( ) );
                startActivityForResult( chooseImageIntent, REQUEST_CODE );
            }
        } );
        if ( getArguments( ) != null && getArguments( ).getBoolean( KEY_IS_EDIT ) )
            setProfile( ProfileSingleton.getInstance( ) );
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
                mProfilePhoto.setImageBitmap( bitmap );
                mPicture = bitmap;
                mProfilePhoto.setScaleType( ImageView.ScaleType.CENTER_CROP );
            } else {
                super.onActivityResult( requestCode, resultCode, data );
            }

        } else
            Log.d( TAG, "Error on camera/Gallery" );
    }

    /**
     * creates a SpinnerAdapter with all the countrys gotten from the Java API Locale.
     *
     * @return SpinnerAdapter that cointains all countrys in English.
     */
    private SpinnerAdapter countryAdapter( ) {
        String[] locales = Locale.getISOCountries( );

        List<String> countries = new ArrayList<>( );
        countries.add( getString( R.string.prompt_country ) );

        // for (String countryCode : locales){
        for ( String countryCode : locales ) {
            Locale obj = new Locale( "", countryCode );
            countries.add( obj.getDisplayCountry( Locale.ENGLISH ) );
        }
        Collections.sort( countries );
        ArrayAdapter<String> adapter = new ArrayAdapter<>( Objects.requireNonNull( getContext( ) ), R.layout.spinner_item, countries );
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        return adapter;
    }

    /**
     * Method that get the ISO code for a given country written in english.
     *
     * @param selectedCountry String of a country, need to match with one from Locale class.
     * @return ISO code of a country.
     */
    private String getISOCode( String selectedCountry ) {
        Map<String, String> countries = new HashMap<>( );
        for ( String iso : Locale.getISOCountries( ) ) {
            Locale l = new Locale( "", iso );
            countries.put( l.getDisplayCountry( ), iso );
        }
        return countries.get( selectedCountry );
    }

    /**
     * Create profile, and updates the ProfileSingleTon and the database.
     */
    private void createProfile( ) {
        Profile profile = new Profile.Builder( userID )
                .withName( mUserName.getText( ).toString( ) )
                .withAge( Integer.parseInt( mAge.getText( ).toString( ) ) )
                .withGender( String.valueOf( mGender.getSelectedItem( ) ) )
                .withCountry( getISOCode( String.valueOf( mCountry.getSelectedItem( ) ) ) )
                .withRole( String.valueOf( mRole.getSelectedItem( ) ) )
                .withTags( tags )
                .build( );

        ImageController.setProfilePicture( Objects.requireNonNull( FirebaseAuth.getInstance( ).getCurrentUser( ) ).getUid( ),
                mPicture );
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance( );
        db.collection( DataBasePath.USERS.getValue( ) ).document( Objects.requireNonNull( mFirebaseAuth.getUid( ) ) )
                .set( profile );
        ProfileSingleton.initialize( profile );
    }

    /**
     * Updates the fragment to sow an existing profile in all its fields.
     *
     * @param p profile that would like to set in the fragment.
     */
    private void setProfile( Profile p ) {
        mEditable = true;

        Task t = ImageController.getProfilePicture( p.getUserID( ) );

        t.addOnSuccessListener( new OnSuccessListener<byte[]>( ) {
            @Override
            public void onSuccess( final byte[] bytes ) {
                mPicture = PictureConversion.byteArrayToBitmap( bytes );
                mProfilePhoto.setImageBitmap( mPicture );
            }
        } );
        mCountry.setSelection( getCountryAdapterPosition( p.getCountry( ) ) );
        mUserName.setText( p.getName( ) );
        mAge.setText( String.format( Locale.getDefault( ), "%d", p.getAge( ) ) );
        mRole.setVisibility( View.GONE );
        mRoleTextView.setVisibility(View.GONE);
        for ( String s : p.getTags( ) )
            mTag.addTag( s, false );
        ArrayAdapter<CharSequence> adapterGender =
                ArrayAdapter.createFromResource( Objects.requireNonNull( getActivity( ) ), R.array.gender_array, R.layout.spinner_item );
        adapterGender.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        mGender.setSelection( adapterGender.getPosition( p.getGender( ) ) );
        mCountry.setSelection( getCountryAdapterPosition( p.getCountry( ) ) );

        mCreateProfile.setText( R.string.edit_profile );

    }

    /**
     * Get the position of the CountrySpinner given a ISO country code.
     *
     * @param countryISO ISO country code.
     * @return Position where it is allocated in the spinner.
     */
    private int getCountryAdapterPosition( String countryISO ) {
        String[] locales = Locale.getISOCountries( );
        List<String> countries = new ArrayList<>( );
        countries.add( getString( R.string.prompt_country ) );

        for ( String countryCode : locales ) {
            Locale obj = new Locale( "", countryCode );
            countries.add( obj.getDisplayCountry( Locale.ENGLISH ) );
        }
        Locale obj = new Locale( "", countryISO );
        Collections.sort( countries );
        return countries.indexOf( obj.getDisplayCountry( Locale.ENGLISH ) );
    }
}