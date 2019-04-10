package com.itcom202.weroom.swipe;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.itcom202.weroom.R;

public class CardInfoFragment extends Fragment {
    ImageButton mButtonExit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_card_info, null, false);

        mButtonExit = v.findViewById(R.id.button_exit_info_page);
        mButtonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AppCompatActivity activity = (AppCompatActivity) mButtonExit.getContext();
                activity.getSupportFragmentManager()
                        .popBackStackImmediate();
            }
        });


        return v;
    }

}