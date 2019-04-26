package com.itcom202.weroom.swipe;

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
import com.itcom202.weroom.account.profiles.Profile;
import com.itcom202.weroom.cameraGallery.PictureConversion;
import com.itcom202.weroom.queries.ImageController;

public class LandlordState extends RecyclerView.ViewHolder implements State {
    private TextView textView;
    private ImageView mPhoto;
    private Profile  mProfile;
    public static final String KEY_TENANT = "KEY_TENANT";


    public LandlordState(@NonNull final View itemView) {
        super(itemView);

        textView = itemView.findViewById(R.id.text);

        mPhoto = itemView.findViewById(R.id.photoCard);
        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) itemView.getContext();
                Fragment myFragment = CardInfoTenantFragment.newInstance(mProfile);
                activity.getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, myFragment).addToBackStack(null).commit();
            }
        });


    }

    public void bind(Profile profile) {

        textView.setText(profile.getName());
        Task t = ImageController.getProfilePicture(profile.getUserID());
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
