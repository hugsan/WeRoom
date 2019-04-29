package com.itcom202.weroom.account.authentification;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.itcom202.weroom.MainActivity;
import com.itcom202.weroom.R;
import com.itcom202.weroom.SingleFragment;
import com.itcom202.weroom.account.profiles.ProfileFragment;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Objects;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.google.android.gms.common.GooglePlayServicesUtilLight.GOOGLE_PLAY_STORE_PACKAGE;
import static com.google.android.gms.common.GooglePlayServicesUtilLight.isGooglePlayServicesAvailable;
import static com.google.android.gms.common.GooglePlayServicesUtilLight.isGooglePlayServicesUid;


public class LoginFragment extends SingleFragment {
    final private static String TAG = "LoginFragment";
    final private static int RC_SIGN_IN = 101;
    private static final String EMAIL = "email";

    private CallbackManager mCallbackManager;
    private LoginButton mLoginButtonFb;
    private SignInButton mGoogleSign;
    private EditText mLoginEmail, mLoginPasswd;
    private Button mButtonLogin;
    private TextView mReferSignup;
    private FirebaseAuth firebaseAuth;
    private TextView mForgotPassword;


    GoogleApiClient mGoogleApiClient;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login_fragment,container,false);


        mGoogleApiClient = GoogleConnection.create(getActivity());
        mGoogleSign = v.findViewById(R.id.sign_in_google);
        mGoogleSign.setOnClickListener(new View.OnClickListener() {
            /**
             * Calls google signin activity to identify the user.
             * @param view view from where it is been called.
             */
            @Override
            public void onClick(View view) {
                startActivityForResult(GoogleConnection.signInIntent(mGoogleApiClient), GoogleConnection.RC_SIGN_IN);

            }
        });
        mReferSignup = v.findViewById(R.id.referSignUp);
        //Create the instances connection with Firebase.
        firebaseAuth = FirebaseAuth.getInstance();
        mLoginEmail = v.findViewById(R.id.emailLogIn);
        mLoginPasswd = v.findViewById(R.id.passwordLogIn);
        mButtonLogin = v.findViewById(R.id.buttonLogIn);
        mForgotPassword = v.findViewById(R.id.forgotPass);

        mForgotPassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                changeFragment(new ForgotPasswordFragment());
            }
        });

        mReferSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new SignFragment());
            }
        });
        //Buttons that logs a user in Firebase
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (mLoginEmail.getText().toString().length()==0) {
                    mLoginEmail.setError(getString(R.string.type_email));
                    mLoginEmail.requestFocus();
                } else if (mLoginPasswd.getText().toString().length()==0) {
                    mLoginPasswd.setError(getString(R.string.enter_passw));
                    mLoginPasswd.requestFocus();
                } else if (mLoginEmail.getText().toString().length()==0&& mLoginPasswd.getText().toString().length()==0) {
                    Toast.makeText(getActivity(), getString(R.string.empty_field), Toast.LENGTH_SHORT).show();
                } else if (!(mLoginEmail.getText().toString().length()==0 && mLoginPasswd.getText().toString().length()==0)) {
                    firebaseAuth.signInWithEmailAndPassword(mLoginEmail.getText().toString(), mLoginPasswd.getText().toString())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(getActivity(),getString(R.string.not_succ), Toast.LENGTH_SHORT).show();
                            } else {
                                changeFragment(new ProfileFragment());
                            }
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mLoginButtonFb = FaceBookConnection.facebookLoginButtonCreate(v, this,firebaseAuth,getActivity());


        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        FaceBookConnection.getCallBackManager().onActivityResult(requestCode,resultCode,data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GoogleConnection.RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                GoogleConnection.firebaseAuthWithGoogle(account, getActivity(), firebaseAuth,
                        getActivity(), this);
                changeFragment(new ProfileFragment());

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
                Toast.makeText(getActivity(), R.string.google_connection_failed, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
