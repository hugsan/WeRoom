package com.itcom202.weroom.account.onboarding.views.flow;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.itcom202.weroom.R;
import com.itcom202.weroom.account.models.LandlordProfile;
import com.itcom202.weroom.account.models.Profile;
import com.itcom202.weroom.framework.ProfileSingleton;
import com.itcom202.weroom.framework.SingleFragment;
import com.itcom202.weroom.interaction.InteractionActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * Fragment that allows the user to create a new LandlordProfile.
 */
public class LandlordProfileFragment extends SingleFragment {
    public static final String KET_INITIALIZE = "initialize";
    private final String MIN_AGE = "18";
    private final String MAX_AGE = "99";
    private Spinner mTenantNation;
    private EditText mTenantMinAge;
    private EditText mTenantMaxAge;
    private Spinner mTenantGender;
    private Spinner mTenantOccupation;
    private RadioGroup mSocialGroup;
    private RadioGroup mSmokingGroup;
    private Button mConfirm;
    private String socialValue = LandlordProfile.I_DONT_CARE;
    private String smokingValue = LandlordProfile.I_DONT_CARE;
    private boolean mEditable = false;

    @Nullable
    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
        View v = inflater.inflate( R.layout.profile_landlord_fragment, container, false );

        mTenantNation = v.findViewById( R.id.spinnerNationalityLLlandlordfragment );
        mTenantMinAge = v.findViewById( R.id.ageMinT );
        mTenantMaxAge = v.findViewById( R.id.ageMaxT );
        mTenantGender = v.findViewById( R.id.spinnerGenderLL );
        mTenantOccupation = v.findViewById( R.id.occupation );
        mSocialGroup = v.findViewById( R.id.socialgroup );
        mSmokingGroup = v.findViewById( R.id.smokinggroup );
        mConfirm = v.findViewById( R.id.confirmtenantprofile );

        ArrayAdapter<CharSequence> adapterGenderTenant = ArrayAdapter.createFromResource( Objects.requireNonNull( getActivity( ) ), R.array.gender_array, R.layout.spinner_item );
        adapterGenderTenant.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        mTenantGender.setAdapter( adapterGenderTenant );

        ArrayAdapter<CharSequence> adapterOccup = ArrayAdapter.createFromResource( getActivity( ), R.array.occupation, R.layout.spinner_item );
        adapterOccup.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        mTenantOccupation.setAdapter( adapterOccup );
        mTenantNation.setAdapter( countryAdapter( ) );

