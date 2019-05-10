package com.itcom202.weroom.interaction.chat.views;

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
import com.itcom202.weroom.framework.ProfileSingleton;
import com.itcom202.weroom.R;
import com.itcom202.weroom.framework.DataBasePath;
import com.itcom202.weroom.interaction.chat.controllers.MessageAdapter;
import com.itcom202.weroom.interaction.chat.models.Message;
import com.itcom202.weroom.account.models.Profile;
/*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;
*/
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
    private String mChatID;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        if (getArguments() != null)
            mChatID = getArguments().getString(PARTNER_ID);
/*
        KeyboardVisibilityEvent.setEventListener(
                getActivity(),
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        // some code depending on keyboard visiblity status
                    }
                });
*/
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
                            .getReference(DataBasePath.CHAT.getValue()).child(mChatID);
                    String key = messageRef.push().getKey();
                    messageRef.child(key).setValue(chatMessage);

                    chatMessages.add(chatMessage);
                    adapter.notifyDataSetChanged();
                    editText.setText("");
                }
            }
        });
        DatabaseReference messageRef = FirebaseDatabase.getInstance()
                .getReference(DataBasePath.CHAT.getValue()).child(mChatID);
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

}
