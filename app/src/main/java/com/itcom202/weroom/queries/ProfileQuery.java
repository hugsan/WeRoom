package com.itcom202.weroom.queries;

import android.os.AsyncTask;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itcom202.weroom.account.profiles.DataBasePath;
import com.itcom202.weroom.account.profiles.Profile;

public class ProfileQuery{
    private static Profile userProfile;

    public static Task userProfileQuery(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection(DataBasePath.USERS.getValue())
                .document(FirebaseAuth.getInstance().getUid());

        Task<DocumentSnapshot> task = docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userProfile = documentSnapshot.toObject(Profile.class);
                System.out.println("TORTUGA! query finished");
            }
        });

        return task;
    }
/*
    @Override
    protected Object doInBackground(Object[] objects) {
        @Override
        protected Object doInBackground(Object[] objects) {

            Task<DocumentSnapshot> documentSnapshotTask = FirebaseFirestore.getInstance().
                    collection((String) objects[0]).document((String) objects[1]).get();

            YourObject obj=null;

            try {
                DocumentSnapshot documentSnapshot = Tasks.await(documentSnapshotTask);

                obj = new YourObject();
                obj.setter(documentSnapshot.get("your field"));

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return obj;
        }
        return null;
    }*/
}
