package com.itcom202.weroom.interaction.swipe.views.holders;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.itcom202.weroom.R;
import com.itcom202.weroom.account.models.Profile;
import com.itcom202.weroom.framework.cameraandgallery.PictureConversion;
import com.itcom202.weroom.framework.queries.ImageController;
import com.itcom202.weroom.interaction.swipe.views.CardInfoTenantFragment;

import java.util.Locale;

/**
 * Holder Tenant in SwipeFragment
 */
public class TenantHolder extends RecyclerView.ViewHolder {
    private TextView mTenantName;
    private TextView mTenantNationality;
    private TextView mTenantAge;
    private ImageView mPhoto;
    private Profile mProfile;

    public TenantHolder( @NonNull final View itemView ) {
        super( itemView );

        mTenantName = itemView.findViewById( R.id.text );
        mTenantNationality = itemView.findViewById( R.id.nationalityOfTenant );
        mTenantAge = itemView.findViewById( R.id.ageOfTenant );

        mPhoto = itemView.findViewById( R.id.photoCard );
        mPhoto.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick( View v ) {
                AppCompatActivity activity = ( AppCompatActivity ) itemView.getContext( );
                Fragment myFragment = CardInfoTenantFragment.newInstance( mProfile );
                activity.getSupportFragmentManager( ).beginTransaction( ).add( R.id.fragment_container_top, myFragment ).addToBackStack( null ).commit( );
            }
        } );
    }

    /**
     * Method that binds the profile to the holder.
     *
     * @param profile profile of a tenant.
     */
    public void bind( Profile profile ) {
        mProfile = profile;

        Locale l = new Locale( "", mProfile.getCountry( ) );

        mTenantName.setText( mProfile.getName( ) );
        mTenantNationality.setText( l.getDisplayCountry( ) );
        mTenantAge.setText( String.format( Locale.getDefault( ), "%d", mProfile.getAge( ) ) );

        Task t = ImageController.getProfilePicture( mProfile.getUserID( ) );
        t.addOnSuccessListener( new OnSuccessListener<byte[]>( ) {
            @Override
            public void onSuccess( byte[] bytes ) {
                Bitmap bmp = PictureConversion.byteArrayToBitmap( bytes );
                mPhoto.setImageBitmap( bmp );
            }
        } );
    }
}
