package com.itcom202.weroom.account.profiles;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.itcom202.weroom.R;

public class RoomCreationFragment extends Fragment {
    EditText mRent;
    EditText mDeposit;
    Spinner mPeriodRenting;
    EditText mRoomSize;
    CheckBox mFurnished;
    CheckBox mInternet;
    CheckBox mCommonArea;
    CheckBox mLaundry;
    Button mConfirmRoom;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_room_fragment, null,false);

        mRent = v.findViewById(R.id.rentInput);
        mDeposit = v.findViewById(R.id.deposit);
        mPeriodRenting = v.findViewById(R.id.PeriodRentingRoomFragment);
        mRoomSize = v.findViewById(R.id.roomsizeroomfragment);
        mFurnished = v.findViewById(R.id.checkFurnished);
        mInternet = v.findViewById(R.id.checkBoxInternet);
        mCommonArea = v.findViewById(R.id.checkBoxCommonArea);
        mLaundry = v.findViewById(R.id.checkBoxLaundry);
        mConfirmRoom = v.findViewById(R.id.postRoomButton);



        return v;
    }

}
