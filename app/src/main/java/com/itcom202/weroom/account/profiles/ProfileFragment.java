package com.itcom202.weroom.account.profiles;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.itcom202.weroom.SingleFragment;
import com.itcom202.weroom.cameraGallery.Camera;
import com.itcom202.weroom.cameraGallery.PictureUploader;
import com.itcom202.weroom.R;
import com.itcom202.weroom.account.profiles.TagDescription.TagModel;
import com.itcom202.weroom.account.profiles.TagDescription.TagSeparator;
import com.itcom202.weroom.account.profiles.TagDescription.TagView;


import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import static android.app.Activity.RESULT_OK;
import static com.itcom202.weroom.cameraGallery.Camera.currentPhotoPath;
import static com.itcom202.weroom.cameraGallery.Camera.uploadFile;
import static com.itcom202.weroom.cameraGallery.Gallery.pickFromGallery;


public class ProfileFragment extends SingleFragment {
    static final int REQUEST_IMAGE_CAPTURE = 0;
    static final int GALLERY_REQUEST_CODE = 1;

    //TODO: Put textViews before each EditText with information what is the editText for
    private static final String TAG = "ProfileFragment";
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mUser;
    private EditText mUserName;
    private EditText mAge;
    private Button mCreateProfile;
    private ImageButton mButtonProfilePhoto;
    private Spinner mGender;
    private Spinner mCountry;
    private Spinner mRole;
    private FirebaseStorage mFirebaseStorage;
    private TagView mTag;
    private File mPhotoFile;
    private List<String> tags = new ArrayList<>();

    private ImageView mProfilePhoto;

    private List<PictureUploader> uploadPictures = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_fragment, container, false);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseStorage = FirebaseStorage.getInstance();

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


        //FIXME: color of text is white on white background

        mCountry.setAdapter(countryAdapter());


        mCreateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mUserName.getText().toString().equals("")) {
                    mUserName.setError(getString(R.string.type_name));
                    mUserName.requestFocus();
                } else if (mAge.getText().toString().isEmpty()) {
                    mAge.setError(getString(R.string.type_age));
                    mAge.requestFocus();
                } else if (Integer.parseInt(mAge.getText().toString()) < 15) {
                    mAge.setError(getString(R.string.too_young));
                    mAge.requestFocus();
                } else if (Integer.parseInt(mAge.getText().toString()) > 95) {
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
                    Profile myProfile =
                            new Profile(mUserName.getText().toString(), Integer.parseInt(mAge.getText().toString()),
                                    String.valueOf(mGender.getSelectedItem()), String.valueOf(mCountry.getSelectedItem()),
                                    String.valueOf(mRole.getSelectedItem()), tags);
                    mDatabaseReference
                            .child(DataBasePath.USERS.getValue())
                            .child(mUser.getUid())
                            .child(DataBasePath.PROFILE.getValue())
                            .setValue(myProfile);

                    uploadFile(uploadPictures);
                    String[] role = getActivity().getResources().getStringArray(R.array.role_array);
                    if (mRole.getSelectedItemId() == 0){
                        changeFragment(new LandlordProfileFragment());
                    }else if (mRole.getSelectedItemId() == 1){
                        changeFragment(new ProfileTenantFragment());
                    }

                }
            }
        });
        final Fragment  thisFragment = this;

        mProfilePhoto = v.findViewById(R.id.profilePhoto);
        mProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery(getActivity(), thisFragment);
            }
        });

        mButtonProfilePhoto = v.findViewById(R.id.buttonProfilePhoto);
        mButtonProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoFile = Camera.dispatchTakePictureIntent(getActivity(),thisFragment );
            }
        });

        return v;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG,"request code: "+ requestCode);
        Log.d(TAG,"result code: "+ resultCode);
        if (resultCode == RESULT_OK){
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:

                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    Bitmap image = BitmapFactory.decodeFile(mPhotoFile.getPath(),bmOptions);
                    mProfilePhoto.setImageBitmap(image);
                    uploadPictures.add(new PictureUploader(image, "profile"));
                    //uploadFile(image, "profile");
                    mProfilePhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    //TODO: rotate picture to portrait
                    break;

                case GALLERY_REQUEST_CODE:
                    Uri selectedImage = data.getData();
                    mProfilePhoto.setImageURI(selectedImage);
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                        //uploadFile(bitmap, "profile");
                        uploadPictures.add(new PictureUploader(bitmap,"profile"));
                        mProfilePhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }catch (Exception e){
                        Log.d(TAG, "Exception Gallery: "+ e);
                    }
                    break;
            }
        } else
            Log.d(TAG, "Error on camera/Gallery");
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = mProfilePhoto.getWidth();
        int targetH = mProfilePhoto.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        mProfilePhoto.setImageBitmap(bitmap);
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