package com.itcom202.weroom.account.onboarding.views.flow;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itcom202.weroom.framework.ProfileSingleton;
import com.itcom202.weroom.framework.SingleFragment;
import com.itcom202.weroom.R;
import com.itcom202.weroom.framework.DataBasePath;
import com.itcom202.weroom.account.onboarding.controllers.tagDescription.TagModel;
import com.itcom202.weroom.account.onboarding.controllers.tagDescription.TagSeparator;
import com.itcom202.weroom.account.onboarding.controllers.tagDescription.TagView;
import com.itcom202.weroom.framework.cameraandgallery.ImagePicker;
import com.itcom202.weroom.framework.cameraandgallery.PictureConversion;
import com.itcom202.weroom.account.models.Profile;
import com.itcom202.weroom.framework.queries.ImageController;
import com.itcom202.weroom.interaction.SwipeActivity;


import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends SingleFragment {
    static final int REQUEST_CODE = 123;
    public static final String KEY_IS_EDIT = "editable";
    private static final String TAG = "ProfileFragment";
    private String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirebaseAuth mFirebaseAuth;
    private EditText mUserName;
    private EditText mAge;
    private Button mCreateProfile;
    private Spinner mGender;
    private Spinner mCountry;
    public static  Spinner mRole;
    private TagView mTag;
    private List<String> tags = new ArrayList<>();

    private ImageView mProfilePhoto;
    private Bitmap mPicture;
    private boolean mEditable = false;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_fragment, container, false);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mUserName = v.findViewById(R.id.username);
        mAge = v.findViewById(R.id.list_contact_age);
        mCreateProfile = v.findViewById(R.id.createprofile);
        mGender = v.findViewById(R.id.spinnerGender);
        mCountry = v.findViewById(R.id.spinnerCountry);
        mRole = v.findViewById(R.id.spinnerRole);
        mTag = v.findViewById(R.id.tags);

        mTag.setHint(getString(R.string.description));
        mTag.addTagSeparator(TagSeparator.ENTER_SEPARATOR);
        String[] tagList = new String[]{getString(R.string.hint_1), getString(R.string.hint_2), getString(R.string.hint_3)};
        mTag.setTagList(tagList);

        ArrayAdapter adapterGender = ArrayAdapter.createFromResource(getActivity(), R.array.gender_array, R.layout.spinner_item);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGender.setAdapter(adapterGender);

        ArrayAdapter adapterRole = ArrayAdapter.createFromResource(getActivity(), R.array.role_array, R.layout.spinner_item);
        adapterRole.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRole.setAdapter(adapterRole);

        mCountry.setAdapter(countryAdapter());

        mCreateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 if(mPicture == null){
                    Toast.makeText(getContext(), "Please add profile picture", Toast.LENGTH_SHORT).show();
                 }

                else if (mUserName.getText().toString().equals("")) {
                    mUserName.setError(getString(R.string.type_name));
                    mUserName.requestFocus();
                } else if (mAge.getText().toString().isEmpty()) {
                    mAge.setError(getString(R.string.type_age));
                    mAge.requestFocus();
                } else if (Integer.parseInt(mAge.getText().toString()) < 17) {
                    mAge.setError(getString(R.string.too_young));
                    mAge.requestFocus();
                } else if (Integer.parseInt(mAge.getText().toString()) > 99) {
                    mAge.setError(getString(R.string.too_old));
                    mAge.requestFocus();
                } else if (mGender.getSelectedItemPosition() == 0) {
                    TextView errorText = (TextView) mGender.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText(R.string.select_gender);

                } else if (mCountry.getSelectedItemPosition() == 0) {
                    TextView errorText = (TextView) mCountry.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText(R.string.select_country);

                /*} else if(mRole.getSelectedItemPosition() == 0){
                    TextView errorText = (TextView) mRole.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText(R.string.select_role);*/
                }else {
                    for(TagModel model:  mTag.getSelectedTags()){
                        tags.add(model.getTagText());
                    }
                    createProfile();
                    if (mEditable){
                        ((SwipeActivity)getActivity()).changeToPorifleFragment();
                    }else
                    {
                        if (mRole.getSelectedItemId() == 0){
                            changeFragment(new LandlordProfileFragment());
                        }else if (mRole.getSelectedItemId() == 1){
                            changeFragment(new ProfileTenantFragment());
                        }
                    }

                }
            }
        });

        mProfilePhoto = v.findViewById(R.id.profilePhoto);
        mProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent chooseImageIntent = ImagePicker.getPickImageIntent(getActivity());
                startActivityForResult(chooseImageIntent, REQUEST_CODE);
            }
        });
        if (getArguments() != null && getArguments().getBoolean(KEY_IS_EDIT))
            setProfile(ProfileSingleton.getInstance());
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG,"request code: "+ requestCode);
        Log.d(TAG,"result code: "+ resultCode);
        if (resultCode == RESULT_OK){
            switch(requestCode) {
                case REQUEST_CODE:
                    Bitmap bitmap = null;
                    try {
                        bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                    } catch (IOException e) {
                        //do sth
                    }
                    mProfilePhoto.setImageBitmap(bitmap);
                    mPicture = bitmap;
                    mProfilePhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
                    break;
            }

        } else
            Log.d(TAG, "Error on camera/Gallery");
    }


    private SpinnerAdapter countryAdapter(){
        String[] locales = Locale.getISOCountries();

        List<String> countries = new ArrayList<>();
        countries.add(getString(R.string.prompt_country));

       // for (String countryCode : locales){
        for(int i=0;i<locales.length;i++){

            String countryCode=locales[i];
            Locale obj = new Locale("",countryCode);


            countries.add(obj.getDisplayCountry(Locale.ENGLISH));
        }
        Collections.sort(countries);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item , countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        return adapter;
    }

    private String getISOCode(String selectedCountry){
        Map<String, String> countries = new HashMap<>();
        for (String iso : Locale.getISOCountries()) {
            Locale l = new Locale("", iso);
            countries.put(l.getDisplayCountry(), iso);
        }

        return countries.get(selectedCountry);

    }
    private void createProfile(){
    Profile profile = new Profile.Builder(userID)
                .withName(mUserName.getText().toString())
                .withAge(Integer.parseInt(mAge.getText().toString()))
                .withGender(String.valueOf(mGender.getSelectedItem()))
                .withCountry(getISOCode(String.valueOf(mCountry.getSelectedItem())))
                .withRole(String.valueOf(mRole.getSelectedItem()))
                .withTags(tags)
                .build();

        ImageController.setProfilePicture(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                mPicture);
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(DataBasePath.USERS.getValue()).document(mFirebaseAuth.getUid())
                .set(profile);
        ProfileSingleton.initialize(profile);
    }
    private void setProfile(Profile p){
        mEditable = true;

        Task t = ImageController.getProfilePicture(p.getUserID());

        t.addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(final byte[] bytes) {
                mPicture = PictureConversion.byteArrayToBitmap(bytes);
                mProfilePhoto.setImageBitmap(mPicture);
            }
        });
        mCountry.setSelection(getCountryAdapterPosition(p.getCountry()));
        mUserName.setText(p.getName());
        mAge.setText(Integer.toString(p.getAge()));
        mRole.setVisibility(View.GONE);
        for (String s : p.getTags())
            mTag.addTag(s,false);
        ArrayAdapter adapterGender = ArrayAdapter.createFromResource(getActivity(), R.array.gender_array, R.layout.spinner_item);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGender.setSelection(adapterGender.getPosition(p.getGender()));
        mCountry.setSelection(getCountryAdapterPosition(p.getCountry()));

        mCreateProfile.setText(R.string.edit_profile);

    }

    private int getCountryAdapterPosition(String countryISO){
        String[] locales = Locale.getISOCountries();

        List<String> countries = new ArrayList<>();
        countries.add(getString(R.string.prompt_country));

        // for (String countryCode : locales){
        for(int i=0;i<locales.length;i++){

            String countryCode=locales[i];
            Locale obj = new Locale("",countryCode);


            countries.add(obj.getDisplayCountry(Locale.ENGLISH));
        }
        Locale obj = new Locale("",countryISO);
        Collections.sort(countries);
        return countries.indexOf(obj.getDisplayCountry(Locale.ENGLISH));
    }


}