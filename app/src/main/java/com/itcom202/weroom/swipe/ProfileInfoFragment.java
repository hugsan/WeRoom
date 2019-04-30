package com.itcom202.weroom.swipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.itcom202.weroom.MainActivity;
import com.itcom202.weroom.R;


public class ProfileInfoFragment extends Fragment {
    private Button mLogoutButton;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_info, null, false);
        mLogoutButton = v.findViewById(R.id.profile_logout_button);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                //TODO this is not working properlu, we should go out to MainActivity to log again.
                startActivity(MainActivity.newIntent(getActivity()));
            }
        });

        return v;
    }
}
