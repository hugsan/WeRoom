package com.itcom202.weroom.account.profiles;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itcom202.weroom.SingleFragment;
import com.itcom202.weroom.cameraGallery.Camera;
import com.itcom202.weroom.cameraGallery.PictureUploader;
import com.itcom202.weroom.MapFragment;
import com.itcom202.weroom.R;
import com.itcom202.weroom.swipe.SwipeActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.itcom202.weroom.cameraGallery.Camera.uploadFile;

public class RoomCreationFragment extends SingleFragment {
    static final int REQUEST_IMAGE_CAPTURE = 0;
    static final int GALLERY_REQUEST_CODE = 1;

    public final String TAG = "RoomCreationFragment";
    private EditText mRent;
    private EditText mDeposit;
    private EditText mRoomDescription;
    private Spinner mPeriodRenting;
    private EditText mRoomSize;
    private CheckBox mFurnished;
    private CheckBox mInternet;
    private CheckBox mCommonArea;
    private CheckBox mLaundry;
    private Button mConfirmRoom;
    private DatabaseReference mDatabaseReference;
    private ImageButton mTakeRoomPicture;
    private File mPhotoFile;
    private ImageView mProfilePhoto;


    String mAddressID;
    String mAddressName;
    double mAddressLatitude;
    double mAddressLongitude;
    String[] pictureNames = {"room_one", "room_two", "room_three", "room_four", "room_five",
            "room_six", "room_seven", "room_eight", "rom_nine", "room_teen"};
    int pictureNameIndex = 0;

    private List<PictureUploader> mPictureUploaders = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_room_fragment, null, false);


        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mRent = v.findViewById(R.id.rentInput);
        mDeposit = v.findViewById(R.id.depositroom);
        mPeriodRenting = v.findViewById(R.id.PeriodRentingRoomFragment);
        mRoomSize = v.findViewById(R.id.roomsizeroomfragment);
        mFurnished = v.findViewById(R.id.checkFurnished);
        mInternet = v.findViewById(R.id.checkBoxInternet);
        mCommonArea = v.findViewById(R.id.checkBoxCommonArea);
        mLaundry = v.findViewById(R.id.checkBoxLaundry);
        mConfirmRoom = v.findViewById(R.id.postRoomButton);
        mProfilePhoto = v.findViewById(R.id.picturepreview);
        mRoomDescription = v.findViewById(R.id.descriptionField);

        final MapFragment mapFragment = new MapFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.showmapfragment, mapFragment).commit();



        // Initialize Places.
        Places.initialize(getApplicationContext(), getString(R.string.google_cloud_api_key));

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(getActivity());

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        //autocompleteFragment.setTypeFilter(TypeFilter.GEOCODE);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                //txtView.setText(place.getName()+","+place.getId());
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getLatLng());
                mapFragment.updateSite(place.getLatLng());
                mAddressID = place.getId();
                mAddressName = place.getName();
                mAddressLatitude = place.getLatLng().latitude;
                mAddressLongitude = place.getLatLng().longitude;

            }

            @Override
            public void onError(Status status) {
                //TODO implement on error message
                Log.i(TAG, "An error occurred: " + status);
            }
        });


        mConfirmRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FIXME change 1 to create room 1, 2 or 3 for the users.



                //TODO missing to verify all the entries before creating the RoomPosted and pushing it to firebase

                RoomPosted input = new RoomPosted.Builder(1)
                        .hasCommonAreas(mCommonArea.isChecked())
                        .hasInternet(mInternet.isChecked())
                        .hasLaundry(mLaundry.isChecked())
                        .isFurnished(mFurnished.isChecked())
                        .withAddress(mAddressID, mAddressName, mAddressLatitude, mAddressLongitude)
                        .withDeposit(Integer.parseInt(mDeposit.getText().toString()))
                        .withPeriodRenting(String.valueOf(mPeriodRenting.getSelectedItem()))
                        .withRent(Integer.parseInt(mRent.getText().toString()))
                        .withSize(Integer.parseInt(mRoomSize.getText().toString()))
                        .withDescription(mRoomDescription.getText().toString())
                        .build();
                mDatabaseReference
                        .child(DataBasePath.USERS.getValue())
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(DataBasePath.PROFILE.getValue())
                        .child(DataBasePath.ROOM.getValue())
                        .setValue(input);
                uploadFile(mPictureUploaders);
                startActivity(SwipeActivity.newIntent(getActivity()));
            }
        });


        final Fragment  thisFragment = this;


        mTakeRoomPicture = v.findViewById(R.id.takeroompicture);
        mTakeRoomPicture.setOnClickListener(new View.OnClickListener() {
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
                    //uploadFile(image, "room_one");
                    if (pictureNameIndex < pictureNames.length)
                        mPictureUploaders.add(new PictureUploader(image,pictureNames[pictureNameIndex++]));
                    mProfilePhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    //TODO: rotate picture to portrait
                    break;

                case GALLERY_REQUEST_CODE:
                    Uri selectedImage = data.getData();
                    mProfilePhoto.setImageURI(selectedImage);
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                        //uploadFile(bitmap,"room_one");
                        if (pictureNameIndex < pictureNames.length)
                            mPictureUploaders.add(new PictureUploader(bitmap,pictureNames[pictureNameIndex++]));
                        mProfilePhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }catch (Exception e){
                        Log.d(TAG, "Exception Gallery: "+ e);
                    }
                    break;
            }
        } else
            Log.d(TAG, "Error on camera/Gallery");
    }


}
