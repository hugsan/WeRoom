package com.itcom202.weroom.account.authentification.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import com.itcom202.weroom.R;
import com.itcom202.weroom.framework.SingleFragment;
import com.itcom202.weroom.account.onboarding.views.flow.ProfileFragment;

public class GoogleConnection {
    private static GoogleSignInOptions gso;
    private static GoogleApiClient sGoogleApiClient;
    public static final int RC_SIGN_IN = 101;

    public static GoogleApiClient create(final Context context){
        if (sGoogleApiClient == null) {
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(context.getResources().getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            sGoogleApiClient = new GoogleApiClient.Builder(context).enableAutoManage((FragmentActivity) context, new GoogleApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                    Toast.makeText(context, R.string.google_connection_failed, Toast.LENGTH_SHORT).show();
                }
            })
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }
        return sGoogleApiClient;
    }

    public static void firebaseAuthWithGoogle(GoogleSignInAccount acct, final Context activity,
                                       FirebaseAuth firebaseAuth, final Activity calledActivity, final Fragment fragment) {
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
                            ((SingleFragment)fragment).changeFragment(new ProfileFragment());
                            /*Fragment newFragment = new ProfileFragment();
                            FragmentTransaction transaction = fragment.getFragmentManager().beginTransaction();


                            // Replace whatever is in the fragment_container view with this fragment,
                            // and add the transaction to the back stack
                            transaction.replace(R.id.fragment_container, newFragment);
                            transaction.addToBackStack(null);

                            // Commit the transaction
                            transaction.commit();
                            activity.startActivity(Profile_Activity.newIntent(activity));*/
                        }
                    }
                });
    }

    public static Intent signInIntent( GoogleApiClient googleApiClient) {
        return Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
    }
}