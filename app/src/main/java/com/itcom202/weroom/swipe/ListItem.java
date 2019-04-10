package com.itcom202.weroom.swipe;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.itcom202.weroom.R;

public class ListItem extends RecyclerView.ViewHolder {
  TextView textView;
  ImageView mPhoto;

    public ListItem(final View itemView) {
      super(itemView);
        //TODO: add information from Firebase-name, address etc + insert photo preview
        //TODO: make layout
      textView = itemView.findViewById(R.id.text);
      mPhoto = itemView.findViewById(R.id.photoCard);
      mPhoto.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Log.i("tortuga", "on clickkkk");

              AppCompatActivity activity = (AppCompatActivity) itemView.getContext();
              Fragment myFragment = new CardInfoFragment();
              activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();

//              Fragment cardInfoFragment = new CardInfoFragment();
//              FragmentTransaction transaction = SwipeFragment.thisFragment.getChildFragmentManager().beginTransaction();
//              transaction.replace(R.id.fragment_container, cardInfoFragment)
//                      .commit();
          }
      });

  }

  public void bind(int i) {
    textView.setText(String.valueOf(i));
    mPhoto.setImageResource(R.drawable.add_profile_picture);


  }
}
