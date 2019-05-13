package com.itcom202.weroom.interaction.profile.views;

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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itcom202.weroom.account.onboarding.views.widget.MapFragment;
import com.itcom202.weroom.framework.ProfileSingleton;
import com.itcom202.weroom.R;
import com.itcom202.weroom.framework.DataBasePath;
import com.itcom202.weroom.framework.PopUpMessage;
import com.itcom202.weroom.account.models.Profile;
import com.itcom202.weroom.account.models.RoomPosted;
import com.itcom202.weroom.framework.cameraandgallery.ImagePicker;
import com.itcom202.weroom.framework.cameraandgallery.PictureConversion;
import com.itcom202.weroom.framework.queries.ImageController;
import com.itcom202.weroom.interaction.SwipeActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class RoomEditFragment extends Fragment {
    static final int REQUEST_CODE = 123;
    public static final String KEY_ROOM = "room";
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
    private Button mEditRoom;
    private DatabaseReference mDatabaseReference;
    private ImageButton mTakeRoomPicture;
    private String mUserId;
    private Button mDeleteRoom;
    private PopUpMessage mPopUp = new PopUpMessage();
    private List<Bitmap> mRoomPictures = new ArrayList<>();
    private LinearLayout layoutMap;
    private ImageView[] mPictures = new ImageView[10];
    String mAddressID;
    String mAddressName;
    double mAddressLatitude;
    double mAddressLongitude;
    private RoomPosted mThisPostedRoom;
    private AutocompleteSupportFragment mAutocompleteFragment;
    private MapFragment mMapFragment;
    private Switch mLazySwipeSwitch;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_room_editing, null, false);



        mUserId = FirebaseAuth.getInstance().getUid();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mRent = v.findViewById(R.id.room_edit_rentInput);
        mDeposit = v.findViewById(R.id.room_edit_depositroom);
        mPeriodRenting = v.findViewById(R.id.room_edit_PeriodRentingRoomFragment);
        mRoomSize = v.findViewById(R.id.room_edit_roomsizeroomfragment);
        mFurnished = v.findViewById(R.id.room_edit_checkFurnished);
        mInternet = v.findViewById(R.id.room_edit_checkBoxInternet);
        mCommonArea = v.findViewById(R.id.room_edit_checkBoxCommonArea);
        mLaundry = v.findViewById(R.id.room_edit_checkBoxLaundry);
        mEditRoom = v.findViewById(R.id.room_edit_postEditRoom);
        mLazySwipeSwitch = v.findViewById(R.id.lazy_swipe);

        mDeleteRoom = v.findViewById(R.id.room_edit_deleteroom);
        layoutMap = v.findViewById(R.id.room_edit_layoutmap);


        for (int i = 0 ; i < 10 ; i++){
            String btnID = "room_edit_picturepreviewnr"+ (i+1);
            int resID = getResources().getIdentifier(btnID, "id", Objects.requireNonNull(getActivity()).getPackageName());
            mPictures[i] = v.findViewById(resID);
            Log.i("TORTUGA","loop: "+i);
            mPictures[i].setVisibility(View.GONE);
        }



        final ArrayAdapter adapterPeriodRent = ArrayAdapter.createFromResource(getActivity(), R.array.rending_period_array, R.layout.spinner_item);
        adapterPeriodRent.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPeriodRenting.setAdapter(adapterPeriodRent);

        mRoomDescription = v.findViewById(R.id.room_edit_descriptionField);

        mMapFragment = new MapFragment();
        final FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.room_edit_showmapfragment, mMapFragment).commit();

        // Initialize Places.
        Places.initialize(getApplicationContext(), getString(R.string.google_cloud_api_key));

        // Create a new Places client instance.
        Places.createClient(getActivity());

        // Initialize the AutocompleteSupportFragment.
        mAutocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.room_edit_autocomplete_fragment);

        //autocompleteFragment.setTypeFilter(TypeFilter.GEOCODE);

        // Specify the types of place data to return.
        mAutocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME));




        mAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // txtView.setText(place.getName()+","+place.getId());
                mAddressName = place.getName();
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getLatLng());

                try {
                    layoutMap.setVisibility(View.VISIBLE);
                    mMapFragment.updateSite(place.getLatLng());
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

        mEditRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    postRoom();
                    ((SwipeActivity)getActivity()).changeToPorifleFragment();
                }
            }
        });

        mLazySwipeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mThisPostedRoom.getMatch().setLazySwipe(isChecked);
            }
        });

        mDeleteRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageController.removeAllRoomPictures(mThisPostedRoom.getRoomID());
                Profile p = ProfileSingleton.getInstance();
                p.getLandlord().removeRoomID(mThisPostedRoom.getRoomID());
                ProfileSingleton.update(p);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection(DataBasePath.ROOMS.getValue())
                        .document(mThisPostedRoom.getRoomID())
                        .delete();

                ((SwipeActivity)getActivity()).removeLandlordRoom(mThisPostedRoom);
                ((SwipeActivity)getActivity()).changeToPorifleFragment();
            }
        });


        mTakeRoomPicture = v.findViewById(R.id.room_edit_takeroompicture);
        mTakeRoomPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseImageIntent = ImagePicker.getPickImageIntent(getActivity());
                startActivityForResult(chooseImageIntent, REQUEST_CODE);
            }
        });
        if (getArguments() != null){
            mThisPostedRoom = getArguments().getParcelable(KEY_ROOM);
            initializeRoom();
            initializePictures();
        }else{
            mEditRoom.setText(R.string.confirm);
            mDeleteRoom.setVisibility(View.GONE);
        }
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

    private void postRoom(){
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
                    .withPeriodRenting(mPeriodRenting.getSelectedItemPosition())
                    .withRent(Integer.parseInt(mRent.getText().toString()))
                    .withSize(Integer.parseInt(mRoomSize.getText().toString()))
                    .withDescription(mRoomDescription.getText().toString())
                    .build();

            input.getMatch().setLazySwipe(mLazySwipeSwitch.isChecked());

            Profile p = ProfileSingleton.getInstance();
            if (!(p.getLandlord().getRoomsID().contains(input.getRoomID()))){
                p.getLandlord().addRoomID(input.getRoomID());
                ProfileSingleton.update(p);
            }
                    //FIXME: we are uploading all pictures on each update of the room, even if the pictures remain the same.

                    uploadRoomPictures(input.getRoomID());
                    if (mThisPostedRoom != null)
                        input.setRoomID(mThisPostedRoom.getRoomID());
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(DataBasePath.ROOMS.getValue())
                    .document(input.getRoomID())
                    .set(input);

            ((SwipeActivity)getActivity()).addLandlordRoom(input);


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
    private void initializeRoom(){
        mRent.setText(Integer.toString(mThisPostedRoom.getRent()));
        mDeposit.setText(Integer.toString(mThisPostedRoom.getDeposit()));
        mRoomSize.setText(Integer.toString(mThisPostedRoom.getSize()));
        mInternet.setChecked(mThisPostedRoom.isInternet());
        mFurnished.setChecked(mThisPostedRoom.isFurnished());
        mCommonArea.setChecked(mThisPostedRoom.isComonAreas());
        mLaundry.setChecked(mThisPostedRoom.isLaundry());
        mPeriodRenting.setSelection(mThisPostedRoom.getPeriodOfRenting());
        mRoomDescription.setText(mThisPostedRoom.getDescription());
        mAddressName = mThisPostedRoom.getCompleteAddress();
        mAddressLatitude = mThisPostedRoom.getLatitude();
        mAddressLongitude = mThisPostedRoom.getLongitude();
        mAutocompleteFragment.setText(mThisPostedRoom.getCompleteAddress());
        LatLng coordinates = new LatLng(mThisPostedRoom.getLatitude(), mThisPostedRoom.getLongitude());
        mMapFragment.initializeSite(coordinates);
        mLazySwipeSwitch.setChecked(mThisPostedRoom.getMatch().isLazySwipe());

    }
    //multiple task are working in the same for loop. using the same index when putting pictures.
    private  void initializePictures(){
            Task[] t = ImageController.getAllRoomPicture(mThisPostedRoom.getRoomID());
            for (int i = 0 ; i < 9 ; i++){
                synchronized (this){
                    final  int k = i;
                    t[i].addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap picture;
                            picture = PictureConversion.byteArrayToBitmap(bytes);
                            mPictures[k].setImageBitmap(picture);
                            mPictures[k].setVisibility(View.VISIBLE);
                            keepPicture(picture);
                        }
                    });
                }
        }

    }


}
