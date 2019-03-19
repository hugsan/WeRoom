package com.itcom202.weroom.account.profiles;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itcom202.weroom.R;
import android.widget.Button;


public class ProfileFragment extends Fragment {
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mUser;
    private EditText mUserName;
    private EditText mAge;
    private Button mCreateProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_fragment,container,false);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        mUserName = v.findViewById(R.id.username);
        mAge = v.findViewById(R.id.age);

        mCreateProfile = v.findViewById(R.id.createprofile);

        mCreateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile myProfile =
                        new Profile(mUserName.getText().toString(), Integer.parseInt(mAge.getText().toString()));
                mDatabaseReference
                        .child(DataBasePath.USERS)
                        .child(mUser.getUid())
                        .child(DataBasePath.PROFILE)
                        .setValue(myProfile);
            }
        });

        return v;

    }
}
