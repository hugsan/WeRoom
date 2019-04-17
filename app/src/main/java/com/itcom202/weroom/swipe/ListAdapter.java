package com.itcom202.weroom.swipe;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.itcom202.weroom.R;
import com.itcom202.weroom.account.profiles.ProfileFragment;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private List<Integer> items = new ArrayList<>(Arrays.asList(0,1,2,3,4,5,6,7,8,9));

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
        viewLandlord.bind(items.get(i).intValue());
        break;

      case 2:
        TenantState viewTenant = (TenantState) viewHolder;
          viewTenant.bind(items.get(i).intValue());

          break;
    }
  }

//  @Override
//  public void onBindViewHolder(@NonNull ListItem holder, int position) {
//         //holder.bind(items.get(position).intValue());
//     //   holder.bind(Integer.parseInt(mFirebaseUser.getEmail()));
//  }
//
  @Override
  public int getItemCount() {
    return items.size();
  }

  public List<Integer> getItems() {
    return items;
  }

  public void removeTopItem() {
    items.remove(0);
    notifyDataSetChanged();
  }
  @Override
  public int getItemViewType(int position) {
    // Just as an example, return 0 or 2 depending on position
    // Note that unlike in ListView adapters, types don't have to be contiguous
    if(ProfileFragment.mRole.getSelectedItemPosition()==0){
        return 0;
    }else {
        return 2;
    }
  }
}
