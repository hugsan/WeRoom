package com.itcom202.weroom.queries;

import android.graphics.Bitmap;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.itcom202.weroom.account.profiles.DataBasePath;
import com.itcom202.weroom.cameraGallery.PictureConversion;

public class ImageController {

    private static byte[] byteArray;

    public static void setProfilePicture (String userID, Bitmap bmp){
        StorageReference reference = FirebaseStorage.getInstance().getReference();

        reference
                .child(DataBasePath.IMAGE.getValue())
                .child(userID)
                .child(DataBasePath.PROFILE_PICTURE.getValue())
                .putBytes(PictureConversion.bitmapToByteArray(bmp));
    }

    public static Bitmap getProfilePicture(String userID){

        StorageReference reference = FirebaseStorage.getInstance().getReference();
        StorageReference downloadRef = reference
                .child(DataBasePath.IMAGE.getValue())
                .child(userID)
                .child(DataBasePath.PROFILE_PICTURE.getValue());

        downloadRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                byteArray = bytes;
            }
        });
        return PictureConversion.byteArrayToBitmap(byteArray);
    }

    public static void setRoomPicture (String roomID, Bitmap bmp, int roomNumber){
        StorageReference reference = FirebaseStorage.getInstance().getReference();

        reference
                .child(DataBasePath.IMAGE.getValue())
                .child(roomID)
                .child(DataBasePath.ROOM_PICTURE.getValue() +"_"+ Integer.toString(roomNumber))
                .putBytes(PictureConversion.bitmapToByteArray(bmp));
    }

    public static Bitmap getRoomPicture(String userID, int roomNumber){
        StorageReference reference = FirebaseStorage.getInstance().getReference();
        StorageReference downloadRef = reference
                .child(DataBasePath.IMAGE.getValue())
                .child(userID)
                .child(DataBasePath.ROOM_PICTURE.getValue() +"_"+ Integer.toString(roomNumber));

        downloadRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                byteArray = bytes;
            }
        });
        if (byteArray.length != 0)
            return PictureConversion.byteArrayToBitmap(byteArray);
        else
            return null;
    }
}
