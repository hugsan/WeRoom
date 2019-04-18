package com.itcom202.weroom.account.profiles;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.itcom202.weroom.SingleFragment;
//import com.itcom202.weroom.cameraGallery.Camera;
import com.itcom202.weroom.R;
import com.itcom202.weroom.account.profiles.tagDescription.TagModel;
import com.itcom202.weroom.account.profiles.tagDescription.TagSeparator;
import com.itcom202.weroom.account.profiles.tagDescription.TagView;
import com.itcom202.weroom.cameraGallery.ImagePicker;
import com.itcom202.weroom.cameraGallery.PictureConversion;
import com.itcom202.weroom.queries.ImageController;


import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends SingleFragment {
    static final int REQUEST_CODE = 123;

    private static final String TAG = "ProfileFragment";
    private FirebaseAuth mFirebaseAuth;
    private EditText mUserName;
    private EditText mAge;
    private Button mCreateProfile;
    private Spinner mGender;
    private Spinner mCountry;
    //TODO we need to fix this, we can not communicate values this way.
    public static  Spinner mRole;
    private TagView mTag;
    private File mPhotoFile;
    private List<String> tags = new ArrayList<>();

    private ImageView mProfilePhoto;
    private Bitmap mPicture;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_fragment, container, false);

      //  String[] genders={"Select your gender", "Female", "Male"};

        mFirebaseAuth = FirebaseAuth.getInstance();

        mUserName = v.findViewById(R.id.username);
        mAge = v.findViewById(R.id.age);
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
               /* } else if (mGender.getSelectedItemPosition() == 0) {
                    TextView errorText = (TextView) mGender.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText(R.string.select_gender);*/

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
                    if (mRole.getSelectedItemId() == 0){
                        changeFragment(new LandlordProfileFragment());
                    }else if (mRole.getSelectedItemId() == 1){
                        changeFragment(new ProfileTenantFragment());
                    }

                }
            }
        });
        final Fragment thisFragment = this;

        mProfilePhoto = v.findViewById(R.id.profilePhoto);
        mProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent chooseImageIntent = ImagePicker.getPickImageIntent(getActivity());
                startActivityForResult(chooseImageIntent, REQUEST_CODE);
            }
        });
        return v;
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

        for (String countryCode : locales) {

            Locale obj = new Locale("", countryCode);

            countries.add(obj.getDisplayCountry());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item , countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        return adapter;
    }
    private void createProfile(){
        Profile myProfile =
                new Profile(mUserName.getText().toString(), Integer.parseInt(mAge.getText().toString()),
                        String.valueOf(mGender.getSelectedItem()), String.valueOf(mCountry.getSelectedItem()),
                        String.valueOf(mRole.getSelectedItem()), tags);

        ImageController.setProfilePicture(FirebaseAuth.getInstance().getUid(),
                mPicture);
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(DataBasePath.USERS.getValue()).document(mFirebaseAuth.getUid())
                .set(myProfile);
    }



}