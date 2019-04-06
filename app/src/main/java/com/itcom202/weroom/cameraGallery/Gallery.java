package com.itcom202.weroom.cameraGallery;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;

import java.io.File;

import static com.itcom202.weroom.cameraGallery.Camera.currentPhotoPath;

public class Gallery{
        public static void pickFromGallery(Activity activity, Fragment fragment) {
            //Create an Intent with action as ACTION_PICK
            Intent intent = new Intent(Intent.ACTION_PICK);
            // Sets the type as image/*. This ensures only components of type image are selected
            intent.setType("image/*");
            //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
            String[] mimeTypes = {"image/jpeg", "image/png"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            fragment.startActivityForResult(intent, GALLERY_REQUEST_CODE);
        }

        private void galleryAddPic(Activity activity) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(currentPhotoPath);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            activity.sendBroadcast(mediaScanIntent);
        }

        private static final int GALLERY_REQUEST_CODE = 1 ;
}
