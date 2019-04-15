package com.itcom202.weroom.account.authentification;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.itcom202.weroom.R;
import android.widget.Button;
import android.widget.Toast;

public class ForgotPasswordFragment extends Fragment {
    private static final String TAG = "ForgotPasswordFragment";
   private EditText mEmailForgotPass;
   private Button mSendNewPassButton;
   private String email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.forgot_password_fragment, null, false);

        mEmailForgotPass = v.findViewById(R.id.emailForgotPass);
        email = mEmailForgotPass.getText().toString();
        mSendNewPassButton = v.findViewById(R.id.sendNewPassButton);
        mSendNewPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                if(!mEmailForgotPass.getText().toString().equals("")){
                    auth.sendPasswordResetEmail(mEmailForgotPass.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Email sent.");
                                    }
                                    else Toast.makeText(getContext(), getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();

                                }
                            });
                }

            }
        });

        return v;
    }
}
