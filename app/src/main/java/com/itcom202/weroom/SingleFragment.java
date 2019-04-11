package com.itcom202.weroom;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

public abstract class SingleFragment extends Fragment {

    public void changeFragment(Fragment fragment){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }
}
