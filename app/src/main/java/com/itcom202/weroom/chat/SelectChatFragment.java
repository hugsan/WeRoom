package com.itcom202.weroom.chat;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.itcom202.weroom.ProfileSingleton;
import com.itcom202.weroom.R;
import com.itcom202.weroom.account.profiles.DataBasePath;
import com.itcom202.weroom.account.profiles.Profile;
import com.itcom202.weroom.account.profiles.RoomPosted;
import java.util.ArrayList;
import java.util.List;

public class SelectChatFragment extends Fragment {
    private RecyclerView mShowContactsView;
    private ChatAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_selectchat, container, false);
        mShowContactsView = v.findViewById(R.id.contact_recycler_view);
        mShowContactsView.setLayoutManager( new LinearLayoutManager(getActivity()));
        updateUI();

        return v;
    }
    private void updateUI(){
        Profile p = ProfileSingleton.getInstance();
        final List<String> matches = p.getMatch().getMatch();

        final Query getLandlordsRooms = FirebaseFirestore.getInstance()
                .collection(DataBasePath.ROOMS.getValue());

        Query allProfilesInDB = FirebaseFirestore.getInstance()
                .collection(DataBasePath.USERS.getValue());
        final List<ShowContact> contacts = new ArrayList<>();
        final List<Profile> allProfiles = new ArrayList<>();
        final List<RoomPosted> matchingRooms = new ArrayList<>();
        final Task t1 = allProfilesInDB.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot d : queryDocumentSnapshots) {
                    Profile p = d.toObject(Profile.class);
                    allProfiles.add(p);
                    if (p != null && matches.contains(p.getUserID()))
                        contacts.add(new ShowContact(p.getName(), null, p.getUserID(), null));
                }
                    getLandlordsRooms.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (DocumentSnapshot d : queryDocumentSnapshots){
                                RoomPosted r = d.toObject(RoomPosted.class);
                                if (r != null && matches.contains(r.getRoomID())){
                                    for (Profile p : allProfiles){
                                        if (r.getLandlordID().equals(p.getUserID())){
                                            contacts.add(new ShowContact(p.getName(),r.getAddressID(),p.getUserID(),null));
                                        }
                                    }
                                }
                            }
                            mAdapter = new ChatAdapter(contacts);
                            mShowContactsView.setAdapter(mAdapter);
                        }
                    });
                }
        });
        Task t2 = getLandlordsRooms.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot d : queryDocumentSnapshots){
                    RoomPosted r = d.toObject(RoomPosted.class);
                    if (r != null && matches.contains(r.getRoomID())){

                    }

                }
            }
        });
    }
    private class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private class ProfileHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView mTitleTextView;
            private TextView mDateTextView;

            private ShowContact mContact;

            public ProfileHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater.inflate(R.layout.list_contacts, parent, false));
                itemView.setOnClickListener(this);

                mTitleTextView = itemView.findViewById(R.id.crime_title);
                mDateTextView = itemView.findViewById(R.id.crime_date);
            }

            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Fragment chatFragment = new ChatFragment();
                Bundle bundle = new Bundle();
                bundle.putString(ChatFragment.PARTNER_ID, mContact.contactID);
                chatFragment.setArguments(bundle);
                transaction.replace(R.id.fragment_container_top, chatFragment);
                transaction.commit();

            }

            public void bind(ShowContact contact) {
                mContact = contact;
                mTitleTextView.setText(mContact.name);
            }

        }

        private class RoomHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView mTitleTextView;
            private TextView mDateTextView;

            private ShowContact mContact;

            public RoomHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater.inflate(R.layout.list_contacts, parent, false));
                itemView.setOnClickListener(this);

                mTitleTextView = itemView.findViewById(R.id.crime_title);
                mDateTextView = itemView.findViewById(R.id.crime_date);
            }

            @Override
            public void onClick(View v) {
                //TODO implement what happen when we click on room.
            }

            public void bind(ShowContact contact) {
                mContact = contact;
                mTitleTextView.setText(mContact.name);
                mDateTextView.setText(mContact.roomAddress);
            }
        }
        private List<ShowContact> mContacts;
        public ChatAdapter(List<ShowContact> contacts){
            mContacts = contacts;
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
            ShowContact contact = mContacts.get(i);

            if (viewHolder instanceof ProfileHolder)
                ((ProfileHolder) viewHolder).bind(contact);
            if (viewHolder instanceof RoomHolder)
                ((RoomHolder) viewHolder).bind(contact);
        }

        @Override
        public int getItemCount() {
            return mContacts.size();
        }
        @Override
        public int getItemViewType(int position) {
            ShowContact contact = mContacts.get(position);
            if (contact.roomAddress == null)
                return 1; //tenant case
            else
                return 2; //landlord case
        }
    }
    private class ShowContact{
        String name;
        String contactID;
        String roomAddress;
        Bitmap profilePicture;
        ShowContact(String name, String room,String contactID, Bitmap btmp){
            this.name = name;
            this.roomAddress = room;
            this.profilePicture = btmp;
            this.contactID = contactID;
        }
    }

}
