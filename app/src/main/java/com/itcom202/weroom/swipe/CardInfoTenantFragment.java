package com.itcom202.weroom.swipe;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.itcom202.weroom.R;
import com.itcom202.weroom.account.profiles.Profile;
import com.itcom202.weroom.account.profiles.RoomPosted;
import com.itcom202.weroom.account.profiles.tagDescription.TagClickListener;
import com.itcom202.weroom.account.profiles.tagDescription.TagModel;
import com.itcom202.weroom.account.profiles.tagDescription.TagView;
import com.itcom202.weroom.cameraGallery.PictureConversion;
import com.itcom202.weroom.queries.ImageController;

import java.util.ArrayList;
import java.util.List;

import static com.itcom202.weroom.swipe.LandlordState.KEY_TENANT;
import static com.itcom202.weroom.swipe.TenantState.KEY_ROOM;

public class CardInfoTenantFragment extends Fragment {
    ImageButton mButtonExit;
    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    ImageView mPhoto;
    static Profile mProfile;
    TagView mTagView;
    private List<String> tags = new ArrayList<>();


    public static CardInfoTenantFragment newInstance(Profile profile) {
        CardInfoTenantFragment fragment = new CardInfoTenantFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_TENANT, profile);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_card_info_tenant, null, false);

        mButtonExit = v.findViewById(R.id.button_exit_info_page_tenant);
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
        Task t = ImageController.getProfilePicture(mProfile.getUserID());
        t.addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                mPhoto.setImageBitmap(PictureConversion.byteArrayToBitmap(bytes));
            }
        });

        mTagView = v.findViewById(R.id.card_tenant_description);

//
//        for(TagModel model:  mTagView.getSelectedTags()){
//            tags.add(model.getTagText());
//        }





            return v;
    }

}