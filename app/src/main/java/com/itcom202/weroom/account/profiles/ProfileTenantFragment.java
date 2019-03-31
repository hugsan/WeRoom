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
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itcom202.weroom.R;
import com.itcom202.weroom.account.profiles.SeekBar.BubbleSeekBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.google.android.libraries.places.api.Places;
// Add import statements for the new library.
import com.google.android.libraries.places.api.net.PlacesClient;


import static com.facebook.FacebookSdk.getApplicationContext;

public class ProfileTenantFragment extends Fragment {
    private static final String TAG = "ProfileTenantFragment";

    private Spinner mSmoking;
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
    private BubbleSeekBar  mDistanceFromCenter;
    private Button mConfirm;

    private int mDistanceFromCenterValue;

    private String mChoosenCityName;
    private String mChoosenCityId;
    private double mCityLatitude;
    private double mCityLongitude;

    private DatabaseReference mDatabaseReference;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_tenant_fragment, null,false);


        mLandlordNation = v.findViewById(R.id.spinnerNationalityLL);
        mSmoking = v.findViewById(R.id.spinnerSmoking);
        mPeriodRenting = v.findViewById(R.id.spinnerPeriodRenting);
        mLandlordGender = v.findViewById(R.id.spinnerGenderLL);
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

        // Initialize Places.
        Places.initialize(getApplicationContext(), getString(R.string.google_cloud_api_key));

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(getActivity());

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);



        autocompleteFragment.setTypeFilter(TypeFilter.CITIES);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                //txtView.setText(place.getName()+","+place.getId());
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getLatLng());


                mChoosenCityName = place.getName();
                mChoosenCityId = place.getId();
                mCityLongitude = place.getLatLng().longitude;
                mCityLatitude = place.getLatLng().latitude;

            }

            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });


        mDistanceFromCenter.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                mDistanceFromCenterValue = progress;


            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                Log.i(TAG, "km: " + progress);

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

            }
        });
        mConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                boolean noError = true;

                //checking if deposit and rent are not nulls
                if (checkNullFields(mDepositMin) & checkNullFields(mDepositMax) &
                        checkNullFields(mRentMin) & checkNullFields(mRentMax)){
                    //Checking if for landlord age values.
                    if (mLandlordAgeMin.getText().toString().length()==0)
                    {

                        mLandlordAgeMin.setText("16");
                    }
                    if (Integer.parseInt(mLandlordAgeMin.getText().toString()) < 16)
                    {
                        mLandlordAgeMin.setError(getString(R.string.min_age));
                        mLandlordAgeMin.requestFocus();
                        mLandlordAgeMin.setText("16");
                        noError = false;
                    }
                    if (mLandlordAgeMax.getText().toString().length()==0)
                    {
                        mLandlordAgeMax.setText("120");
                    }
                    if (Integer.parseInt(mLandlordAgeMax.getText().toString()) > 120)
                    {
                        mLandlordAgeMax.setError(getString(R.string.max_age));
                        mLandlordAgeMax.requestFocus();
                        mLandlordAgeMax.setText("120");
                        noError = false;
                    }

                    if (mChoosenCityName == null){
                        Toast.makeText(getActivity(), getString(R.string.choose_city), Toast.LENGTH_LONG).show();
                        noError = false;
                    }
                    //checking if the min is smaller than Max, otherwise show to user.
                    if (minMaxFieldCheck(mRentMin,mRentMax))
                        noError = false;
                    if (minMaxFieldCheck(mDepositMin,mDepositMax))
                        noError = false;
                    if (noError){
                        TenantProfile newInput = new TenantProfile.Builder(userID)
                                .withLandlordNationallity(String.valueOf(mLandlordNation.getSelectedItem()))
                                .isSmokingFriendly(String.valueOf(mSmoking.getSelectedItem()))
                                .withCity(mChoosenCityId, mChoosenCityName, mCityLatitude, mCityLongitude)
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

                        mDatabaseReference
                                .child(DataBasePath.USERS.getValue())
                                .child(userID)
                                .child(DataBasePath.PROFILE.getValue())
                                .child(DataBasePath.TENANT.getValue())
                                .setValue(newInput);
                    }

                }

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
    private boolean checkNullFields(EditText tv){
            if (tv.getText().toString().equals("")){
                tv.setError(getString(R.string.requiered_field));
                tv.requestFocus();
                return false;
            }
            return true;

    }
    private boolean minMaxFieldCheck(EditText min, EditText max){
        if (Integer.parseInt((min.getText().toString())) > Integer.parseInt(max.getText().toString())){
            min.setError(getString(R.string.wrong_min));
            min.requestFocus();
            max.setError(getString(R.string.wrong_max));
            max.requestFocus();
            return true;
        }
        return false;
    }
}
