package com.itcom202.weroom.account.profiles;

import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
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
import com.itcom202.weroom.swipe.SwipeActivity;


import static com.facebook.FacebookSdk.getApplicationContext;

public class ProfileTenantFragment extends Fragment {
    private static final String TAG = "ProfileTenantFragment";

    private Spinner mPeriodRenting;
    private RadioGroup mSmoking;
    private RadioGroup mPetFriendly;
    private RadioGroup mFurnished;
    private RadioGroup mInternet;
    private RadioGroup mLaundry;
    private EditText mDepositMin;
    private EditText mDepositMax;
    private EditText mRentMin;
    private EditText mRentMax;
    private BubbleSeekBar mDistanceFromCenter;
    private Button mConfirm;

    //FIXME: when there is a toast in the screen, all the selected radioButtons go to default
    //if there is an error and consequently a setFocus() the selected radio buttons are not being changed
    //FIXME: needs to be done to fit every screen

    private int mDistanceFromCenterValue;

    private String mChosenCityName;
    private String mChosenCityId;
    private double mCityLatitude;
    private double mCityLongitude;

    private String smoke=null;
    private String pet=null;
    private String furnish=null;
    private String laundry=null;
    private String intenet=null;

    private DatabaseReference mDatabaseReference;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tenant_profile_fragment, null,false);


        mSmoking = v.findViewById(R.id.radioGroupSmokeFriendly_tenantProfile);
        mPeriodRenting = v.findViewById(R.id.spinnerPeriodRenting_tenantProfile);
        mPetFriendly = v.findViewById(R.id.radioGroupPetFriendly_tenantProfile);
        mFurnished = v.findViewById(R.id.radioGroupFurnished_tenantProfile);
        mInternet = v.findViewById(R.id.radioGroupInternet_tenantProfile);
        mLaundry = v.findViewById(R.id.radioGroupLaundry_tenantProfile);
        mDepositMin = v.findViewById(R.id.depositMin_tenantProfile);
        mDepositMax = v.findViewById(R.id.depositMax_tenantProfile);
        mRentMin = v.findViewById(R.id.rentMin_tenantProfile);
        mRentMax = v.findViewById(R.id.rentMax_tenantProfile);
        mDistanceFromCenter = v.findViewById(R.id.radiusCenter_tenantProfile);
        mConfirm = v.findViewById(R.id.confirmButton_tenantProfile);


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


                mChosenCityName = place.getName();
                mChosenCityId = place.getId();
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
                    if (mChosenCityName == null){
                        Toast.makeText(getActivity(), getString(R.string.choose_city), Toast.LENGTH_LONG).show();
                        noError = false;
                    }
                    //checking if the min is smaller than Max, otherwise show to user.
                    if (minMaxFieldCheck(mRentMin,mRentMax))
                        noError = false;
                    if (minMaxFieldCheck(mDepositMin,mDepositMax))
                        noError = false;
                    if(mPeriodRenting.getSelectedItemPosition()==0){
                        noError=false;
                        TextView errorText = (TextView) mPeriodRenting.getSelectedView();
                        errorText.setError("");
                        errorText.setTextColor(Color.RED);
                        errorText.setText(R.string.choose_period);
                    }
                    if(String.valueOf(mSmoking.getCheckedRadioButtonId()).equals("2131296538"))
                        smoke="Yes";
                    else if(String.valueOf(mSmoking.getCheckedRadioButtonId()).equals("2131296533"))
                        smoke="No";
                    else smoke="Does not matter";

                    if(String.valueOf(mPetFriendly.getCheckedRadioButtonId()).equals("2131296537"))
                        pet="Yes";
                    else if(String.valueOf(mPetFriendly.getCheckedRadioButtonId()).equals("2131296532"))
                        pet="No";
                    else pet="Does not matter";

                    if(String.valueOf(mFurnished.getCheckedRadioButtonId()).equals("2131296534"))
                        furnish="Yes";
                    else if(String.valueOf(mFurnished.getCheckedRadioButtonId()).equals("2131296529"))
                        furnish="No";
                    else furnish="Does not matter";

                    if(String.valueOf(mLaundry.getCheckedRadioButtonId()).equals("2131296536"))
                        laundry="Yes";
                    else if(String.valueOf(mLaundry.getCheckedRadioButtonId()).equals("2131296531"))
                        laundry="No";
                    else laundry="Does not matter";

                    if(String.valueOf(mInternet.getCheckedRadioButtonId()).equals("2131296535"))
                        intenet="Yes";
                    else if(String.valueOf(mLaundry.getCheckedRadioButtonId()).equals("2131296530"))
                        intenet="No";
                    else intenet="Does not matter";


                    if (noError){

                         TenantProfile newInput = new TenantProfile.Builder(userID)
                               .isSmokingFriendly(smoke)
                                .withCity(mChosenCityId, mChosenCityName, mCityLatitude, mCityLongitude)
                                .withRentingPeriod(mPeriodRenting.getSelectedItemPosition())
                                .isPetFriendly(pet)
                                .isFurnished(furnish)
                                .hasInternet(intenet)
                                .hasLaundry(laundry)
                                .withDepositRange(Integer.parseInt(mDepositMin.getText().toString()), Integer.parseInt(mDepositMax.getText().toString()))
                                .withRentRange(Integer.parseInt(mRentMin.getText().toString()), Integer.parseInt(mRentMax.getText().toString()))
                                .distanceFromCenter(mDistanceFromCenterValue)
                                .build();

                        mDatabaseReference
                                .child(DataBasePath.USERS.getValue())
                                .child(userID)
                                .child(DataBasePath.PROFILE.getValue())
                                .child(DataBasePath.TENANT.getValue())
                                .setValue(newInput);
                        startActivity(SwipeActivity.newIntent(getActivity()));
                    }

                }

            }
        });



        return v;
    }
//    private SpinnerAdapter countryAdapter(){
//
//        String[] locales = Locale.getISOCountries();
//        List<String> countries = new ArrayList<>();
//        countries.add(getString(R.string.prompt_country));
//
//
//
//        for (String countryCode : locales) {
//
//            Locale obj = new Locale("", countryCode);
//
//            countries.add(obj.getDisplayCountry());
//        }
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, countries);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//
//        return adapter;
//    }
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
