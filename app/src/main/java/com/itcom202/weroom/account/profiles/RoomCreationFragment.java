package com.itcom202.weroom.account.profiles;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itcom202.weroom.SingleFragment;
import com.itcom202.weroom.MapFragment;
import com.itcom202.weroom.R;
import com.itcom202.weroom.cameraGallery.ImagePicker;
import com.itcom202.weroom.swipe.SwipeActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class RoomCreationFragment extends SingleFragment {
    static final int REQUEST_CODE = 123;

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
    private String mUserId;
    private String mFreeRoom;
    private Button mAddAnotherRoom;
    private PopUpMessage mPopUp = new PopUpMessage();
    private List<String> mPictures = new ArrayList<>();

    String mAddressID;
    String mAddressName;
    double mAddressLatitude;
    double mAddressLongitude;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_room_fragment, null, false);

        mUserId = FirebaseAuth.getInstance().getUid();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        roomExist();

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
        mAddAnotherRoom = v.findViewById(R.id.addMoreRooms);

        ArrayAdapter adapterPeriodRent = ArrayAdapter.createFromResource(getActivity(), R.array.rending_period_array, R.layout.spinner_item);
        adapterPeriodRent.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPeriodRenting.setAdapter(adapterPeriodRent);


        mRoomDescription = v.findViewById(R.id.descriptionField);
        final MapFragment mapFragment = new MapFragment();

        if(isPackageInstalled(GooglePlayServicesUtil.GOOGLE_PLAY_STORE_PACKAGE)) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.showmapfragment, mapFragment).commit();

        }



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
               // txtView.setText(place.getName()+","+place.getId());
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getLatLng());
                if(isPackageInstalled(GooglePlayServicesUtil.GOOGLE_PLAY_STORE_PACKAGE)) {

                    mapFragment.updateSite(place.getLatLng());
                }
                mAddressID = place.getId();
                mAddressName = place.getName();
                mAddressLatitude = place.getLatLng().latitude;
                mAddressLongitude = place.getLatLng().longitude;

            }


            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
                Toast.makeText(getActivity(), R.string.room_creation_failed, Toast.LENGTH_SHORT).show();
            }
        });




        mConfirmRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FIXME change 1 to create room 1, 2 or 3 for the users.
                if(mRent.length()==0 || Integer.parseInt(mRent.getText().toString())<0){
                    mRent.setError(getString(R.string.type_rent));
                    mRent.requestFocus();
                }
                else if(mDeposit.length()==0){
                    mDeposit.setError(getString(R.string.type_deposit));
                    mDeposit.requestFocus();
                }
                else if(mPeriodRenting.getSelectedItemPosition()==0){
                    TextView errorText = (TextView) mPeriodRenting.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText(R.string.choose_period);
                }
//                else if(mAddressName == null){
//                    Toast.makeText(getContext(), R.string.type_address, Toast.LENGTH_SHORT).show();
//                }
                else if(mRoomSize.length()==0){
                    mRoomSize.setError(getString(R.string.type_room_size));
                    mRoomSize.requestFocus();
                }
                else if(mRoomDescription.length()==0){
                    Toast.makeText(getContext(), R.string.type_description_room, Toast.LENGTH_SHORT).show();
                }
                else {
                    RoomPosted input = new RoomPosted.Builder()
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


                roomExist();

                if (mFreeRoom != null){
                    postRoom();

                }

                postRoom();
                startActivity(SwipeActivity.newIntent(getActivity()));
                }
            }
        });

        mAddAnotherRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roomExist();
                if (mFreeRoom != null){
                    postRoom();

                    mPopUp.showDialog(getActivity(),getString(R.string.dialog_message));

                    changeFragment(new RoomCreationFragment());


                }else
                {
                    Toast.makeText(getActivity(), getString(R.string.only_3_rooms), Toast.LENGTH_LONG).show();

                }
            }
        });

        final Fragment  thisFragment = this;


        mTakeRoomPicture = v.findViewById(R.id.takeroompicture);
        mTakeRoomPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseImageIntent = ImagePicker.getPickImageIntent(getActivity());
                startActivityForResult(chooseImageIntent, REQUEST_CODE);
            }
        });
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
                    mPictures.add(String.valueOf(bitmap));
                    mProfilePhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
                    break;
            }
        } else
            Log.d(TAG, "Error on camera/Gallery");
    }

    private void roomExist(){

        Query event = mDatabaseReference
                .child(DataBasePath.USERS.getValue())
                .child(FirebaseAuth.getInstance().getUid())
                .child(DataBasePath.LANDLORD.getValue());
        event.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (!dataSnapshot.child(mUserId).child(DataBasePath.ROOM_ONE.getValue()).exists())
                        mFreeRoom = DataBasePath.ROOM_ONE.getValue();
                    else if (!dataSnapshot.child(mUserId).child(DataBasePath.ROOM_TWO.getValue()).exists())
                        mFreeRoom = DataBasePath.ROOM_TWO.getValue();
                    else if (!dataSnapshot.child(mUserId).child(DataBasePath.ROOM_THREE.getValue()).exists())
                        mFreeRoom = DataBasePath.ROOM_THREE.getValue();
                    else
                        mFreeRoom = null;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //TODO something when there is an error wiht the database query
            }
        });
    }
    //FIXME Patrick will fix this, this method is not working properly, posting all the rooms in roomOne
    private void postRoom(){
        if(mRent.length()==0 || Integer.parseInt(mRent.getText().toString())<0){
            mRent.setError(getString(R.string.type_rent));
            mRent.requestFocus();
        }
        else if(mDeposit.length()==0){
            mDeposit.setError(getString(R.string.type_deposit));
            mDeposit.requestFocus();
        }
        //TODO check that the user have atleast introduced 3 pictures for the room. (check the list mPictures.size > 3)
        else if(mPeriodRenting.getSelectedItemPosition()==0){
            TextView errorText = (TextView) mPeriodRenting.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText(R.string.choose_period);
        }
//                else if(mAddressName == null){
//                    Toast.makeText(getContext(), R.string.type_address, Toast.LENGTH_SHORT).show();
//                }
        else if(mRoomSize.length()==0){
            mRoomSize.setError(getString(R.string.type_room_size));
            mRoomSize.requestFocus();
        }
        else if(mRoomDescription.length()==0){
            Toast.makeText(getContext(), R.string.type_description_room, Toast.LENGTH_SHORT).show();
        }
        else {
            RoomPosted input = new RoomPosted.Builder()
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

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            mFreeRoom = "roomOne";

            db.collection(DataBasePath.USERS.getValue())
                    .document(FirebaseAuth.getInstance().getUid())
                    .update(DataBasePath.LANDLORD.getValue()+"."+mFreeRoom,input);

        }
    }
    protected final boolean isPackageInstalled(String packageName) {
        try {
            getApplicationContext().getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }



}

