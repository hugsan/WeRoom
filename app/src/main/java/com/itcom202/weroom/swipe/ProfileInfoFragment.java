package com.itcom202.weroom.swipe;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.itcom202.weroom.MainActivity;
import com.itcom202.weroom.ProfileSingleton;
import com.itcom202.weroom.R;
import com.itcom202.weroom.account.profiles.Profile;
import com.itcom202.weroom.account.profiles.ProfileFragment;
import com.itcom202.weroom.account.profiles.tagDescription.TagView;
import com.itcom202.weroom.cameraGallery.PictureConversion;
import com.itcom202.weroom.chat.SelectChatFragment;
import com.itcom202.weroom.queries.ImageController;

import java.util.Locale;


public class ProfileInfoFragment extends Fragment {
    private Button mLogoutButton;
    private Button mSettingButton;
    private Button mEditButton;
    private Button mEditSubProfile;
    private TextView mShowName;
    private TextView mShowAge;
    private TextView mShowRole;
    private TextView mShowNation;
    private TextView mShowGender;
    private ImageView mProfilePicture;
    private TagView mTag;
    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_info, null, false);
        mLogoutButton = v.findViewById(R.id.profile_logout_button);
        mShowName = v.findViewById(R.id.profile_username);
        mShowAge = v.findViewById(R.id.profileshow_age);
        mShowRole = v.findViewById(R.id.profile_role);
        mShowNation = v.findViewById(R.id.profiles_nationatility);
        mShowGender = v.findViewById(R.id.profile_gender);
        mProfilePicture = v.findViewById(R.id.profile_profilePhoto);
        mTag = v.findViewById(R.id.profile_tags);
        mEditButton = v.findViewById(R.id.profile_edit_profile);
        mSettingButton = v.findViewById(R.id.profile_account_setting);
        mEditSubProfile = v.findViewById(R.id.modify_sub_profile);

        final Profile p = ProfileSingleton.getInstance();


        if (p.getRole().equals("Landlord"))
            mEditSubProfile.setText(R.string.edit_landlord);
        else
            mEditSubProfile.setText(R.string.edit_tenant);
        for (String s : p.getTags())
            mTag.addTag(s, false);

        mShowName.setText(p.getName());
        mShowAge.setText(Integer.toString(p.getAge()));
        mShowRole.setText(p.getRole());

        Locale obj = new Locale("",p.getCountry());

        mShowNation.setText(obj.getDisplayCountry(Locale.ENGLISH));
        mShowGender.setText(p.getGender());

        Task t = ImageController.getProfilePicture(p.getUserID());

        t.addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(final byte[] bytes) {
                mProfilePicture.setImageBitmap(PictureConversion.byteArrayToBitmap(bytes));
            }
        });

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(MainActivity.newIntent(getActivity()));
            }
        });
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SwipeActivity)getActivity()).changeToProfileEditFragment();
            }
        });
        mSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mEditSubProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (p.getRole().equals("Landlord")){
                    //TODO landlord case.
                }else{
                    ((SwipeActivity)getActivity()).changeToTenantEditFragment();
                }
            }
        });


        return v;
    }
}
