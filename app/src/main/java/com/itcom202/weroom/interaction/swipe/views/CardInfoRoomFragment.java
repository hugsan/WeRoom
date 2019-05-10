package com.itcom202.weroom.interaction.swipe.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.itcom202.weroom.account.onboarding.views.widget.OpenPictureFragment;
import com.itcom202.weroom.account.models.RoomPosted;
import com.itcom202.weroom.framework.cameraandgallery.PictureConversion;
import com.itcom202.weroom.framework.queries.ImageController;


public class CardInfoRoomFragment extends Fragment {
    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    ImageButton mButtonExit;
    ImageView mPhoto;
    RoomPosted mRoomPosted;
    private static final String KEY_ROOM = "KEY_ROOM";

    public static CardInfoRoomFragment newInstance(RoomPosted room) {
        CardInfoRoomFragment fragment = new CardInfoRoomFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_ROOM, room);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_card_info_room, null, false);

        if (getArguments() != null){
            mRoomPosted = getArguments().getParcelable(KEY_ROOM);
        }
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
        Task t = ImageController.getRoomPicture(mRoomPosted.getRoomID(), 0);
        t.addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(final byte[] bytes) {
                mPhoto.setImageBitmap(PictureConversion.byteArrayToBitmap(bytes));
                mPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                OpenPictureFragment openPic = new OpenPictureFragment();

                Bundle bundle = new Bundle();
               // YourObj obj = SET_YOUR_OBJECT_HERE;
               // bundle.putSerializable("your_obj",ImageController.getRoomPicture(mUserId,0).getResult());
                bundle.putByteArray("picture",bytes);
                openPic.setArguments(bundle);
                ft.replace(android.R.id.content, openPic);
                ft.addToBackStack(null);
                ft.commit();
                    }
                });
            }
        });



        return v;
    }

}
