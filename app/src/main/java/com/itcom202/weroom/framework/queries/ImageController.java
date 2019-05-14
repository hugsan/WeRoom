package com.itcom202.weroom.framework.queries;

import android.graphics.Bitmap;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.itcom202.weroom.framework.DataBasePath;
import com.itcom202.weroom.framework.cameraandgallery.PictureConversion;

import java.io.Serializable;

/**
 * Class that implements different static method used to communicate with FirebaseStorage.
 * <p>
 * Contains queries to obtain and update pictures from profiles and rooms.
 */
public class ImageController implements Serializable {

    private static byte[] byteArray;

    /**
     * Upload to FirebaseStorage a bitmap to from the user.
     *
     * @param userID ID of the user we want to store the picture.
     * @param bmp    bitmap that we want to upload.
     */
    public static void setProfilePicture( String userID, Bitmap bmp ) {
        StorageReference reference = FirebaseStorage.getInstance( ).getReference( );

        reference
                .child( DataBasePath.IMAGE.getValue( ) )
                .child( userID )
                .child( DataBasePath.PROFILE_PICTURE.getValue( ) )
                .putBytes( PictureConversion.bitmapToByteArray( bmp ) );
    }

    /**
     * Gets the task from the query that search for the profile picture from a specific user.
     *
     * @param userID ID of the user we want to get the picture.
     * @return Task related to the query from FirebaseStorage.
     */
    public static Task getProfilePicture( String userID ) {

        StorageReference reference = FirebaseStorage.getInstance( ).getReference( );
        StorageReference downloadRef = reference
                .child( DataBasePath.IMAGE.getValue( ) )
                .child( userID )
                .child( DataBasePath.PROFILE_PICTURE.getValue( ) );

        return downloadRef.getBytes( Long.MAX_VALUE ).addOnSuccessListener( new OnSuccessListener<byte[]>( ) {
            @Override
            public void onSuccess( byte[] bytes ) {
                byteArray = bytes;
            }
        } );
    }

    /**
     * Upload picture bitmap picture from a room into FirebaseStorage.
     *
     * @param roomID     ID of the room we want to upload the picture.
     * @param bmp        bitmap we want to upload into FirebaseStorage.
     * @param roomNumber Picture number (0 to 9)
     */
    public static void setRoomPicture( String roomID, Bitmap bmp, int roomNumber ) {
        StorageReference reference = FirebaseStorage.getInstance( ).getReference( );

        reference
                .child( DataBasePath.IMAGE.getValue( ) )
                .child( roomID )
                .child( DataBasePath.ROOM_PICTURE.getValue( ) + "_" + roomNumber )
                .putBytes( PictureConversion.bitmapToByteArray( bmp ) );
    }

    /**
     * Get a task related to the query of getting a picture of a room.
     *
     * @param roomID     ID of the room we want to get the picture.
     * @param roomNumber number of the picture (from 0 to 9)
     * @return task related to the query that perform the getBytes() to the database.
     */
    public static Task getRoomPicture( String roomID, int roomNumber ) {
        StorageReference reference = FirebaseStorage.getInstance( ).getReference( );
        StorageReference downloadRef = reference
                .child( DataBasePath.IMAGE.getValue( ) )
                .child( roomID )
                .child( DataBasePath.ROOM_PICTURE.getValue( ) + "_" + roomNumber );


        return downloadRef.getBytes( Long.MAX_VALUE ).addOnSuccessListener( new OnSuccessListener<byte[]>( ) {
            @Override
            public void onSuccess( byte[] bytes ) {
                byteArray = bytes;
            }
        } );
    }

    /**
     * Gets a task referring to each of the rooms pictures for a specific Room.
     *
     * @param roomID ID of the room we want to get the pictures.
     * @return Task array that contains each of it a task for a query to each picture.
     */
    public static Task[] getAllRoomPicture( String roomID ) {
        Task[] arrayTask = new Task[ 10 ];
        for ( int i = 0 ; i < 10 ; i++ ) {
            arrayTask[ i ] = getRoomPicture( roomID, i );
        }
        return arrayTask;
    }

    /**
     * Delete a specific room picture from a Room.
     *
     * @param roomID     ID of the room we want to delete a picture.
     * @param roomNumber number of the picture we want to delete from FirebaseStorage
     */
    public static void removeRoomPicture( String roomID, int roomNumber ) {
        StorageReference reference = FirebaseStorage.getInstance( ).getReference( );
        reference.child( DataBasePath.IMAGE.getValue( ) )
                .child( roomID )
                .child( DataBasePath.ROOM_PICTURE.getValue( ) + "_" + roomNumber )
                .delete( );

    }

    /**
     * Deletes all the pictures related to a Room.
     *
     * @param roomID ID of the room we want to delete all the pictures from.
     */
    public static void removeAllRoomPictures( String roomID ) {
        for ( int i = 0 ; i < 10 ; i++ ) {
            removeRoomPicture( roomID, i );
        }
    }

}
