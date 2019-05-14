package com.itcom202.weroom.account.authentification.controllers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.itcom202.weroom.R;
import com.itcom202.weroom.account.onboarding.views.flow.ProfileFragment;
import com.itcom202.weroom.framework.SingleFragment;

import java.util.Objects;

/**
 * Class that manage the verification with facebook and firebaseauth.
 */
public class FaceBookConnection {
    private static final String TAG = "FaceBookConnection";
    private static CallbackManager mCallbackManager;

    private static void handleFacebookAccessToken( AccessToken token, FirebaseAuth firebaseAuth, final Activity context ) {
        Log.d( TAG, "handleFacebookAccessToken:" + token );
        AuthCredential credential = FacebookAuthProvider.getCredential( token.getToken( ) );
        firebaseAuth.signInWithCredential( credential )
                .addOnCompleteListener( Objects.requireNonNull( context ), new OnCompleteListener<AuthResult>( ) {
                    @Override
                    public void onComplete( @NonNull Task<AuthResult> task ) {
                        if ( task.isSuccessful( ) ) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d( TAG, "signInWithCredential:success" );
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w( TAG, "signInWithCredential:failure", task.getException( ) );
                            Toast.makeText( context, "Authentication failed.",
                                    Toast.LENGTH_SHORT ).show( );
                        }
                    }
                } );
    }

    /**
     * @param v            view from where it is been called.
     * @param fragment     fragment from where it is been called.
     * @param firebaseAuth instance of firebaseauth were we are handling the verification of users.
     * @param activity     activity where the method will be called.
     * @return return a button that opens a new intent and verify the user from facebook with a callback
     */
    public static LoginButton facebookLoginButtonCreate( View v, final Fragment fragment, final FirebaseAuth firebaseAuth,
                                                         final Activity activity ) {
        LoginButton loginButtonFB = v.findViewById( R.id.login_button_Fb );
        loginButtonFB.setReadPermissions( "email", "public_profile" );
        loginButtonFB.setFragment( fragment );
        loginButtonFB.registerCallback( getCallBackManager( ), new FacebookCallback<LoginResult>( ) {
            @Override
            public void onSuccess( LoginResult loginResult ) {
                Log.d( TAG, "facebook:onSuccess:" + loginResult );
                FaceBookConnection.handleFacebookAccessToken( loginResult.getAccessToken( ),
                        firebaseAuth, activity );

                ( ( SingleFragment ) fragment ).changeFragment( new ProfileFragment( ) );
                //activity.startActivity(Profile_Activity.newIntent(activity));
            }

            @Override
            public void onCancel( ) {
                Log.d( TAG, "facebook:onCancel" );
                Toast.makeText( activity, R.string.facebook_disconnected, Toast.LENGTH_SHORT ).show( );
            }

            @Override
            public void onError( FacebookException error ) {
                Toast.makeText( activity, R.string.connection_error_FB, Toast.LENGTH_SHORT ).show( );
                Log.d( TAG, "facebook:onError", error );
            }
        } );

        return loginButtonFB;
    }

    public static CallbackManager getCallBackManager( ) {
        if ( mCallbackManager == null )
            mCallbackManager = CallbackManager.Factory.create( );
        return mCallbackManager;
    }
}
