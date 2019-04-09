package com.itcom202.weroom.account.authentification;

import android.app.Activity;

import android.content.Context;
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
import com.itcom202.weroom.SingleFragment;
import com.itcom202.weroom.account.profiles.ProfileFragment;

import java.util.Objects;

class FaceBookConnection {
    private static final String TAG = "FaceBookConnection";
    private static CallbackManager mCallbackManager;
    private static Activity context ;


    static void handleFacebookAccessToken(AccessToken token, FirebaseAuth firebaseAuth, final Activity context
    ) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(Objects.requireNonNull(context), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
//                            FirebaseUser user = firebaseAuth.getCurrentUser();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });
    }
    static LoginButton facebookLoginButtonCreate(View v, final Fragment fragment, final FirebaseAuth firebaseAuth,
                                                 final Activity activity) {
        LoginButton loginButtonFB = v.findViewById(R.id.login_button_Fb);
        loginButtonFB.setReadPermissions("email","public_profile");
        loginButtonFB.setFragment(fragment);
        loginButtonFB.registerCallback(getCallBackManager(), new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                FaceBookConnection.handleFacebookAccessToken(loginResult.getAccessToken(),
                        firebaseAuth,activity);

                ((SingleFragment)fragment).changeFragment(new ProfileFragment());
                //activity.startActivity(Profile_Activity.newIntent(activity));
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }
            //TODO condition when the user cancel facebook login?
            @Override
            public void onError(FacebookException error) {
                //TODO notify properly the user when there was an error on the login with FB
                Toast.makeText(context, R.string.connection_error_FB, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "facebook:onError", error);
            }
        });

        return loginButtonFB;
    }
    static CallbackManager getCallBackManager() {
        if (mCallbackManager == null)
            mCallbackManager = CallbackManager.Factory.create();
        return mCallbackManager;
    }


}
