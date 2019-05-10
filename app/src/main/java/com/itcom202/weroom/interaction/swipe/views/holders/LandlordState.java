package com.itcom202.weroom.interaction.swipe.views.holders;

import android.graphics.Bitmap;
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
import com.itcom202.weroom.account.models.Profile;
import com.itcom202.weroom.framework.cameraandgallery.PictureConversion;
import com.itcom202.weroom.framework.queries.ImageController;
import com.itcom202.weroom.interaction.swipe.views.CardInfoTenantFragment;

public class LandlordState extends RecyclerView.ViewHolder implements State {
    private TextView textView;
    private ImageView mPhoto;
    private Profile  mProfile;


    public LandlordState(@NonNull final View itemView) {
        super(itemView);

        textView = itemView.findViewById(R.id.text);

        mPhoto = itemView.findViewById(R.id.photoCard);
        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) itemView.getContext();
                Fragment myFragment = CardInfoTenantFragment.newInstance(mProfile);
                activity.getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_top, myFragment).addToBackStack(null).commit();
            }
        });


    }

    public void bind(Profile profile) {
        mProfile = profile;
        textView.setText(mProfile.getName());
        Task t = ImageController.getProfilePicture(mProfile.getUserID());
        t.addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = PictureConversion.byteArrayToBitmap(bytes);
                mPhoto.setImageBitmap(bmp);
            }
        });

    }

    @Override
    public void changeTypeProfile() {

    }
}
