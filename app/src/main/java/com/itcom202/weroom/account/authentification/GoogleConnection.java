package com.itcom202.weroom.account.authentification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.itcom202.weroom.AccountCreationActivity;
import com.itcom202.weroom.R;
import com.itcom202.weroom.account.profiles.Profile_Activity;

class GoogleConnection {
    private static GoogleSignInOptions gso;
    static final int RC_SIGN_IN = 101;

    static GoogleApiClient create(Context context){
        if (gso == null){
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(context.getResources().getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
        }

        return  new GoogleApiClient.Builder(context).enableAutoManage((FragmentActivity)context, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                //TODO case where we cannot connect with google
            }
        })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    static void firebaseAuthWithGoogle(GoogleSignInAccount acct, final Context activity,
                                       FirebaseAuth firebaseAuth, Activity calledActivity) {
        Log.d("MainACtivity", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(calledActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Main", "signInWithCredential:onComplete:" + task.isSuccessful());
//
                        if (!task.isSuccessful()) {
                            Log.w("MainAcitivyt", "signInWithCredential", task.getException());
                            Toast.makeText(activity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            // This line has been changed.
                            activity.startActivity(Profile_Activity.newIntent(activity));
                        }
                    }
                });
    }

    static Intent signInIntent( GoogleApiClient googleApiClient) {
        return Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
    }
}
