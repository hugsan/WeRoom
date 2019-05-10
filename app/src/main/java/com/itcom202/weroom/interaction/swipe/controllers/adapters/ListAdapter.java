package com.itcom202.weroom.interaction.swipe.controllers.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.itcom202.weroom.R;
import com.itcom202.weroom.account.models.Profile;
import com.itcom202.weroom.account.models.RoomPosted;
import com.itcom202.weroom.interaction.swipe.views.holders.LandlordState;
import com.itcom202.weroom.interaction.swipe.views.holders.TenantState;


import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private List<Profile> mTenantList;
  private List<RoomPosted> mRoomPostedFromLandlord;
  private List<RoomPosted> mAllRoomPosted;
  private Profile p;

  public ListAdapter(List<Profile> tenantList, List<RoomPosted> roomPosteds, List<RoomPosted> allRooms, Profile p){
    mTenantList = tenantList;
    mRoomPostedFromLandlord = roomPosteds;
    mAllRoomPosted = allRooms;
    this.p = p;

  }


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
        viewTenant.bind(mAllRoomPosted.get(i));

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
    if(p.getRole().equals("Landlord"))
      return mTenantList.size();
    else
      return mAllRoomPosted.size();
  }

  public List<Profile> getTenantList() {
    return mTenantList;
  }
  public List<RoomPosted> getRoomPostedFromLandlord(){return mRoomPostedFromLandlord;}

  public void removeTopItem() {
    if(p.getRole().equals("Landlord")){
      mTenantList.remove(0);
      notifyDataSetChanged();
    }else{
      mAllRoomPosted.remove(0);
      notifyDataSetChanged();
    }
  }
  public String returnTopItemID(){
      if (p.getRole().equals("Landlord")){
          return mTenantList.get(0).getUserID();
      }else{
          return mAllRoomPosted.get(0).getRoomID();
      }

  }
  public RoomPosted returnTopRoomLandlord(){
    return mRoomPostedFromLandlord.get(0);
  }
  public Profile returnTopTenant(){
    return mTenantList.get(0);
  }
  public RoomPosted returnTopRoom(){
    return mAllRoomPosted.get(0);
  }
  @Override
  public int getItemViewType(int position) {
    if(p.getRole().equals("Landlord")){
        return 0;
    }else {
        return 2;
    }
  }
}
