package com.itcom202.weroom.interaction.swipe.views;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.itcom202.weroom.R;
import com.itcom202.weroom.account.models.Profile;
import com.itcom202.weroom.account.onboarding.controllers.tagDescription.TagView;
import com.itcom202.weroom.account.onboarding.views.widget.OpenPictureFragment;
import com.itcom202.weroom.framework.cameraandgallery.PictureConversion;
import com.itcom202.weroom.framework.queries.ImageController;

import java.util.Locale;
import java.util.Objects;

/**
 * Fragment used to display the card information of a Tenant.
 */
public class CardInfoTenantFragment extends Fragment {
    private static final String KEY_TENANT = "mytenant";
    private ImageButton mButtonExit;
    private ImageView mPhoto;
    private Profile mProfile;
    private TagView mTagView;
    private TextView mTenantName;
    private TextView mTenantAge;
    private TextView mTenantGender;
    private TextView mTenantNation;
    private TextView mSmoker;

    /**
     * Method to create a Intent to call the fragment CardInfoTenantFragment with a corresponding Profile.
     *
     * @param profile Profile to be displayed in the card fragment.
     * @return Intent for CardInfoTenantFragment with its corresponding bundle.
     */
    public static CardInfoTenantFragment newInstance( Profile profile ) {
        CardInfoTenantFragment fragment = new CardInfoTenantFragment( );
        Bundle bundle = new Bundle( );
        bundle.putParcelable( KEY_TENANT, profile );
        fragment.setArguments( bundle );
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
        View v = inflater.inflate( R.layout.fragment_card_info_tenant, container, false );

        mTenantName = v.findViewById( R.id.card_tenant_name );
        mTenantAge = v.findViewById( R.id.card_tenant_age );
        mTenantGender = v.findViewById( R.id.card_tenant_gender );
        mTenantNation = v.findViewById( R.id.card_tenant_nation );
        mSmoker = v.findViewById( R.id.card_tenant_smoking );
        mTagView = v.findViewById( R.id.card_tenant_description );
        mPhoto = v.findViewById( R.id.card_tenant_picture );
        if ( getArguments( ) != null )
            mProfile = getArguments( ).getParcelable( KEY_TENANT );
        mButtonExit = v.findViewById( R.id.button_exit_info_page_tenant );
        mButtonExit.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick( View v ) {
                AppCompatActivity activity = ( AppCompatActivity ) mButtonExit.getContext( );
                activity.getSupportFragmentManager( )
                        .popBackStackImmediate( );
            }
        } );

        updateUI( );

        return v;
    }

    /**
     * Initialize the field of the card with the values from mProfile.
     */
    private void updateUI( ) {
        mTenantName.setText( mProfile.getName( ) );
        mTenantAge.setText( String.format( Locale.getDefault( ), "%d", mProfile.getAge( ) ) );
        mTenantGender.setText( mProfile.getGender( ) );
        Locale l = new Locale( "", mProfile.getCountry( ) );
        mTenantNation.setText( l.getDisplayCountry( ) );
        mSmoker.setText( mProfile.getTenant( ).getSmokeFriendly( ) );

        for ( String s : mProfile.getTags( ) )
            mTagView.addTag( s, false );

        Task t = ImageController.getProfilePicture( mProfile.getUserID( ) );

        t.addOnSuccessListener( new OnSuccessListener<byte[]>( ) {
            @Override
            public void onSuccess( final byte[] bytes ) {
                Bitmap picture = PictureConversion.byteArrayToBitmap( bytes );
                mPhoto.setImageBitmap( picture );
                mPhoto.setOnClickListener( new View.OnClickListener( ) {
                    @Override
                    public void onClick( View v ) {
                        FragmentTransaction ft = Objects.requireNonNull( getActivity( ) )
                                .getSupportFragmentManager( ).beginTransaction( );
                        ft.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN );
                        OpenPictureFragment openPic = new OpenPictureFragment( );

                        Bundle bundle = new Bundle( );
                        bundle.putByteArray( "picture", bytes );
                        openPic.setArguments( bundle );
                        ft.replace( android.R.id.content, openPic );
                        ft.addToBackStack( null );
                        ft.commit( );
                    }
                } );
            }
        } );
    }
}