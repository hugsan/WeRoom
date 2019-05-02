package com.itcom202.weroom.account.profiles;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.itcom202.weroom.SingleFragment;
import com.itcom202.weroom.MapFragment;
import com.itcom202.weroom.R;
import com.itcom202.weroom.cameraGallery.ImagePicker;
import com.itcom202.weroom.queries.ImageController;
import com.itcom202.weroom.swipe.SwipeActivity;
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
    private String mUserId;
    private Button mAddAnotherRoom;
    private PopUpMessage mPopUp = new PopUpMessage();
    private List<Bitmap> mRoomPictures = new ArrayList<>();
    private LinearLayout layoutMap;
    private ImageView[] mPictures = new ImageView[10];

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

        mRent = v.findViewById(R.id.rentInput);
        mDeposit = v.findViewById(R.id.depositroom);
        mPeriodRenting = v.findViewById(R.id.PeriodRentingRoomFragment);
        mRoomSize = v.findViewById(R.id.roomsizeroomfragment);
        mFurnished = v.findViewById(R.id.checkFurnished);
        mInternet = v.findViewById(R.id.checkBoxInternet);
        mCommonArea = v.findViewById(R.id.checkBoxCommonArea);
        mLaundry = v.findViewById(R.id.checkBoxLaundry);
        mConfirmRoom = v.findViewById(R.id.postRoomButton);
        mAddAnotherRoom = v.findViewById(R.id.addMoreRooms);
        layoutMap = v.findViewById(R.id.layoutmap);

        for (int i = 0 ; i < 9 ; i++){
            String btnID = "picturepreviewnr"+ (i+1);
            int resID = getResources().getIdentifier(btnID, "id", Objects.requireNonNull(getActivity()).getPackageName());
            mPictures[i] = v.findViewById(resID);
            mPictures[i].setVisibility(View.GONE);
        }



        try{
            layoutMap.setVisibility(View.GONE);
        }catch(Exception e){
            Log.d(TAG,"layout visible");
        }

        final ArrayAdapter adapterPeriodRent = ArrayAdapter.createFromResource(getActivity(), R.array.rending_period_array, R.layout.spinner_item);
        adapterPeriodRent.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPeriodRenting.setAdapter(adapterPeriodRent);

        mRoomDescription = v.findViewById(R.id.descriptionField);

        final MapFragment mapFragment = new MapFragment();
        final FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
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
                // txtView.setText(place.getName()+","+place.getId());
                mAddressName = place.getName();
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getLatLng());

                try {
                    layoutMap.setVisibility(View.VISIBLE);
                    mapFragment.updateSite(place.getLatLng());
                }catch (Exception e){
                   // Toast.makeText(getContext(), "No map", Toast.LENGTH_SHORT).show();
                    layoutMap.setVisibility(View.GONE);
                }

                    mAddressID = place.getId();

                    mAddressLatitude = Objects.requireNonNull(place.getLatLng()).latitude;
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
                else if(mAddressName == null){
                    Toast.makeText(getContext(), R.string.type_address, Toast.LENGTH_SHORT).show();
                }
                else if(mRoomSize.length()==0){
                    mRoomSize.setError(getString(R.string.type_room_size));
                    mRoomSize.requestFocus();
                }
                else if(mRoomDescription.length()==0){
                    Toast.makeText(getContext(), R.string.type_description_room, Toast.LENGTH_SHORT).show();
                }
                else {
                    postRoom(true);
                }
            }
        });

        mAddAnotherRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postRoom(false);




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
                    keepPicture(bitmap);
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
                    break;
            }
        } else
            Log.d(TAG, "Error on camera/Gallery");
    }

    private void postRoom(final boolean once){
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
        }else if(mAddressName == null){
                    Toast.makeText(getContext(), R.string.type_address, Toast.LENGTH_SHORT).show();
        }
        else if(mRoomSize.length()==0){
            mRoomSize.setError(getString(R.string.type_room_size));
            mRoomSize.requestFocus();
        }
        else if(mRoomDescription.length()==0){
            Toast.makeText(getContext(), R.string.type_description_room, Toast.LENGTH_SHORT).show();
        } else if (mRoomPictures.size() < 3 ){
            Toast.makeText(getContext(), R.string.at_least_three_pictures, Toast.LENGTH_SHORT).show();
        }
        else {
            final RoomPosted input = new RoomPosted.Builder(mUserId)
                    .hasCommonAreas(mCommonArea.isChecked())
                    .hasInternet(mInternet.isChecked())
                    .hasLaundry(mLaundry.isChecked())
                    .isFurnished(mFurnished.isChecked())
                    .withAddress(mAddressID, mAddressName, mAddressLatitude, mAddressLongitude)
                    //FIXME  java.lang.NumberFormatException: For input string: "4567890987" for deposit;
                    .withDeposit(Integer.parseInt(mDeposit.getText().toString()))
                    .withPeriodRenting(String.valueOf(mPeriodRenting.getSelectedItem()))
                    .withRent(Integer.parseInt(mRent.getText().toString()))
                    .withSize(Integer.parseInt(mRoomSize.getText().toString()))
                    .withDescription(mRoomDescription.getText().toString())
                    .build();

            final FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection(DataBasePath.USERS.getValue())
                    .whereEqualTo("userID", mUserId)
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    DocumentSnapshot doc = queryDocumentSnapshots.getDocuments().get(0);
                    Profile p = doc.toObject(Profile.class);
                    if (p.getLandlord().getRoomsID().size() >= 3){
                        mPopUp.showDialog(getActivity(),getString(R.string.more_three_rooms_error));
                        mPopUp.changeToActivityOnClose(getActivity(),SwipeActivity.newIntent(getActivity()));
                    }
                    else
                    {
                        if (p.getLandlord().addRoomID(input.getRoomID())){

                            db.collection(DataBasePath.USERS.getValue())
                                    .document(mUserId)
                                    .set(p);
                        }

                        db.collection(DataBasePath.ROOMS.getValue())
                                .document(input.getRoomID())
                                .set(input);
                        uploadRoomPictures(input.getRoomID());
                        if (!once){
                            mPopUp.showDialog(getActivity(),getString(R.string.dialog_message));
                            changeFragment(new RoomCreationFragment());
                        }
                        else{
                            mPopUp.showDialog(getActivity(),getString(R.string.dialog_title));
                            startActivity(SwipeActivity.newIntent(getActivity()));
                        }
                    }
                }
            });
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

    private void uploadRoomPictures(String roomID){
        for (int i = 0 ; i < mRoomPictures.size(); i++)
        ImageController.setRoomPicture(roomID,mRoomPictures.get(i), i);
    }
    private void keepPicture(Bitmap bmp){
        int picturePosition = mRoomPictures.size();
        if (picturePosition<10){
            mPictures[picturePosition].setVisibility(View.VISIBLE);
            mPictures[picturePosition].setScaleType(ImageView.ScaleType.CENTER_CROP);
            mPictures[picturePosition].setImageBitmap(bmp);
            mRoomPictures.add(bmp);
        }
    }


}

