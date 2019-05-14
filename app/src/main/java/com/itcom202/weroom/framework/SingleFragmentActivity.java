package com.itcom202.weroom.framework;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.itcom202.weroom.R;

/**
 * SuperClass used for Activities that contains only one fragment in their layout.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment createFragment( );

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_one_fragment );

        FragmentManager fm = getSupportFragmentManager( );
        Fragment fragment = fm.findFragmentById( R.id.fragment_container );

        if ( fragment == null ) {
            fragment = createFragment( );
            fm.beginTransaction( )
                    .add( R.id.fragment_container, fragment )
                    .commit( );
        }
    }


}
