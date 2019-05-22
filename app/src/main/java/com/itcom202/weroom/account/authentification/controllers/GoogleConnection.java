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
import com.itcom202.weroom.account.onboarding.views.flow.ProfileFragment;
import com.itcom202.weroom.framework.SingleFragment;

/**
 * class that handle the verification between google and our firebaseAuth database.
 */
public class GoogleConnection {
    public static final int RC_SIGN_IN = 101;
    private static GoogleApiClient sGoogleApiClient;

    /**
     * Create a instance of a google client that creates a connection with google API.
     *
     * @param context context where the method is been called.
     * @return return a instance of GoogleApiClient.
     */
    public static GoogleApiClient create( final Context context ) {
        if ( sGoogleApiClient == null ) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder( GoogleSignInOptions.DEFAULT_SIGN_IN )
                    .requestIdToken( context.getResources( ).getString( R.string.default_web_client_id ) )
                    .requestEmail( )
                    .build( );

            sGoogleApiClient = new GoogleApiClient.Builder( context )
                    .enableAutoManage( ( FragmentActivity ) context, new GoogleApiClient.OnConnectionFailedListener( ) {
                        @Override
                        public void onConnectionFailed( @NonNull ConnectionResult connectionResult ) {
                            Toast.makeText( context, R.string.google_connection_failed, Toast.LENGTH_SHORT ).show( );
                        }
                    } )
                    .addApi( Auth.GOOGLE_SIGN_IN_API, gso )
                    .build( );
        }
        return sGoogleApiClient;
    }

    /**
     * Method used in the callback from Google verification intent to log the user into firebaseAuth.
     *
     * @param acct           Account from google where the user has log in.
     * @param activity       Activity where the method is been used.
     * @param firebaseAuth   Instance of the FirebaseAuth where we want to log the user.
     * @param calledActivity Activity that called the Google intent to log the user into google.
     * @param fragment       Fragment where the method is been used.
     */
    public static Task firebaseAuthWithGoogle( GoogleSignInAccount acct, final Context activity,
                                               FirebaseAuth firebaseAuth, final Activity calledActivity, final Fragment fragment ) {
        Log.d( "MainACtivity", "firebaseAuthWithGoogle:" + acct.getId( ) );

        AuthCredential credential = GoogleAuthProvider.getCredential( acct.getIdToken( ), null );
        Task t1 = firebaseAuth.signInWithCredential( credential )
                .addOnCompleteListener( calledActivity, new OnCompleteListener<AuthResult>( ) {
                    @Override
                    public void onComplete( @NonNull Task<AuthResult> task ) {
                        Log.d( "Main", "signInWithCredential:onComplete:" + task.isSuccessful( ) );

                        if ( ! task.isSuccessful( ) ) {
                            Log.w( "MainAcitivyt", "signInWithCredential", task.getException( ) );
                            Toast.makeText( activity, "Authentication failed.",
                                    Toast.LENGTH_SHORT ).show( );
                        } else {
                            ( ( SingleFragment ) fragment ).changeFragment( new ProfileFragment( ) );
                        }
                    }
                } );
        return t1;
    }

    /**
     * Create a intent where the user will be asked to log-in with a Google account.
     *
     * @param googleApiClient instance of the GoogleAPI reference.
     * @return return intent where the user will be asked to log-in with a Google account.
     */
    public static Intent signInIntent( GoogleApiClient googleApiClient ) {
        return Auth.GoogleSignInApi.getSignInIntent( googleApiClient );
    }
}
