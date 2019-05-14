package com.itcom202.weroom.interaction.swipe.views.holders;

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
import com.itcom202.weroom.account.models.RoomPosted;
import com.itcom202.weroom.framework.cameraandgallery.PictureConversion;
import com.itcom202.weroom.framework.queries.ImageController;
import com.itcom202.weroom.interaction.swipe.views.CardInfoRoomFragment;

import java.util.Locale;

/**
 * Holder of landlord for the SwipeFragment
 */
public class RoomHolder extends RecyclerView.ViewHolder {
    private TextView mAddressRoom;
    private TextView mRentRoom;
    private TextView mSizeRoom;
    private ImageView mPhoto;
    private RoomPosted room;

    public RoomHolder( @NonNull final View itemView ) {
        super( itemView );

        mAddressRoom = itemView.findViewById( R.id.textTenantCard );
        mRentRoom = itemView.findViewById( R.id.rentOfRoom );
        mSizeRoom = itemView.findViewById( R.id.card_room_size );

        mPhoto = itemView.findViewById( R.id.photoCardTenant );
        mPhoto.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick( View v ) {
                AppCompatActivity activity = ( AppCompatActivity ) itemView.getContext( );
                Fragment myFragment = CardInfoRoomFragment.newInstance( room );
                activity.getSupportFragmentManager( ).beginTransaction( ).add( R.id.fragment_container_top, myFragment ).addToBackStack( null ).commit( );
            }
        } );

    }

    /**
     * method that binds the holder with a roomPosted
     *
     * @param roomPosted Room to be binded to the holder.
     */
    public void bind( RoomPosted roomPosted ) {
        room = roomPosted;
        mAddressRoom.setText( room.getCompleteAddress( ) );
        mRentRoom.setText( String.format( Locale.getDefault( ), "%d", room.getRent( ) ) );
        mSizeRoom.setText( String.format( Locale.getDefault( ), "%d", room.getSize( ) ) );
        Task t = ImageController.getRoomPicture( room.getRoomID( ), 0 );
        t.addOnSuccessListener( new OnSuccessListener<byte[]>( ) {
            @Override
            public void onSuccess( byte[] bytes ) {
                mPhoto.setImageBitmap( PictureConversion.byteArrayToBitmap( bytes ) );
            }
        } );
    }
}
