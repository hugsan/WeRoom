package com.itcom202.weroom.account.onboarding.views.widget;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.itcom202.weroom.R;
import com.itcom202.weroom.framework.SingleFragment;
import com.itcom202.weroom.framework.cameraandgallery.PictureConversion;

/**
 * Fragment used to display a picture on a Fragment.
 */
public class OpenPictureFragment extends SingleFragment {

    public static final String KEY_PICTURE = "picture";
    static final int REQUEST_CODE = 123;
    private String TAG = "OpenPictureFragment";
    private ImageView mOpenedPicture;

    @Nullable
    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState ) {
        View v = inflater.inflate( R.layout.open_picture_fragment, container, false );

        mOpenedPicture = v.findViewById( R.id.openedPicture );
        Bundle bundle = getArguments( );

        byte[] obj = new byte[ 0 ];
        if ( bundle != null ) {
            obj = ( byte[] ) bundle.getSerializable( KEY_PICTURE );
        }
        mOpenedPicture.setImageBitmap( PictureConversion.byteArrayToBitmap( obj ) );

        return v;
    }
}
