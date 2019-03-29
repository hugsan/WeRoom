package com.itcom202.weroom.account.profiles;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.itcom202.weroom.R;
import com.itcom202.weroom.account.authentification.ForgotPasswordActivity;

public class ProfileTenantFragment extends Fragment {
    private Spinner mSmoking;
    private Spinner mChooseCity;
    private Spinner mPeriodRenting;
    private Spinner mLandlordGender;
    private Spinner mLandlordNation;
    private Spinner mPetFriendly;
    private CheckBox mIsFurnished;
    private CheckBox mHasInternet;
    private CheckBox mHandicap;
    private CheckBox mHasLaundry;
    private EditText mDepositMin;
    private EditText mDespotiMax;
    private EditText mRentMin;
    private EditText mLandlordAgeMin;
    private EditText mLandlordAgeMax;
    private SeekBar  mDistanceFromCenter;
    private Button mConfirm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_tenant_fragment, null,false);

        mSmoking = v.findViewById(R.id.smokingFriendly);
        mSmoking.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(ForgotPasswordActivity.newIntent(getActivity()));
            }
        });


        return v;
    }
}
