package com.itcom202.weroom.swipe;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.itcom202.weroom.R;
import com.itcom202.weroom.account.profiles.Profile;

public class LandlordState extends RecyclerView.ViewHolder implements State {
    TextView textView;
    ImageView mPhoto;

    public LandlordState(@NonNull final View itemView) {
        super(itemView);

        textView = itemView.findViewById(R.id.text);

        mPhoto = itemView.findViewById(R.id.photoCard);
        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) itemView.getContext();
                Fragment myFragment = new CardInfoFragment();
                activity.getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, myFragment).addToBackStack(null).commit();
            }
        });


    }

    public void bind(Profile profile) {
        textView.setText(profile.getName());
        mPhoto.setImageResource(R.drawable.add_profile_picture);


    }

    @Override
    public void changeTypeProfile() {

    }
}
