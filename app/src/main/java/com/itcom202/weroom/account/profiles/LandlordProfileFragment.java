package com.itcom202.weroom.account.profiles;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itcom202.weroom.SingleFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.itcom202.weroom.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class LandlordProfileFragment extends SingleFragment {

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


        ArrayAdapter adapterGenderTenant = ArrayAdapter.createFromResource(getActivity(), R.array.gender_array, R.layout.spinner_item);
        adapterGenderTenant.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTenantGender.setAdapter(adapterGenderTenant);

        ArrayAdapter adapterOccup = ArrayAdapter.createFromResource(getActivity(), R.array.occupation, R.layout.spinner_item);
        adapterOccup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTenantOccupation.setAdapter(adapterOccup);


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

                    mTenantMinAge.setText("18");
                }
                if (Integer.parseInt(mTenantMinAge.getText().toString()) < 16)
                {
                    mTenantMinAge.setError(getString(R.string.min_age));
                    mTenantMinAge.requestFocus();
                    mTenantMinAge.setText("18");
                    noError = false;
                }
                if (mTenantMaxAge.getText().toString().length()==0)
                {
                    mTenantMaxAge.setText("99");
                }
                if (Integer.parseInt(mTenantMaxAge.getText().toString()) > 120)
                {
                    mTenantMaxAge.setError(getString(R.string.max_age));
                    mTenantMaxAge.requestFocus();
                    mTenantMaxAge.setText("99");
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

                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    db.collection(DataBasePath.USERS.getValue())
                            .document(userID)
                            .update(DataBasePath.LANDLORD.getValue(),newInput);

                    changeFragment(new RoomCreationFragment());
                }

            }
        });



        return  v;


    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }


    private SpinnerAdapter countryAdapter(){
        String[] locales = Locale.getISOCountries();
        List<String> countries = new ArrayList<>();
        countries.add(getString(R.string.prompt_country));



        for (String countryCode : locales) {

            Locale obj = new Locale("", countryCode);

            countries.add(obj.getDisplayCountry());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item , countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
}
