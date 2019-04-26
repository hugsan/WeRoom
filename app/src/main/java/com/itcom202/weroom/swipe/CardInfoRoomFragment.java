package com.itcom202.weroom.swipe;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.itcom202.weroom.R;
import com.itcom202.weroom.account.profiles.Profile;
import com.itcom202.weroom.account.profiles.RoomPosted;
import com.itcom202.weroom.cameraGallery.PictureConversion;
import com.itcom202.weroom.queries.ImageController;

import static com.itcom202.weroom.swipe.TenantState.KEY_ROOM;

public class CardInfoRoomFragment extends Fragment {
    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    ImageButton mButtonExit;
    ImageView mPhoto;
   static RoomPosted mRoomPosted;

    public static CardInfoRoomFragment newInstance(RoomPosted room) {
        CardInfoRoomFragment fragment = new CardInfoRoomFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_ROOM, room);
        fragment.setArguments(bundle);
        mRoomPosted = room;
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_card_info_room, null, false);

        mButtonExit = v.findViewById(R.id.button_exit_info_page_lord);
        mButtonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AppCompatActivity activity = (AppCompatActivity) mButtonExit.getContext();
                activity.getSupportFragmentManager()
                        .popBackStackImmediate();
            }
        });



        mPhoto = v.findViewById(R.id.card_lord_picture);
        Task t = ImageController.getProfilePicture(mRoomPosted.getLandlordID());
        t.addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                mPhoto.setImageBitmap(PictureConversion.byteArrayToBitmap(bytes));
            }
        });


        return v;
    }

}
