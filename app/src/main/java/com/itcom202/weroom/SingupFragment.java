package com.itcom202.weroom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.content.SharedPreferences;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.SignInMethodQueryResult;

import static android.content.Context.MODE_PRIVATE;

public class SingupFragment extends Fragment {

    private static final String TAG = "SignupFragment";
    public static final String MY_PREF_NAME = "WeRoomPreferences";
    public static final String LOG_KEY = "log_token";

    public EditText mEmail, mPasswd, mPasswd2;
    Button mButtonSignUp;
    TextView mReferSignIn;
    FirebaseAuth mFirebaseAuth;
    CheckBox mKeepLogged;
    SharedPreferences mSettings;
    SharedPreferences.Editor mPrefEditor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         mSettings = getActivity().getSharedPreferences(MY_PREF_NAME, MODE_PRIVATE);
         mPrefEditor = mSettings.edit();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.signup_fragment, container, false);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mEmail = v.findViewById(R.id.emailSignUp);
        mPasswd = v.findViewById(R.id.passwordSignUp);
        mPasswd2 = v.findViewById(R.id.repeatPassword);
        mKeepLogged = v.findViewById(R.id.keeplogin);

        mButtonSignUp = v.findViewById(R.id.buttonSignUp);
        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd1 = mPasswd.getText().toString();
                String pwd2 = mPasswd2.getText().toString();
                if (mEmail.getText().toString().length() == 0) {
                    mEmail.setError("Provide your Email first!");
                    mEmail.requestFocus();
                } else if (mPasswd.getText().toString().length() == 0) {
                    mPasswd.setError("Set your password!");
                    mPasswd.requestFocus();
                } else if (!pwd1.equals(pwd2)) {
                    mPasswd.setError("The two passwords should be the same");
                    mPasswd.requestFocus();
                    mPasswd2.requestFocus();
                } else if (mEmail.getText().toString().length() == 0 || pwd1.length() == 0 || pwd2.length() == 0) {
                    Toast.makeText(getActivity(), getString(R.string.empty_fields), Toast.LENGTH_SHORT).show();
                } else if (!(mEmail.getText().toString().length() == 0 || pwd1.length() == 0 || pwd2.length() == 0))
                {
                    mFirebaseAuth.createUserWithEmailAndPassword(mEmail.getText().toString(), mPasswd2.getText().toString())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        if (mKeepLogged.isChecked()){
                                            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                                            mUser.getIdToken(true)
                                                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                                                            if (task.isSuccessful()) {
                                                                String idToken = task.getResult().getToken();
                                                                Log.i(TAG, "We put the IdToken: "+idToken);
                                                                mPrefEditor.putString(LOG_KEY, idToken);

                                                                Fragment nextFragment = new AccountCreationFragment();
                                                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                                                fragmentTransaction.replace(R.id.fragment_container, nextFragment);
                                                                fragmentTransaction.addToBackStack(null);
                                                                fragmentTransaction.commit();

                                                            } else {
                                                                Toast.makeText(getActivity().getApplicationContext(),
                                                                        R.string.login_process_error + task.getException().getMessage(),
                                                                        Toast.LENGTH_LONG).show();                                                            }
                                                        }
                                                    });
                                        }

                                    } else {
                                        //TODO stuff i dont know yet!!!!
                                        Toast.makeText(getActivity().getApplicationContext(),
                                                "SignUp unsuccessful: " + task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    }
                                });
                }
                else {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mReferSignIn = v.findViewById(R.id.referLogIn);

        mReferSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment nextFragment = new LoginFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, nextFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return v;

    }

}

