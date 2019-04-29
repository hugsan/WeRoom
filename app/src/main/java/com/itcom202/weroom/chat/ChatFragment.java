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

import com.google.firebase.auth.FirebaseAuth;
import com.itcom202.weroom.R;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private ListView listView;
    private View btnSend;
    private EditText editText;
    private List<Message> chatMessages;
    private ArrayAdapter<Message> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

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
                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                if (editText.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), "Please input some text...", Toast.LENGTH_SHORT).show();
                } else {
                    //add message to list
                    Message chatMessage = new Message(editText.getText().toString(), userID);
                    chatMessages.add(chatMessage);
                    adapter.notifyDataSetChanged();
                    editText.setText("");
                }
            }
        });

        return v;
    }
}
