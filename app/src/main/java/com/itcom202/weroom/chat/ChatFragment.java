package com.itcom202.weroom.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itcom202.weroom.ProfileSingleton;
import com.itcom202.weroom.R;
import com.itcom202.weroom.account.profiles.DataBasePath;
import com.itcom202.weroom.account.profiles.Profile;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {
    static public final String PARTNER_ID = "parner_id";
    private ListView listView;
    private View btnSend;
    private EditText editText;
    private List<Message> chatMessages;
    private ArrayAdapter<Message> adapter;
    private Profile mProfile = ProfileSingleton.getInstance();
    private String mChatPartnerID;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        if (getArguments() != null)
            mChatPartnerID = getArguments().getString(PARTNER_ID);

        chatMessages = new ArrayList<>();

        listView    =   v.findViewById(R.id.list_msg);
        btnSend     =   v.findViewById(R.id.btn_chat_send);
        editText    =   v.findViewById(R.id.msg_type);

        adapter = new MessageAdapter(getActivity(), R.layout.item_chat_left, chatMessages);
        listView.setAdapter(adapter);

        //event for button SEND
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), "Please input some text...", Toast.LENGTH_SHORT).show();
                } else {
                    //add message to list
                    Message chatMessage = new Message(editText.getText().toString(), mProfile.getName(),mProfile.getUserID());

                    DatabaseReference messageRef = FirebaseDatabase.getInstance()
                            .getReference(DataBasePath.CHAT.getValue()).child(thisChatID());
                    String key = messageRef.push().getKey();
                    messageRef.child(key).setValue(chatMessage);

                    chatMessages.add(chatMessage);
                    adapter.notifyDataSetChanged();
                    editText.setText("");
                }
            }
        });
        DatabaseReference messageRef = FirebaseDatabase.getInstance()
                .getReference(DataBasePath.CHAT.getValue()).child(thisChatID());
        messageRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message message = dataSnapshot.getValue(Message.class);
                if (!message.getSenderID().equals(mProfile.getUserID())){
                    chatMessages.add(message);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return v;
    }

    private String thisChatID(){
        String ID_one = mProfile.getUserID();
        String ID_two = mChatPartnerID;
        String chat_ID;
        if (ID_one.compareTo(ID_two) < 0)
            chat_ID = ID_one + "_" + ID_two;
        else
            chat_ID = ID_two + "_" + ID_one;
        return chat_ID;
    }
}
