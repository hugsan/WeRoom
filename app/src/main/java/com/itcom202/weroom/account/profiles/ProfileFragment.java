package com.itcom202.weroom.account.profiles;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itcom202.weroom.R;
import com.skyhope.materialtagview.TagView;
import com.skyhope.materialtagview.enums.TagSeparator;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {
    static final int REQUEST_IMAGE_CAPTURE = 0;
    static final int GALLERY_REQUEST_CODE = 1;

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
    private FirebaseStorage mFirebaseStorage;
    private TagView mTag;

    private ImageView mProfilePhoto;


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
        mTag= v.findViewById(R.id.textTags);

        mTag.setHint("Add tags about yourself");
        mTag.addTagSeparator(TagSeparator.SPACE_SEPARATOR);
        String[] tagList = new String[]{"Vegan", "Dog_Lover", "Outgoing"};
        mTag.setTagList(tagList);

        mGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String[] locales = Locale.getISOCountries();
        List<String> countries = new ArrayList<>();
        countries.add("Select Country");


        for (String countryCode : locales) {

            Locale obj = new Locale("", countryCode);

            countries.add(obj.getDisplayCountry());

        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCountry.setAdapter(adapter);


        mCreateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mUserName.getText().toString().equals("")) {
                    mUserName.setError("Please type your name!");
                    mUserName.requestFocus();
                } else if (mAge.getText().toString().isEmpty()) {
                    mAge.setError("Please type your age!");
                    mAge.requestFocus();
                } else if (Integer.parseInt(mAge.getText().toString()) < 15) {
                    mAge.setError("You should be at least 15!");
                    mAge.requestFocus();
                } else if (Integer.parseInt(mAge.getText().toString()) > 95) {
                    mAge.setError("You are too old! :)");
                    mAge.requestFocus();
                } else if (mGender.getSelectedItemPosition() == 0) {
                    TextView errorText = (TextView) mGender.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Select your gender!");

                } else if (mCountry.getSelectedItemPosition() == 0) {
                    TextView errorText = (TextView) mCountry.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Select your country!");

                } else {
                    Profile myProfile =
                            new Profile(mUserName.getText().toString(), Integer.parseInt(mAge.getText().toString()),
                                    String.valueOf(mGender.getSelectedItem()), String.valueOf(mCountry.getSelectedItem()));
                    mDatabaseReference
                            .child(DataBasePath.USERS)
                            .child(mUser.getUid())
                            .child(DataBasePath.PROFILE)
                            .setValue(myProfile);
                }
            }
        });

        mProfilePhoto = v.findViewById(R.id.profilePhoto);
        mProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery();
            }
        });


        mButtonProfilePhoto = v.findViewById(R.id.buttonProfilePhoto);
        mButtonProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
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

                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    mProfilePhoto.setImageBitmap(imageBitmap);
                    mProfilePhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    //TODO: rotate picture to portrait
                    break;

                case GALLERY_REQUEST_CODE:
                    Uri selectedImage = data.getData();
                    mProfilePhoto.setImageURI(selectedImage);
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                        uploadFile(bitmap);
                        mProfilePhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);


                    }catch (Exception e){
                        Log.d(TAG, "Exception Gallery: "+ e);
                    }
                    break;


            }

        }else
            Log.d(TAG,"Error on camera/Gallery");
    }
    private void uploadFile(Bitmap bitmap) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://weroom-fa6fe.appspot.com");
        StorageReference mountainImagesRef = storageRef.child("images/" + mUser.getUid() + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG,"Exception: "+ exception);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            }
        });

    }

    private void dispatchTakePictureIntent() {

       Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void pickFromGallery(){
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        startActivityForResult(intent,GALLERY_REQUEST_CODE);
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
    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


}