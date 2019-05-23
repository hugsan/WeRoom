package com.itcom202.weroom.interaction.profile.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itcom202.weroom.MainActivity;
import com.itcom202.weroom.framework.ProfileSingleton;
import com.itcom202.weroom.R;
import com.itcom202.weroom.framework.DataBasePath;
import com.itcom202.weroom.account.models.Profile;
import com.itcom202.weroom.framework.queries.ImageController;
import com.itcom202.weroom.interaction.InteractionActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Fragment that allows the user to change the settings of the application and delete the account.
 */
public class SettingFragment extends Fragment {
    private final String TAG = "SettingFragment";
    private Button mDeleteAccount;
    private Switch mBatterySafeMode;
    private List<Task> mDeleteAccountTask = new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting, container, false);

        mDeleteAccount = v.findViewById(R.id.delete_account);
        mBatterySafeMode = v.findViewById(R.id.battery_safe_mode);

        mDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                Profile p = ProfileSingleton.getInstance();

                final FirebaseUser user = firebaseAuth.getCurrentUser();
                Task t1 = null;
                if ( user != null ) {
                    t1 = user.delete();
                }
                mDeleteAccountTask.add(t1);

                Task t2 = db.collection(DataBasePath.USERS.getValue())
                        .document(p.getUserID())
                        .delete();
                mDeleteAccountTask.add(t2);

                if (p.getRole().equals("Landlord")){
                for (String s : p.getLandlord().getRoomsID()){
                    Task t = db.collection(DataBasePath.ROOMS.getValue())
                            .document(s)
                            .delete();
                    ImageController.removeAllRoomPictures(s);
                    mDeleteAccountTask.add(t);
                }}

                Tasks.whenAllSuccess(mDeleteAccountTask.toArray(new Task[mDeleteAccountTask.size()])).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                    @Override
                    public void onSuccess(List<Object> list) {
                        ProfileSingleton.initialize(null);
                        startActivity(MainActivity.newIntent(getActivity()));
                    }
                });
            }
        });

        mBatterySafeMode.setChecked((( InteractionActivity ) Objects.requireNonNull( getActivity( ) ) ).getNotificationOption());
        mBatterySafeMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                (( InteractionActivity ) Objects.requireNonNull( getActivity( ) ) ).changeNotificationOption(isChecked);
            }
        });
        return v;
    }

}
