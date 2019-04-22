package com.itcom202.weroom;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.itcom202.weroom.account.LoginActivity;
import com.itcom202.weroom.account.profiles.DataBasePath;
import com.itcom202.weroom.account.profiles.Profile;
import com.itcom202.weroom.queries.MatchQueries;
import com.itcom202.weroom.swipe.SwipeActivity;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Profile userProfile;
    private List<Profile> tenantCandidates = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*

        Task allTenant = MatchQueries.getTenantCandidates();




        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Getting the TenantProfile.


        //Making a query to all Tenant and looking for the candidates.
        CollectionReference userReference = db
                .collection(DataBasePath.USERS.getValue());



        final Query tenantCandidateQuery = FirebaseFirestore.getInstance()
                .collection(DataBasePath.USERS.getValue());


        Task<QuerySnapshot> task = tenantCandidateQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot d : queryDocumentSnapshots){
                    tenantCandidates.add(d.toObject(Profile.class));
                }
            }
        });
        task.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Query removeNonTenant = FirebaseFirestore.getInstance()
                        .collection(DataBasePath.USERS.getValue())
                        .whereEqualTo("tenant", null);
                removeNonTenant.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot d : queryDocumentSnapshots){
                            tenantCandidates.remove(d.toObject(Profile.class));
                        }

                    }
                });
            }
        });*/



        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
       // firebaseAuth.signOut();
        Log.i(TAG,"User logged in: "+ firebaseAuth.getCurrentUser());
        //if there is no user logged in Firebase it starts LoginActivity
        if (firebaseAuth.getCurrentUser() == null){
            Log.i(TAG,"We are not logged as:");
            startActivity(LoginActivity.newIntent(this));
            finish();
        }//IF there is a user logged into Firebase it starts at AccountCreationActivity
        else{
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection(DataBasePath.USERS.getValue())
                    .document(FirebaseAuth.getInstance().getUid());
            Task getUser = docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    userProfile = documentSnapshot.toObject(Profile.class);
                    ProfileSingleton.initialize(userProfile);
                    System.out.println("TORTUGA! query finished");
                }
            });
            Log.i(TAG,"I am logged in: "+ firebaseAuth.getCurrentUser().getEmail());
            getUser.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    startActivity(LoginActivity.newIntent(MainActivity.this));
                    finish();
                }
            });

        }

    }


}

