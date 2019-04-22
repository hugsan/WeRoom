package com.itcom202.weroom.swipe;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.itcom202.weroom.R;
import com.itcom202.weroom.account.profiles.RoomPosted;
import com.itcom202.weroom.cameraGallery.PictureConversion;
import com.itcom202.weroom.queries.ImageController;

import java.util.zip.Inflater;

public class TenantState extends RecyclerView.ViewHolder implements State {
    TextView textView;
    ImageView mPhoto;

    public TenantState(@NonNull final View itemView) {
        super(itemView);

        textView = itemView.findViewById(R.id.textTenantCard);

        mPhoto = itemView.findViewById(R.id.photoCardTenant);
        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) itemView.getContext();
                Fragment myFragment = new CardInfoFragment();
                activity.getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, myFragment).addToBackStack(null).commit();
            }
        });

    }

    public void bind(RoomPosted roomPosted) {
        textView.setText(roomPosted.getCompleteAddress());
        Task t = ImageController.getRoomPicture(roomPosted.getRoomID(),0);
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
