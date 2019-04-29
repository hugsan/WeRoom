package com.itcom202.weroom.account.profiles;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.itcom202.weroom.R;
import com.itcom202.weroom.SingleFragment;
import com.itcom202.weroom.cameraGallery.ImagePicker;
import com.itcom202.weroom.cameraGallery.PictureConversion;
import com.itcom202.weroom.queries.ImageController;

import java.io.IOException;



import static android.app.Activity.RESULT_OK;
import static android.support.v4.content.ContextCompat.getSystemService;

public class OpenPictureFragment extends SingleFragment {

    private String TAG ="OpenPictureFragment";
    static final int REQUEST_CODE = 123;

    private ImageView mOpenedPicture;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.open_picture_fragment, null, false);

        mOpenedPicture = v.findViewById(R.id.openedPicture);
        // System.out.println( ImageController.getRoomPicture(FirebaseAuth.getInstance().getUid(),0));

        Bundle bundle = getArguments();

        byte[] obj = (byte[]) bundle.getSerializable("picture");
        mOpenedPicture.setImageBitmap(PictureConversion.byteArrayToBitmap(obj));


        //TODO rotation of picture


       // if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)



       // if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            // mOpenedPicture.setImageBitmap(ImageController.getRoomPicture(FirebaseAuth.getInstance().getUid(),0).);
//        OrientationEventListener mOrientationEventListener = new OrientationEventListener(getContext(), SensorManager.SENSOR_DELAY_NORMAL) {
//            @Override
//            public void onOrientationChanged(int orientation) {
//                mDeviceOrientation = orientation;
//            }
//        };
//
//        if (mOrientationEventListener.canDetectOrientation()) {
//            mOrientationEventListener.enable();
//        }
//
//        int orientation = 90*Math.round(mDeviceOrientation / 90);
//
//// Convert 360 to 0
//        if(orientation == 360)
//        {
//            orientation = 0;
//        }




        return v;

    }

//    private void getScreenRotationOnPhone() {
//
//        final Display display = ((WindowManager) getSystemService(getContext(),TAG)).getDefaultDisplay();
//
//        switch (display.getRotation()) {
//            case Surface.ROTATION_0:
//                System.out.println("SCREEN_ORIENTATION_PORTRAIT");
//                break;
//
//            case Surface.ROTATION_90:
//                System.out.println("SCREEN_ORIENTATION_LANDSCAPE");
//                break;
//
//            case Surface.ROTATION_180:
//                System.out.println("SCREEN_ORIENTATION_REVERSE_PORTRAIT");
//                break;
//
//            case Surface.ROTATION_270:
//                System.out.println("SCREEN_ORIENTATION_REVERSE_LANDSCAPE");
//                break;
//        }
//    }
//
//    private void getScreenRotationOnTablet() {
//
//        final Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
//
//        switch (display.getRotation()) {
//            case Surface.ROTATION_0:
//                System.out.println("SCREEN_ORIENTATION_LANDSCAPE");
//                break;
//
//            case Surface.ROTATION_90:
//                System.out.println("SCREEN_ORIENTATION_REVERSE_PORTRAIT");
//                break;
//
//            case Surface.ROTATION_180:
//                System.out.println("SCREEN_ORIENTATION_REVERSE_LANDSCAPE");
//                break;
//
//            case Surface.ROTATION_270:
//                System.out.println("SCREEN_ORIENTATION_PORTRAIT");
//                break;
//        }
//    }
//
//    private boolean isTabletDevice(){
//        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
//        if (tabletSize) {
//            return true;
//        } else {
//            return false;
//        }
//    }


}
