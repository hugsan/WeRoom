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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itcom202.weroom.account.profiles.LandlordProfile;

import com.google.firebase.auth.FirebaseAuth;
import com.itcom202.weroom.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LandlordProfileFragment extends Fragment {

    private Spinner mTenantNation;
    private EditText mTenantMinAge;
    private EditText mTenantMaxAge;
    private Spinner mTenantGender;
    private Spinner mTenantOccupation;
    private RadioGroup mSocialGroup;
    private RadioGroup mSmokingGroup;
    private Button mConfirm;
    private String socialValue = LandlordProfile.I_DONT_CARE;
    private String smokingValue = LandlordProfile.I_DONT_CARE;
    private DatabaseReference mDatabaseReference;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_landlord_fragment, null,false);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mTenantNation = v.findViewById(R.id.spinnerNationalityLLlandlordfragment);
        mTenantMinAge = v.findViewById(R.id.ageMinT);
        mTenantMaxAge = v.findViewById(R.id.ageMaxT);
        mTenantGender = v.findViewById(R.id.spinnerGenderLL);
        mTenantOccupation = v.findViewById(R.id.occupation);
        mSocialGroup = v.findViewById(R.id.socialgroup);
        mSmokingGroup = v.findViewById(R.id.smokinggroup);
        mConfirm = v.findViewById(R.id.confirmtenantprofile);


        mSocialGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radiossocialyes:
                        socialValue = LandlordProfile.YES;
                        break;
                    case R.id.radiosocialno:
                        socialValue = LandlordProfile.NO;
                        break;
                    case R.id.radiosocialidc:
                        socialValue = LandlordProfile.I_DONT_CARE;
                        break;
                }
            }
        });
        mSmokingGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radiosmokeyes:
                        smokingValue = LandlordProfile.YES;
                        break;
                    case R.id.radiosmokeno:
                        smokingValue = LandlordProfile.NO;
                        break;
                    case R.id.radiosmokeidc:
                        smokingValue = LandlordProfile.I_DONT_CARE;
                        break;
                }
            }
        });
        mTenantNation.setAdapter(countryAdapter());

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                boolean noError = true;
                if (mTenantMinAge.getText().toString().length()==0)
                {

                    mTenantMinAge.setText("16");
                }
                if (Integer.parseInt(mTenantMinAge.getText().toString()) < 16)
                {
                    mTenantMinAge.setError(getString(R.string.min_age));
                    mTenantMinAge.requestFocus();
                    mTenantMinAge.setText("16");
                    noError = false;
                }
                if (mTenantMaxAge.getText().toString().length()==0)
                {
                    mTenantMaxAge.setText("120");
                }
                if (Integer.parseInt(mTenantMaxAge.getText().toString()) > 120)
                {
                    mTenantMaxAge.setError(getString(R.string.max_age));
                    mTenantMaxAge.requestFocus();
                    mTenantMaxAge.setText("120");
                    noError = false;
                }


                if (noError){
                    LandlordProfile newInput = new LandlordProfile.Builder(userID)
                            .withTenantNationallity(String.valueOf(mTenantNation.getSelectedItem()))
                            .withTenantAge(Integer.parseInt(mTenantMinAge.getText().toString()), Integer.parseInt(mTenantMaxAge.getText().toString()))
                            .withTenantGender(String.valueOf(mTenantGender.getSelectedItem()))
                            .withTenantOccupation(String.valueOf(mTenantOccupation.getSelectedItem()))
                            .tenantSocial(socialValue)
                            .canTenantSmoke(smokingValue)
                            .build();
                    mDatabaseReference
                            .child(DataBasePath.USERS.getValue())
                            .child(userID)
                            .child(DataBasePath.PROFILE.getValue())
                            .child(DataBasePath.LANDLORD.getValue())
                            .setValue(newInput);



                }

            }
        });



        return  v;


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
