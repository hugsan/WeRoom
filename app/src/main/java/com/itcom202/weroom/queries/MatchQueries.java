package com.itcom202.weroom.queries;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.itcom202.weroom.ProfileSingleton;
import com.itcom202.weroom.account.profiles.DataBasePath;
import com.itcom202.weroom.account.profiles.LandlordProfile;
import com.itcom202.weroom.account.profiles.Profile;

import java.util.List;

public class MatchQueries {
    private static DatabaseReference sDatabaseReference = FirebaseDatabase.getInstance().getReference();
/*
    public static List<Profile> getDataBaseRooms(){
        Profile userProfile = ProfileSingleton.getInstance();

        Query landlordQuery = sDatabaseReference.orderByKey().equalTo("4C58FF8QsLTDICvnj2chfJeiRBY2");
        landlordQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }*/
}
