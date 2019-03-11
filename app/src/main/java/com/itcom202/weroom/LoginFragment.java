package com.itcom202.weroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;


public class LoginFragment extends Fragment {

    CallbackManager mCallbackManager;
    LoginButton mLoginButtonFb;

    public EditText mLoginEmail, mLoginPasswd;
    Button mButtonLogin;
    TextView mReferSignup;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;



    private static final String EMAIL = "email";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       mCallbackManager = CallbackManager.Factory.create();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login_fragment,container,false);

        firebaseAuth = FirebaseAuth.getInstance();
        mLoginEmail = v.findViewById(R.id.emailLogIn);
        mLoginPasswd = v.findViewById(R.id.passwordLogIn);
        mButtonLogin = v.findViewById(R.id.buttonLogIn);
        mReferSignup = v.findViewById(R.id.referSignUp);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(getActivity(), "User logged in ", Toast.LENGTH_SHORT).show();
                    Intent I = new Intent(getActivity(), UserActivity.class);
                    startActivity(I);
                } else {
                    Toast.makeText(getActivity(), "Login to continue", Toast.LENGTH_SHORT).show();
                }
            }
        };
        mReferSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(getActivity(), SingupFragment.class);
                startActivity(I);
            }
        });
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLoginEmail.length()==0) {
                    mLoginEmail.setError("Provide your Email first!");
                    mLoginEmail.requestFocus();
                } else if (mLoginPasswd.length()==0) {
                    mLoginPasswd.setError("Enter Password!");
                    mLoginPasswd.requestFocus();
                } else if (mLoginEmail.length()==0&& mLoginPasswd.length()==0) {
                    Toast.makeText(getActivity(), "Fields Empty!", Toast.LENGTH_SHORT).show();
                } else if (!(mLoginEmail.length()==0 && mLoginPasswd.length()==0)) {
                    firebaseAuth.signInWithEmailAndPassword(mLoginEmail.getText().toString(), mLoginPasswd.getText().toString())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Not sucessfull", Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(new Intent(getActivity(), UserActivity.class));
                            }
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });


        mLoginButtonFb = v.findViewById(R.id.login_button_Fb);
        mLoginButtonFb.setReadPermissions(Arrays.asList(EMAIL));
        mLoginButtonFb.setFragment(this);
        // Callback registration
        mLoginButtonFb.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });




        return v;
    }
}
