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

import org.w3c.dom.Document;

import java.util.List;
import java.util.Objects;

public class MatchQueries {
    private static String TAG = "MatchQueries";

    private static Profile userProfile;
    private static List<Profile> tenantCandidates;

    public static Task<QuerySnapshot> getTenantCandidates(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Getting the TenantProfile.
        DocumentReference tenantProfile = db.collection(DataBasePath.USERS.getValue())
                .document(FirebaseAuth.getInstance().getUid());


        //Making a query to all Tenant and looking for the candidates.
        CollectionReference userReference = db
                .collection(DataBasePath.USERS.getValue());

        Query tenantCandidateQuery = userReference
                .whereGreaterThan("tenant", "");


        Task<QuerySnapshot> task = tenantCandidateQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot d : queryDocumentSnapshots){
                    d.toObject(Profile.class);
                }
            }
        });

        Task<QuerySnapshot> t = tenantCandidateQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

            }
        });



    return task;
    }




}
