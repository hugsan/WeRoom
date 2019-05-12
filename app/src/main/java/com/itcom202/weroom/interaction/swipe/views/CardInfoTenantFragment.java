package com.itcom202.weroom.interaction.swipe.views;

import android.graphics.Bitmap;
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
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.itcom202.weroom.R;
import com.itcom202.weroom.account.onboarding.views.widget.OpenPictureFragment;
import com.itcom202.weroom.account.models.Profile;
import com.itcom202.weroom.account.onboarding.controllers.tagDescription.TagView;
import com.itcom202.weroom.framework.cameraandgallery.PictureConversion;
import com.itcom202.weroom.framework.queries.ImageController;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class CardInfoTenantFragment extends Fragment {
    private ImageButton mButtonExit;
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private ImageView mPhoto;
    private Profile mProfile;
    private TagView mTagView;
    private TextView mTenantName;
    private TextView mTenantAge;
    private TextView mTenantGender;
    private TextView mTenantNation;
    private TextView mSmoker;

    private List<String> tags = new ArrayList<>();
    private static final String KEY_TENANT = "mytenant";


    public static CardInfoTenantFragment newInstance(Profile profile) {
        CardInfoTenantFragment fragment = new CardInfoTenantFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_TENANT, profile);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_card_info_tenant, null, false);

        mTenantName = v.findViewById(R.id.card_tenant_name);
        mTenantAge = v.findViewById(R.id.card_tenant_age);
        mTenantGender = v.findViewById(R.id.card_tenant_gender);
        mTenantNation = v.findViewById(R.id.card_tenant_nation);
        mSmoker = v.findViewById(R.id.card_tenant_smoking);
        mTagView = v.findViewById(R.id.card_tenant_description);
        mPhoto = v.findViewById(R.id.card_tenant_picture);
        if (getArguments() != null)
            mProfile = getArguments().getParcelable(KEY_TENANT);
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



        updateUI();
        return v;
    }

    private void updateUI(){
        mTenantName.setText(mProfile.getName());
        mTenantAge.setText(Integer.toString(mProfile.getAge()));
        mTenantGender.setText(mProfile.getGender());
        Locale l = new Locale("",mProfile.getCountry());
        mTenantNation.setText(l.getDisplayCountry());
        mSmoker.setText(mProfile.getTenant().getSmokeFriendly());

        for (String s : mProfile.getTags())
            mTagView.addTag(s, false);

        Task t = ImageController.getProfilePicture(mProfile.getUserID());

        t.addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(final byte[] bytes) {
                Bitmap picture = PictureConversion.byteArrayToBitmap(bytes);
                mPhoto.setImageBitmap(picture);
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
    }
}