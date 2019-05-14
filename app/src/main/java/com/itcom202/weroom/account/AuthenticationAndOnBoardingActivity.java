package com.itcom202.weroom.account;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.itcom202.weroom.account.authentification.views.LoginFragment;
import com.itcom202.weroom.framework.SingleFragmentActivity;

/**
 * Activity that handles all the fragments in Log-in process and on boarding flow.
 */
public class AuthenticationAndOnBoardingActivity extends SingleFragmentActivity {

    public static Intent newIntent( Context myContext ) {
        Intent i = new Intent( myContext, AuthenticationAndOnBoardingActivity.class );
        i.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
        return i;
    }

    @Override
    protected Fragment createFragment( ) {
        return new LoginFragment( );
    }

}
