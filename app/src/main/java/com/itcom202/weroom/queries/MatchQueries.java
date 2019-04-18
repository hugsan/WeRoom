package com.itcom202.weroom.queries;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.itcom202.weroom.account.profiles.DataBasePath;
import com.itcom202.weroom.account.profiles.LandlordProfile;
import com.itcom202.weroom.account.profiles.Profile;
import java.util.List;
import java.util.Objects;

public class MatchQueries {

    private static Profile userProfile;
    private static List<Profile> tenantCandidates;

    public static List<Profile> getTenantCandidates(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Getting the TenantProfile.
        DocumentReference tenantProfile = db.collection(DataBasePath.USERS.getValue())
                .document(FirebaseAuth.getInstance().getUid());

        tenantProfile.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userProfile = documentSnapshot.toObject(Profile.class);
            }
        });

        //Making a query to all Tenant and looking for the candidates.
        CollectionReference userReference = db
                .collection(DataBasePath.USERS.getValue());
        LandlordProfile landlord = userProfile.getLandlord();

        Query tenantCandidateQuery = userReference
                .whereGreaterThan("tenant", "")
                .whereEqualTo("country",landlord.getTenantNation())
                .whereEqualTo("gender",landlord.getTenantGender())
                .whereGreaterThanOrEqualTo("age", landlord.getTenantMinAge())
                .whereLessThanOrEqualTo("age",landlord.getTenantMaxAge())
                ;
        tenantCandidates = null;
        tenantCandidateQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())){
                        tenantCandidates.add(document.toObject(Profile.class));
                    }
                }
            }
        });
    return tenantCandidates;
    }




}
