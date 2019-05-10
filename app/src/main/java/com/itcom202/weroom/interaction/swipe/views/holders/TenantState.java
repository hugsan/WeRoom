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

public class TenantState extends RecyclerView.ViewHolder implements State {
    private TextView textView;
    private ImageView mPhoto;
    private RoomPosted room;

    public TenantState(@NonNull final View itemView) {
        super(itemView);

        textView = itemView.findViewById(R.id.textTenantCard);

        mPhoto = itemView.findViewById(R.id.photoCardTenant);
        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity) itemView.getContext();
                Fragment myFragment = CardInfoRoomFragment.newInstance(room);
                activity.getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_top, myFragment).addToBackStack(null).commit();
            }
        });

    }

    public void bind(RoomPosted roomPosted) {
        room = roomPosted;
        textView.setText(room.getCompleteAddress());
        Task t = ImageController.getRoomPicture(room.getRoomID(),0);
        t.addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                mPhoto.setImageBitmap(PictureConversion.byteArrayToBitmap(bytes));
            }
        });


    }
    @Override
    public void changeTypeProfile() {


    }

}
