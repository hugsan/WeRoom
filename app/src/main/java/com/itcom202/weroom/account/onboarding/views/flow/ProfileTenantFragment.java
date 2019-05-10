package com.itcom202.weroom.account.onboarding.views.flow;

import android.content.pm.ActivityInfo;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itcom202.weroom.framework.ProfileSingleton;
import com.itcom202.weroom.R;
import com.itcom202.weroom.account.models.TenantProfile;
import com.itcom202.weroom.framework.DataBasePath;
import com.itcom202.weroom.account.onboarding.controllers.seekBar.BubbleSeekBar;

import java.util.Arrays;
import java.util.Objects;

import com.google.android.libraries.places.api.Places;
// Add import statements for the new library.
import com.google.android.libraries.places.api.net.PlacesClient;
import com.itcom202.weroom.account.models.Profile;
import com.itcom202.weroom.interaction.SwipeActivity;


import static com.facebook.FacebookSdk.getApplicationContext;

public class ProfileTenantFragment extends Fragment {
    private static final String TAG = "ProfileTenantFragment";
    public static final String KEY_INITIALIZE = "initialize";

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


    private int mDistanceFromCenterValue;

    private String mChosenCityName;
    private String mChosenCityId;
    private double mCityLatitude;
    private double mCityLongitude;

    private String smoke=null;
    private String pet=null;
    private String furnish=null;
    private String laundry=null;
    private String internet =null;

    private boolean mIsEdit = false;

    AutocompleteSupportFragment autocompleteFragment;



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



        ArrayAdapter adapterPeriodRent = ArrayAdapter.createFromResource(getActivity(), R.array.rending_period_array, R.layout.spinner_item);
        adapterPeriodRent.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPeriodRenting.setAdapter(adapterPeriodRent);



        // Initialize Places.
        Places.initialize(getApplicationContext(), getString(R.string.google_cloud_api_key));

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(getActivity());

        // Initialize the AutocompleteSupportFragment.
        autocompleteFragment = (AutocompleteSupportFragment)
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
                        internet ="Yes";
                    else if(String.valueOf(mLaundry.getCheckedRadioButtonId()).equals("2131296530"))
                        internet ="No";
                    else internet ="Does not matter";
//

                    if (noError){

                        // get selected radio button from radioGroup
                        int selectedId = mSmoking.getCheckedRadioButtonId();


                        RadioButton radioSexButton = v.findViewById(selectedId);

                        TenantProfile newInput = new TenantProfile.Builder(userID)
                               .isSmokingFriendly(smoke)
                                .withCity(mChosenCityId, mChosenCityName, mCityLatitude, mCityLongitude)
                                .withRentingPeriod(mPeriodRenting.getSelectedItemPosition())
                                .isPetFriendly(pet)
                                .isFurnished(furnish)
                                .hasInternet(internet)
                                .hasLaundry(laundry)
                                .withDepositRange(Integer.parseInt(mDepositMin.getText().toString()), Integer.parseInt(mDepositMax.getText().toString()))
                                .withRentRange(Integer.parseInt(mRentMin.getText().toString()), Integer.parseInt(mRentMax.getText().toString()))
                                .distanceFromCenter(mDistanceFromCenterValue)
                                .build();

                        Profile p = ProfileSingleton.getInstance();
                        p.setTenant(newInput);
                        ProfileSingleton.initialize(p);

                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        db.collection(DataBasePath.USERS.getValue())
                                .document(userID)
                                .update(DataBasePath.TENANT.getValue(),newInput);

                        if (mIsEdit){
                            ((SwipeActivity)getActivity()).changeToPorifleFragment();
                        }else{
                            startActivity(SwipeActivity.newIntent(getActivity()));
                        }
                    }
                }
            }
        });

        if (getArguments() != null && getArguments().getBoolean(KEY_INITIALIZE))
            setTenantValues(v,ProfileSingleton.getInstance().getTenant());
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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
    private void setTenantValues(View v,TenantProfile tp){
        mIsEdit = true;
        String smoker = tp.getSmokeFriendly();
        switch(smoker){
            case "Yes":
                ((RadioButton)v.findViewById(R.id.rbNoSmoke_tenantProfile)).setChecked(true);
                break;
            case "No":
                ((RadioButton)v.findViewById(R.id.rbNoSmoke_tenantProfile)).setChecked(true);
                break;
            case "Does not matter":
                ((RadioButton)v.findViewById(R.id.rbDCSMoke_tenantProfile)).setChecked(true);
                break;
            default:
                Log.e(TAG,"Error on getSmoking from Tenant.");
                break;
        }
        ((Spinner)v.findViewById(R.id.spinnerPeriodRenting_tenantProfile)).setSelection(tp.getPeriodOfRent());
        String petFriendly = tp.getPetFriendly();
        switch(petFriendly){
            case "Yes":
                ((RadioButton)v.findViewById(R.id.rbYesPet_tenantProfile)).setChecked(true);
                break;
            case "No":
                ((RadioButton)v.findViewById(R.id.rbNoPet_tenantProfile)).setChecked(true);
                break;
            case "Does not matter":
                ((RadioButton)v.findViewById(R.id.rbDCPet_tenantProfile)).setChecked(true);
                break;
            default:
                Log.e(TAG,"Error on getPetFriendly from Tenant.");
                break;
        }
        String furnished = tp.getmFurnished();
        switch(furnished){
            case "Yes":
                ((RadioButton)v.findViewById(R.id.rbNoFurnished_tenantProfile)).setChecked(true);
                break;
            case "No":
                ((RadioButton)v.findViewById(R.id.rbNoFurnished_tenantProfile)).setChecked(true);
                break;
            case "Does not matter":
                ((RadioButton)v.findViewById(R.id.rbDCFurnished_tenantProfile)).setChecked(true);
                break;
            default:
                Log.e(TAG,"Error on getFurnished from Tenant.");
                break;
        }
        String laundry = tp.getmLaundry();
        switch(laundry){
            case "Yes":
                ((RadioButton)v.findViewById(R.id.rbNoLaundry_tenantProfile)).setChecked(true);
                break;
            case "No":
                ((RadioButton)v.findViewById(R.id.rbNoLaundry_tenantProfile)).setChecked(true);
                break;
            case "Does not matter":
                ((RadioButton)v.findViewById(R.id.rbDCLaundry_tenantProfile)).setChecked(true);
                break;
            default:
                Log.e(TAG,"Error on getLaundry from Tenant.");
                break;
        }
        String internet = tp.getmInternet();
        switch(internet){
            case "Yes":
                ((RadioButton)v.findViewById(R.id.rbNoInternet_tenantProfile)).setChecked(true);
                break;
            case "No":
                ((RadioButton)v.findViewById(R.id.rbNoInternet_tenantProfile)).setChecked(true);
                break;
            case "Does not matter":
                ((RadioButton)v.findViewById(R.id.rbDCInternet_tenantProfile)).setChecked(true);
                break;
            default:
                Log.e(TAG,"Error on getInternet from Tenant.");
                break;
        }
        mDepositMin.setText(Integer.toString(tp.getMinDeposit()));
        mDepositMax.setText(Integer.toString(tp.getMAxDeposit()));
        mRentMin.setText(Integer.toString(tp.getMinRent()));
        mRentMax.setText(Integer.toString(tp.getMaxRent()));

        mDistanceFromCenter.setProgress(tp.getDistanceCenter());

        autocompleteFragment.setText(tp.getChoosenCityname());
        mConfirm.setText(R.string.edit_tenant);
    }
}
