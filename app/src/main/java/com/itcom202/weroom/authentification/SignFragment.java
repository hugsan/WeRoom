package com.itcom202.weroom.authentification;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.itcom202.weroom.R;
import com.itcom202.weroom.AccountCreationActivity;

public class SignFragment extends Fragment {

    private static final String TAG = "SignFragment";

    private  EditText mEmail, mPasswd, mPasswd2;
    private Button mButtonSignUp;
    private TextView mReferSignIn;
    private FirebaseAuth mFirebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.signup_fragment, container, false);

        mFirebaseAuth = FirebaseAuth.getInstance();

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
                                            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                                            mUser.getIdToken(true)
                                                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                                                            if (task.isSuccessful()) {
                                                                startActivity(AccountCreationActivity.newIntent(getActivity()));

                                                            } else {
                                                                Toast.makeText(getActivity().getApplicationContext(),
                                                                        R.string.login_process_error + task.getException().getMessage(),
                                                                        Toast.LENGTH_LONG).show();                                                            }
                                                        }
                                                    });
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
                startActivity(LoginActivity.newIntent(getActivity()));
            }
        });
        return v;
    }

}

