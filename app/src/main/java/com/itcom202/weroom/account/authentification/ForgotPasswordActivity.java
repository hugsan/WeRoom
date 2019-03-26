package com.itcom202.weroom.account.authentification;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.itcom202.weroom.SingleFragmentActivity;

public class ForgotPasswordActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ForgotPasswordFragment();}


    public static Intent newIntent (Context myContext) {
        Intent i = new Intent(myContext, ForgotPasswordActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
    }

}
