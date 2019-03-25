package com.itcom202.weroom;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.itcom202.weroom.account.authentification.LoginActivity;
import com.itcom202.weroom.account.authentification.LoginFragment;

public class AccountCreationFragment extends Fragment {
    Button mLogoutButton;
    FirebaseAuth mFirebaseAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.account_creation_fragment, container, false);
        mLogoutButton = v.findViewById(R.id.logout_button);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    mFirebaseAuth.signOut();
                    startActivity(LoginActivity.newIntent(getActivity()));
                }catch (Exception e){
                    Toast.makeText(getActivity(), String.valueOf(R.string.error_logout), Toast.LENGTH_LONG).show();
                }
            }
        });
        return v;
    }
}
