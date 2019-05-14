package com.itcom202.weroom.framework;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.itcom202.weroom.R;

/**
 * Superclass used for fragment that are used in Activities with a single container.
 */
public abstract class SingleFragment extends Fragment {

    public void changeFragment( Fragment fragment ) {
        FragmentTransaction transaction;
        if ( getFragmentManager( ) != null ) {
            transaction = getFragmentManager( ).beginTransaction( );
            transaction.replace( R.id.fragment_container, fragment );
            transaction.addToBackStack( null );

            transaction.commit( );
        }

    }
}
