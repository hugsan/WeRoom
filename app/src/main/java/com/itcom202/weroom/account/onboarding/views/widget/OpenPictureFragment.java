package com.itcom202.weroom.account.onboarding.views.widget;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.itcom202.weroom.R;
import com.itcom202.weroom.framework.SingleFragment;
import com.itcom202.weroom.framework.cameraandgallery.PictureConversion;


public class OpenPictureFragment extends SingleFragment {

    private String TAG ="OpenPictureFragment";

    private ImageView mOpenedPicture;
    private ImageButton mButtonClose;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.open_picture_fragment, null, false);

        mOpenedPicture = v.findViewById(R.id.openedPicture);
        mButtonClose = v.findViewById(R.id.imageButtonClose);

        Bundle bundle = getArguments();

        byte[] obj = (byte[]) bundle.getSerializable("picture");
        mOpenedPicture.setImageBitmap(PictureConversion.byteArrayToBitmap(obj));
        mButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    assert getFragmentManager() != null;
                    getFragmentManager().popBackStack();
                }
                catch(Exception e) {
                    Log.w(TAG,"popBackStack not successful");
                }
            }
        });

        return v;

    }

}
