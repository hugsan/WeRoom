package com.itcom202.weroom.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.itcom202.weroom.ProfileSingleton;
import com.itcom202.weroom.R;
import com.itcom202.weroom.account.profiles.Profile;
import com.itcom202.weroom.account.profiles.RoomPosted;
import com.itcom202.weroom.cameraGallery.PictureConversion;
import com.itcom202.weroom.queries.ImageController;

import java.util.ArrayList;
import java.util.List;

public class SelectChatFragment extends Fragment {
    public static final String KEY_ROOM_LANDLORD = "landlords_rooms";
    private RecyclerView mShowContactsView;
    private ChatAdapter mAdapter;
    private ArrayList<RoomPosted> mLandlordsRooms;
    private RoomPosted mCurrentSelectedRoom;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_selectchat, container, false);
        mShowContactsView = v.findViewById(R.id.contact_recycler_view);
        TabLayout tabLayout = v.findViewById(R.id.tab_layout_top_toolbar);

        if (ProfileSingleton.getInstance().getRole().equals("Landlord")){
            if (getArguments() != null){
                mLandlordsRooms = getArguments().getParcelableArrayList(KEY_ROOM_LANDLORD);
                mCurrentSelectedRoom = mLandlordsRooms.get(0);
            }
            mShowContactsView.setLayoutManager( new LinearLayoutManager(getActivity()));
            List<String> rooms = getRoomsStrings();
            for (String s : rooms){
                tabLayout.addTab(tabLayout.newTab().setText(s));
            }
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    mCurrentSelectedRoom = mLandlordsRooms.get(tab.getPosition());
                    updateUI();
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }else{
            tabLayout.setVisibility(View.GONE);
        }


        updateUI();

        return v;
    }
    private void updateUI(){
        List<String> contacts;
        Profile p = ProfileSingleton.getInstance();
        if (p.getRole().equals("Landlord")){
            contacts = mCurrentSelectedRoom.getMatch().getMatch();
        }else{//the user has a tenant rol.
            contacts = p.getMatch().getMatch();
        }
        mAdapter = new ChatAdapter(contacts);
        mShowContactsView.setAdapter(mAdapter);
    }
    private class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private class ProfileHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView mTitleTextView;
            private TextView mDateTextView;
            private ImageView mProfilePicture;

            private String mContact;

            public ProfileHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater.inflate(R.layout.list_contacts, parent, false));
                itemView.setOnClickListener(this);

                mTitleTextView = itemView.findViewById(R.id.name);
                mDateTextView = itemView.findViewById(R.id.age);
                mProfilePicture = itemView.findViewById(R.id.list_contac_profile);
            }

            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Fragment chatFragment = new ChatFragment();
                Bundle bundle = new Bundle();
                bundle.putString(ChatFragment.PARTNER_ID, createChatID(mContact,ProfileSingleton.getInstance().getUserID()));
                chatFragment.setArguments(bundle);
                transaction.replace(R.id.fragment_container_top, chatFragment);
                transaction.commit();

            }

            public void bind(String contact) {
                mContact = contact;
                mTitleTextView.setText(mContact);
                Task t = ImageController.getProfilePicture(mContact);
                t.addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(final byte[] bytes) {
                        mProfilePicture.setImageBitmap(PictureConversion.byteArrayToBitmap(bytes));
                    }
                });
            }

        }

        private class RoomHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView mTitleTextView;
            private TextView mDateTextView;

            private String mContact;

            public RoomHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater.inflate(R.layout.list_contacts, parent, false));
                itemView.setOnClickListener(this);

                mTitleTextView = itemView.findViewById(R.id.name);
                mDateTextView = itemView.findViewById(R.id.age);
            }

            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Fragment chatFragment = new ChatFragment();
                Bundle bundle = new Bundle();
                bundle.putString(ChatFragment.PARTNER_ID, createChatID(mContact,ProfileSingleton.getInstance().getUserID()));
                chatFragment.setArguments(bundle);
                transaction.replace(R.id.fragment_container_top, chatFragment);
                transaction.commit();
            }

            public void bind(String contact) {
                mContact = contact;
                //TODO get this values from a query
                mTitleTextView.setText(contact);
            }
        }
        private List<String> mChatPartnerIDS;

        public ChatAdapter(List<String> chatPartnerIDS){
            this.mChatPartnerIDS = chatPartnerIDS;
        }
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            if (i == 1)//tenant case
                return new ProfileHolder(layoutInflater, viewGroup);
            return new RoomHolder(layoutInflater, viewGroup);//room case
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            String contact = mChatPartnerIDS.get(i);

            if (viewHolder instanceof ProfileHolder)
                ((ProfileHolder) viewHolder).bind(contact);
            if (viewHolder instanceof RoomHolder)
                ((RoomHolder) viewHolder).bind(contact);
        }

        @Override
        public int getItemCount() {
            return mChatPartnerIDS.size();
        }
        @Override
        public int getItemViewType(int position) {
            String contact = mChatPartnerIDS.get(position);
            if (contact.length() != 36)
                return 1; //tenant case
            else
                return 2; //landlord case
        }
    }

    private List<String> getRoomsStrings(){
        List<String> roomsName = new ArrayList<>();
        for (RoomPosted p : mLandlordsRooms)
            roomsName.add(p.getCompleteAddress());

        return roomsName;
    }
    private String createChatID(String ID_one, String ID_two){
        String chat_ID;
        if (ID_one.compareTo(ID_two) < 0)
            chat_ID = ID_one + "_" + ID_two;
        else
            chat_ID = ID_two + "_" + ID_one;
        return chat_ID;
    }

}
