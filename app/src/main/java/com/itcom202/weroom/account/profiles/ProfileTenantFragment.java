package com.itcom202.weroom.account.profiles;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itcom202.weroom.R;
import com.itcom202.weroom.account.authentification.ForgotPasswordActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    private EditText mDepositMax;
    private EditText mRentMin;
    private EditText mRentMax;
    private EditText mLandlordAgeMin;
    private EditText mLandlordAgeMax;
    private SeekBar  mDistanceFromCenter;
    private Button mConfirm;

    private DatabaseReference mDatabaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_tenant_fragment, null,false);
        mLandlordNation = v.findViewById(R.id.spinnerNationalityLL);



        mLandlordNation.setAdapter(countryAdapter());
        mSmoking = v.findViewById(R.id.spinnerSmoking);
        mChooseCity = v.findViewById(R.id.spinnerChooseCity);
        mPeriodRenting = v.findViewById(R.id.spinnerPeriodRenting);
        mLandlordGender = v.findViewById(R.id.spinnerGender);
        mLandlordNation = v.findViewById(R.id.spinnerNationalityLL);
        mPetFriendly = v.findViewById(R.id.spinnerPetFriendly);
        mIsFurnished = v.findViewById(R.id.furnished);
        mHasInternet = v.findViewById(R.id.internet);
        mHandicap = v.findViewById(R.id.handicap);
        mHasLaundry = v.findViewById(R.id.laundry);
        mDepositMin = v.findViewById(R.id.depositMin);
        mDepositMax = v.findViewById(R.id.depositMax);
        mRentMin = v.findViewById(R.id.rentMin);
        mRentMax = v.findViewById(R.id.rentMax);
        mLandlordAgeMin = v.findViewById(R.id.ageMinL);
        mLandlordAgeMax = v.findViewById(R.id.ageMaxL);
        mDistanceFromCenter = v.findViewById(R.id.radiusCenter);
        mConfirm = v.findViewById(R.id.confirm);


        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                /*String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                TenantProfile newTenant = new TenantProfile.Builder(userID)*/






               /* mDatabaseReference
                        .child(DataBasePath.USERS.getValue())
                        .child(userID)
                        .child(DataBasePath.PROFILE.getValue())
                        .child(DataBasePath.TENANT.getValue())
                        .setValue(newTenant);*/


            }
        });



        return v;
    }
    private SpinnerAdapter countryAdapter(){
        String[] locales = Locale.getISOCountries();
        List<String> countries = new ArrayList<>();
        countries.add(getString(R.string.prompt_country));



        for (String countryCode : locales) {

            Locale obj = new Locale("", countryCode);

            countries.add(obj.getDisplayCountry());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
}
