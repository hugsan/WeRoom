package com.itcom202.weroom.swipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.itcom202.weroom.ProfileSingleton;
import com.itcom202.weroom.R;
import com.itcom202.weroom.account.profiles.DataBasePath;
import com.itcom202.weroom.account.profiles.Profile;
import com.itcom202.weroom.account.profiles.RoomPosted;

import java.util.ArrayList;
import java.util.List;


public class SwipeActivity extends AppCompatActivity {
    private static List<Profile> sTenantList = new ArrayList<>();
    private static List<RoomPosted> sRoomPostedList = new ArrayList<>();

    public static Intent newIntent(Context myContext){
        Intent i = new Intent(myContext, SwipeActivity.class);
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (ProfileSingleton.getInstance().getRole().equals("Landlord")){

                //Making a query to all Tenant and looking for the candidates.
                CollectionReference userReference = db
                        .collection(DataBasePath.USERS.getValue());

                final Query tenantCandidateQuery = db.collection(DataBasePath.USERS.getValue());

                tenantCandidateQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot d : queryDocumentSnapshots){
                            sTenantList.add(d.toObject(Profile.class));
                        }
                        Query removeNonTenant = FirebaseFirestore.getInstance()
                                .collection(DataBasePath.USERS.getValue())
                                .whereEqualTo("tenant", null);
                        Task task = removeNonTenant.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (DocumentSnapshot d : queryDocumentSnapshots){
                                    sTenantList.remove(d.toObject(Profile.class));
                                }
                            }
                        });
                        task.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                startFragment();
                            }
                        });
                    }
                });


            }else{

            CollectionReference roomReference = db
                    .collection(DataBasePath.ROOMS.getValue());

            final Query RoomQuery = db.collection(DataBasePath.ROOMS.getValue());

            RoomQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (DocumentSnapshot d : queryDocumentSnapshots){
                        sRoomPostedList.add(d.toObject(RoomPosted.class));
                    }
                    startFragment();
//                    Query removeNonTenant = FirebaseFirestore.getInstance()
//                            .collection(DataBasePath.USERS.getValue())
//                            .whereEqualTo("tenant", null);
//                    Task task = removeNonTenant.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                        @Override
//                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                            for (DocumentSnapshot d : queryDocumentSnapshots){
//                                sTenantList.remove(d.toObject(Profile.class));
//                            }
//
//                        }
//                    });
//                    task.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            startFragment();
//
//                        }
//                    });
                }
            });
        }




    }
    private void startFragment(){
        setContentView(R.layout.activity_one_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null){
            fragment = new SwipeFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    public static  List<Profile> getTenantList() {
        return sTenantList;
    }
    public static  List<RoomPosted> getRoomPostedList() { return sRoomPostedList; }
}
