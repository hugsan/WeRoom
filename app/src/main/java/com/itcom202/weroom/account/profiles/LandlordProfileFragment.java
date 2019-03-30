package com.itcom202.weroom.account.profiles;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import com.itcom202.weroom.account.profiles.LandlordProfile;

import com.google.firebase.auth.FirebaseAuth;
import com.itcom202.weroom.R;

public class LandlordProfileFragment extends Fragment {

    private Spinner mTenantNation;
    private EditText mTenantMinAge;
    private EditText mTenantMaxAge;
    private Spinner mTenantGender;
    private Spinner mTenantOccupation;
    private RadioGroup mSocialGroup;
    private RadioGroup mSmokingGroup;
    private Button mConfirm;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_landlord_fragment, null,false);

        mTenantNation = v.findViewById(R.id.spinnerNationalityLL);
        mTenantMinAge = v.findViewById(R.id.ageMinT);
        mTenantMaxAge = v.findViewById(R.id.ageMaxT);
        mTenantGender = v.findViewById(R.id.spinnerGenderLL);
        mTenantOccupation = v.findViewById(R.id.occupation);
        mSocialGroup = v.findViewById(R.id.socialgroup);
        mSmokingGroup = v.findViewById(R.id.smokinggroup);
        mConfirm = v.findViewById(R.id.confirmtenantprofile);


        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();


                int social = mSocialGroup.getCheckedRadioButtonId();
                int smoking = mSmokingGroup.getCheckedRadioButtonId();

                LandlordProfile newInput = new LandlordProfile.Builder(userID)
                        .withTenantNationallity(String.valueOf(mTenantNation.getSelectedItem()))
                        .withTenantAge(Integer.parseInt(mTenantMinAge.getText().toString()), Integer.parseInt(mTenantMaxAge.getText().toString()))
                        .withTenantGender(String.valueOf(mTenantGender.getSelectedItem()))
                        .withTenantOccupation(String.valueOf(mTenantOccupation.getSelectedItem()))
                        .tenantSocial(v.findViewById(social).toString())
                        .canTenantSmoke(v.findViewById(smoking).toString())
                        .build();

            }
        });



        return  v;


    }
}
