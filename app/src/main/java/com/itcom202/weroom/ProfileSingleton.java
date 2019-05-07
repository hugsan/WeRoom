package com.itcom202.weroom;

import com.google.firebase.firestore.FirebaseFirestore;
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


        boolean answer = (userProfile.getTenant() != null) ||
                (userProfile.getLandlord() != null && userProfile.getLandlord().getRoomsID() != null
                        && userProfile.getLandlord().getRoomsID().size()>0);
        return answer;
    }
}
