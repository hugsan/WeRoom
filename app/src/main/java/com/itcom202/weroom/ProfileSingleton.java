package com.itcom202.weroom;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.itcom202.weroom.account.profiles.DataBasePath;
import com.itcom202.weroom.account.profiles.Profile;

public class ProfileSingleton {
    private static Profile userProfile;

    private ProfileSingleton(){}

    public static synchronized Profile getInstance(){
        return userProfile;
    }
    public static void initialize(Profile profile){
        userProfile = profile;
    }
    public static void update(Profile profile){
            userProfile = profile;
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection(DataBasePath.USERS.getValue())
                .document(profile.getUserID())
                .set(profile);
}
    public static boolean isFinishedProfile(){
        if (userProfile == null)
            return false;
        return (userProfile.getTenant() != null) ||
                (userProfile.getLandlord() != null && userProfile.getLandlord().getRoomsID() != null
                        && userProfile.getLandlord().getRoomsID().size()>0);
    }
}