        mSocialGroup.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener( ) {
            public void onCheckedChanged( RadioGroup group, int checkedId ) {
                switch ( checkedId ) {
                    case R.id.radiossocialyes:
                        socialValue = LandlordProfile.YES;
                        break;
                    case R.id.radiosocialno:
                        socialValue = LandlordProfile.NO;
                        break;
                    case R.id.radiosocialidc:
                        socialValue = LandlordProfile.I_DONT_CARE;
                        break;
                }
            }
        } );
        mSmokingGroup.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener( ) {
            public void onCheckedChanged( RadioGroup group, int checkedId ) {
                switch ( checkedId ) {
                    case R.id.radiosmokeyes:
                        smokingValue = LandlordProfile.YES;
                        break;
                    case R.id.radiosmokeno:
                        smokingValue = LandlordProfile.NO;
                        break;
                    case R.id.radiosmokeidc:
                        smokingValue = LandlordProfile.I_DONT_CARE;
                        break;
                }
            }
        } );

        mConfirm.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick( View v ) {
                String userID = Objects.requireNonNull( FirebaseAuth.getInstance( ).getCurrentUser( ) ).getUid( );
                boolean noError = true;
                if ( mTenantMinAge.getText( ).toString( ).length( ) == 0 ) {

                    mTenantMinAge.setText( MIN_AGE );
                }
                if ( Integer.parseInt( mTenantMinAge.getText( ).toString( ) ) < 18 ) {
                    mTenantMinAge.setError( getString( R.string.min_age ) );
                    mTenantMinAge.requestFocus( );
                    mTenantMinAge.setText( MIN_AGE );
                    noError = false;
                }
                if ( mTenantMaxAge.getText( ).toString( ).length( ) == 0 ) {
                    mTenantMaxAge.setText( MAX_AGE );
                }

                if(Integer.parseInt( mTenantMinAge.getText( ).toString( ) )>Integer.parseInt( mTenantMaxAge.getText( ).toString( ) )){
                    mTenantMaxAge.setError( getString( R.string.wrong_age ) );
                    mTenantMaxAge.requestFocus( );
                    mTenantMinAge.setError( getString( R.string.wrong_age ) );
                    mTenantMinAge.requestFocus( );
                    noError = false;
                }


                if ( Integer.parseInt( mTenantMaxAge.getText( ).toString( ) ) > 99 ) {
                    mTenantMaxAge.setError( getString( R.string.max_age ) );
                    mTenantMaxAge.requestFocus( );
                    mTenantMaxAge.setText( MAX_AGE );
                    noError = false;
                }

                if ( noError ) {
                    LandlordProfile newInput = new LandlordProfile.Builder( userID )
                            .withTenantNationality( getISOCode( String.valueOf( mTenantNation.getSelectedItem( ) ) ) )
                            .withTenantAge( Integer.parseInt( mTenantMinAge.getText( ).toString( ) ), Integer.parseInt( mTenantMaxAge.getText( ).toString( ) ) )
                            .withTenantGender( String.valueOf( mTenantGender.getSelectedItem( ) ) )
                            .withTenantOccupation( String.valueOf( mTenantOccupation.getSelectedItem( ) ) )
                            .tenantSocial( socialValue )
                            .canTenantSmoke( smokingValue )
                            .build( );

                    Profile p = ProfileSingleton.getInstance( );
                    p.setLandlord( newInput );
                    ProfileSingleton.update( p );
                    if ( mEditable ) {
                        ( ( InteractionActivity ) Objects.requireNonNull( getActivity( ) ) ).changeToPorifleFragment( );
                    } else {
                        changeFragment( new RoomCreationFragment( ) );
                    }
                }

            }
        } );

        if ( getArguments( ) != null && getArguments( ).getBoolean( KET_INITIALIZE ) ) {
            mConfirm.setText( R.string.edit_landlord );
            initializeLandlord( v, ProfileSingleton.getInstance( ).getLandlord( ) );
        }

        return v;


    }

    @Override
    public void onResume( ) {
        super.onResume( );
        Objects.requireNonNull( getActivity( ) ).setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );

    }


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

    private String getISOCode( String selectedCountry ) {
        Map<String, String> countries = new HashMap<>( );
        for ( String iso : Locale.getISOCountries( ) ) {
            Locale l = new Locale( "", iso );
            countries.put( l.getDisplayCountry( ), iso );
        }

        return countries.get( selectedCountry );

    }

    private void initializeLandlord( View v, LandlordProfile lp ) {
        mEditable = true;
        mTenantNation.setSelection( getCountryAdapterPosition( lp.getTenantNation( ) ) );
        mTenantMinAge.setText( String.format( Locale.getDefault( ), "%d", lp.getTenantMinAge( ) ) );
        mTenantMaxAge.setText( String.format( Locale.getDefault( ), "%d", lp.getTenantMaxAge( ) ) );
        String smoking = lp.getAllowTenantSmoking( );
        switch ( smoking ) {
            case LandlordProfile.YES:
                ( ( RadioButton ) v.findViewById( R.id.radiosmokeyes ) ).setChecked( true );
                break;
            case LandlordProfile.NO:
                ( ( RadioButton ) v.findViewById( R.id.radiosmokeno ) ).setChecked( true );
                break;
            case LandlordProfile.I_DONT_CARE:
                ( ( RadioButton ) v.findViewById( R.id.radiosmokeidc ) ).setChecked( true );
                break;
        }
        String social = lp.getSocialTenant( );
        switch ( social ) {
            case LandlordProfile.YES:
                ( ( RadioButton ) v.findViewById( R.id.radiossocialyes ) ).setChecked( true );
                break;
            case LandlordProfile.NO:
                ( ( RadioButton ) v.findViewById( R.id.radiosocialno ) ).setChecked( true );
                break;
            case LandlordProfile.I_DONT_CARE:
                ( ( RadioButton ) v.findViewById( R.id.radiosocialidc ) ).setChecked( true );
                break;
        }
        ArrayAdapter<CharSequence> adapterGender = ArrayAdapter.createFromResource( Objects.requireNonNull( getActivity( ) ), R.array.gender_array, R.layout.spinner_item );
        adapterGender.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        mTenantGender.setSelection( adapterGender.getPosition( lp.getTenantGender( ) ) );

        ArrayAdapter<CharSequence> adapterOccup = ArrayAdapter.createFromResource( getActivity( ), R.array.occupation, R.layout.spinner_item );
        adapterOccup.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        mTenantOccupation.setSelection( adapterOccup.getPosition( lp.getTenantOccupation( ) ) );

    }

    /**
     * Method that return the position of the spinner of ISO country's translated in english from a ISO country code.
     *
     * @param countryISO country ISO code.
     * @return Position of our spinner where it its allocated the ISO code when translated to english.
     */
    private int getCountryAdapterPosition( String countryISO ) {
        String[] locales = Locale.getISOCountries( );

        List<String> countries = new ArrayList<>( );
        countries.add( getString( R.string.prompt_country ) );

        // for (String countryCode : locales){
        for ( String countryCode : locales ) {
            Locale obj = new Locale( "", countryCode );
            countries.add( obj.getDisplayCountry( Locale.ENGLISH ) );
        }
        Collections.sort( countries );
        Locale obj = new Locale( "", countryISO );
        return countries.indexOf( obj.getDisplayCountry( Locale.ENGLISH ) );
    }
}
