package com.itcom202.weroom.account.profiles;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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

    private int mDistanceFromCenterValue;

    private DatabaseReference mDatabaseReference;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_tenant_fragment, null,false);



        mLandlordNation = v.findViewById(R.id.spinnerNationalityLL);
        mSmoking = v.findViewById(R.id.spinnerSmoking);
        mChooseCity = v.findViewById(R.id.spinnerChooseCity);
        mPeriodRenting = v.findViewById(R.id.spinnerPeriodRenting);
        mLandlordGender = v.findViewById(R.id.spinnerGender);
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

        mLandlordNation.setAdapter(countryAdapter());

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mDistanceFromCenter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                mDistanceFromCenterValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        mConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                Log.d("Tortuga", String.valueOf(mSmoking.getSelectedItem()));
                TenantProfile newInput = new TenantProfile.Builder(userID)
                        .withLandlordNationallity(String.valueOf(mLandlordNation.getSelectedItem()))
                        .isSmokingFriendly(String.valueOf(mSmoking.getSelectedItem()))
                        .withCity(String.valueOf(mChooseCity.getSelectedItem()))
                        .withRentingPeriod(mPeriodRenting.getSelectedItemPosition())
                        .withLandlordGender(("Female".equals(String.valueOf(mLandlordGender.getSelectedItem()))) ? 'F' : 'M')
                        .isPetFriendly(String.valueOf(mPetFriendly.getSelectedItem()))
                        .isFurnished(mIsFurnished.isChecked())
                        .hasInternet(mHasInternet.isChecked())
                        .isHandicapFriendly(mHandicap.isChecked())
                        .hasLaundry(mHasLaundry.isChecked())
                        .withDepositRange(Integer.parseInt(mDepositMin.getText().toString()), Integer.parseInt(mDepositMax.getText().toString()))
                        .withRentRange(Integer.parseInt(mRentMin.getText().toString()), Integer.parseInt(mRentMax.getText().toString()))
                        .withLandlordAgeRange(Integer.parseInt(mLandlordAgeMin.getText().toString()), Integer.parseInt(mLandlordAgeMax.getText().toString()))
                        .distanceFromCenter(mDistanceFromCenterValue)
                        .build();






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
