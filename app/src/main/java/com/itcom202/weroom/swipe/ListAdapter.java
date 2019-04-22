package com.itcom202.weroom.swipe;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.itcom202.weroom.ProfileSingleton;
import com.itcom202.weroom.R;
import com.itcom202.weroom.account.profiles.DataBasePath;
import com.itcom202.weroom.account.profiles.Profile;
import com.itcom202.weroom.queries.MatchQueries;


import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private List<Profile> mTenantList = SwipeActivity.getTenantList();

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    switch (viewType) {
      case 0: return new LandlordState(LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_card_landlord, parent, false));

      case 2: return new TenantState(LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_card_tenant,parent,false));
    }
    return null;
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
    switch (viewHolder.getItemViewType()) {
      case 0:
        LandlordState viewLandlord = (LandlordState) viewHolder;
        viewLandlord.bind(mTenantList.get(i));
        break;

      case 2:
        TenantState viewTenant = (TenantState) viewHolder;
         // viewTenant.bind(mTenantList.get(i).intValue());

          break;
    }
  }

//  @Override
//  public void onBindViewHolder(@NonNull ListItem holder, int position) {
//         //holder.bind(mTenantList.get(position).intValue());
//     //   holder.bind(Integer.parseInt(mFirebaseUser.getEmail()));
//  }
//
  @Override
  public int getItemCount() {
    return mTenantList.size();
  }

  public List<Profile> getTenantList() {
    return mTenantList;
  }

  public void removeTopItem() {
    mTenantList.remove(0);
    notifyDataSetChanged();
  }
  @Override
  public int getItemViewType(int position) {
    // Just as an example, return 0 or 2 depending on position
    // Note that unlike in ListView adapters, types don't have to be contiguous
    Profile p = ProfileSingleton.getInstance();
    if(p.getRole().equals("Landlord")){
        return 0;
    }else {
        return 2;
    }
  }
}
