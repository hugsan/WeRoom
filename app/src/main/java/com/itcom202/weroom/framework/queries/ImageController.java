package com.itcom202.weroom.framework.queries;

import android.graphics.Bitmap;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.itcom202.weroom.framework.DataBasePath;
import com.itcom202.weroom.framework.cameraandgallery.PictureConversion;

import java.io.Serializable;

public class ImageController implements Serializable {

    private static byte[] byteArray;

    public static void setProfilePicture (String userID, Bitmap bmp){
        StorageReference reference = FirebaseStorage.getInstance().getReference();

        reference
                .child(DataBasePath.IMAGE.getValue())
                .child(userID)
                .child(DataBasePath.PROFILE_PICTURE.getValue())
                .putBytes(PictureConversion.bitmapToByteArray(bmp));
    }

    public static Task getProfilePicture(String userID){

        StorageReference reference = FirebaseStorage.getInstance().getReference();
        StorageReference downloadRef = reference
                .child(DataBasePath.IMAGE.getValue())
                .child(userID)
                .child(DataBasePath.PROFILE_PICTURE.getValue());

        Task t = downloadRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                byteArray = bytes;
            }
        });
        return t;
    }

    public static void setRoomPicture (String roomID, Bitmap bmp, int roomNumber){
        StorageReference reference = FirebaseStorage.getInstance().getReference();

        reference
                .child(DataBasePath.IMAGE.getValue())
                .child(roomID)
                .child(DataBasePath.ROOM_PICTURE.getValue() +"_"+ roomNumber)
                .putBytes(PictureConversion.bitmapToByteArray(bmp));
    }

    public static Task getRoomPicture(String roomID, int roomNumber){
        StorageReference reference = FirebaseStorage.getInstance().getReference();
        StorageReference downloadRef = reference
                .child(DataBasePath.IMAGE.getValue())
                .child(roomID)
                .child(DataBasePath.ROOM_PICTURE.getValue() +"_"+ roomNumber);


        Task t = downloadRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                byteArray = bytes;
            }
        });
        return t;
    }
    public static Task[] getAllRoomPicture(String roomID){
        Task[] arrayTask = new Task[10];
        for (int i = 0 ; i < 10 ; i++){
            arrayTask[i] = getRoomPicture(roomID, i);
        }
        return arrayTask;
    }
    public static void removeRoomPicture(String roomID, int roomNumber){
        StorageReference reference = FirebaseStorage.getInstance().getReference();
        reference.child(DataBasePath.IMAGE.getValue())
                .child(roomID)
                .child(DataBasePath.ROOM_PICTURE.getValue() +"_"+ roomNumber)
                .delete();

    }
    public static void removeAllRoomPictures(String roomID){
        for (int i = 0 ; i < 10 ; i++){
            removeRoomPicture(roomID,i);
        }
    }

}
