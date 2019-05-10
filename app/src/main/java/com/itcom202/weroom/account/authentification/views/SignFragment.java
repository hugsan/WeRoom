package com.itcom202.weroom.account.authentification.views;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.itcom202.weroom.R;
import com.itcom202.weroom.framework.SingleFragment;
import com.itcom202.weroom.account.authentification.controllers.GoogleConnection;
import com.itcom202.weroom.account.onboarding.views.flow.ProfileFragment;

import java.util.Objects;

public class SignFragment extends SingleFragment {

    private static final String TAG = "SignFragment";
    private  EditText mEmail, mPasswd, mPasswd2;
    private Button mButtonSignUp;
    private TextView mReferSignIn;
    private FirebaseAuth mFirebaseAuth;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton mGoogleSign;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.signup_fragment, container, false);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mGoogleApiClient = GoogleConnection.create(getActivity());
        //GoogleSign in Button
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

        mEmail = v.findViewById(R.id.emailSignUp);
        mPasswd = v.findViewById(R.id.passwordSignUp);
        mPasswd2 = v.findViewById(R.id.repeatPassword);

        mButtonSignUp = v.findViewById(R.id.buttonSignUp);
        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd1 = mPasswd.getText().toString();
                String pwd2 = mPasswd2.getText().toString();
                if (mEmail.getText().toString().length() == 0) {
                    mEmail.setError(getString(R.string.type_email));
                    mEmail.requestFocus();
                } else if (0 == mPasswd.getText().toString().length()) {
                    mPasswd.setError(getString(R.string.set_passw));
                    mPasswd.requestFocus();
                } else if (!pwd1.equals(pwd2)) {
                    mPasswd.setError(getString(R.string.same_passw));
                    mPasswd.requestFocus();
                    mPasswd2.requestFocus();
                } else if (mEmail.getText().toString().length() == 0 || pwd1.length() == 0 || pwd2.length() == 0) {
                    Toast.makeText(getActivity(), R.string.empty_fields, Toast.LENGTH_SHORT).show();
                } else if (!(mEmail.getText().toString().length() == 0 || pwd1.length() == 0 || pwd2.length() == 0))
                {
                    mFirebaseAuth.createUserWithEmailAndPassword(mEmail.getText().toString(), mPasswd2.getText().toString())
                            .addOnCompleteListener(Objects.requireNonNull(getActivity()), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                                            mUser.getIdToken(true)
                                                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                                                            if (task.isSuccessful()) {
                                                                changeFragment(new ProfileFragment());}

                                                           else {
                                                                Toast.makeText(getActivity(), R.string.login_process_error, Toast.LENGTH_SHORT).show();
//                                                                Toast.makeText(getActivity().getApplicationContext(),
//                                                                        R.string.login_process_error + task.getException().getMessage(),
//                                                                        Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });
                                    } else {
                                        //TODO stuff i don't know yet!!!!
                                       // Toast.makeText(getActivity().getApplicationContext(),
                                             //   R.string.not_succ + task.getException().getMessage(),
                                             //   Toast.LENGTH_SHORT).show();
                                        Toast.makeText(getActivity(), R.string.email_in_use, Toast.LENGTH_SHORT).show();
                                    }
                                    }
                                });
                }
                else {
                    Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                }
            }
        });

        mReferSignIn = v.findViewById(R.id.referLogIn);

        mReferSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new LoginFragment());
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //FB stuff
        //mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GoogleConnection.RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                GoogleConnection.firebaseAuthWithGoogle(account, getActivity(), mFirebaseAuth, getActivity(), this);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, getString(R.string.google_fail), e);
                // ...
                Toast.makeText(getActivity(), R.string.acc_creation_failed, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
