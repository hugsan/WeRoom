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
        mLandlordNation = v.findViewById(R.id.spinnerNationalityLL);



        mLandlordNation.setAdapter(countryAdapter());



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
