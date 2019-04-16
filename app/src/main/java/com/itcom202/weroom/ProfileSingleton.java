package com.itcom202.weroom;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.itcom202.weroom.account.profiles.DataBasePath;
import com.itcom202.weroom.account.profiles.Profile;

public class ProfileSingleton {
    private static Profile userProfile;

    private ProfileSingleton(){}

    public static synchronized Profile getInstance(){
        if (userProfile == null){
            queryUser();
        }
        return userProfile;
    }

    private static void queryUser(){
        DatabaseReference dbLandlordReference =
                FirebaseDatabase.getInstance().getReference().child(DataBasePath.USERS.getValue());
        Query userProfileQuery = dbLandlordReference.orderByKey().equalTo(FirebaseAuth.getInstance().getUid());
        userProfileQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 userProfile = dataSnapshot.getValue(Profile.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    /* DocumentReference docRef = db.collection(DataBasePath.USERS.getValue())
                                .document(userID);
                        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Profile p = documentSnapshot.toObject(Profile.class);
                            }
                        });*/
}
