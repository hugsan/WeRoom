package com.itcom202.weroom.interaction.chat.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itcom202.weroom.R;
import com.itcom202.weroom.account.models.Profile;
import com.itcom202.weroom.framework.DataBasePath;
import com.itcom202.weroom.framework.ProfileSingleton;
import com.itcom202.weroom.interaction.InteractionActivity;
import com.itcom202.weroom.interaction.chat.controllers.MessageAdapter;
import com.itcom202.weroom.interaction.chat.models.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Fragment that displays a live chat room for the users.
 */
public class ChatFragment extends Fragment {
    static public final String KET_CHAT_ID = "chat_id";
    static public final String KEY_CHAT_PARTNER = "chat_partner";
    private ListView listView;
    private View btnSend;
    private EditText editText;
    private List<Message> chatMessages;
    private ArrayAdapter<Message> adapter;
    private Profile mProfile = ProfileSingleton.getInstance( );
    private String mChatID;
    private TextView mChatName;
    private ImageButton mButtonBack;

    @Nullable
    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
        View v = inflater.inflate( R.layout.fragment_chat, container, false );
        if ( getArguments( ) != null ) {
            mChatID = getArguments().getString(KET_CHAT_ID);
        }
        chatMessages = new ArrayList<>( );

        listView = v.findViewById( R.id.list_msg );
        btnSend = v.findViewById( R.id.btn_chat_send );
        editText = v.findViewById( R.id.msg_type );
        mChatName = v.findViewById(R.id.nameChat);
        mChatName.setText(getArguments().getString(KEY_CHAT_PARTNER));
        mButtonBack = v.findViewById(R.id.backbutton_chat);
        mButtonBack.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick( View v ) {
                ((InteractionActivity) Objects.requireNonNull(getActivity())).changeToChatSelection();
            }
        } );
        adapter = new MessageAdapter( getActivity( ), R.layout.item_chat_left, chatMessages );
        listView.setAdapter( adapter );



        createSendMessageListener( );
        newMessageListener( );
        return v;
    }

    /**
     * Initialize the btnSend to send message from editText to the FireBase RealtimeDatabase.
     * after sending the message it reset the editText to have a empty string.
     */
    public void createSendMessageListener( ) {
        //event for button SEND
        btnSend.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick( View v ) {
                if ( editText.getText( ).toString( ).trim( ).equals( "" ) ) {
                    Toast.makeText( getActivity( ), "Please input some text...", Toast.LENGTH_SHORT ).show( );
                } else {
                    //add message to list
                    Message chatMessage = new Message( editText.getText( ).toString( ), mProfile.getName( ), mProfile.getUserID( ) );

                    DatabaseReference messageRef = FirebaseDatabase.getInstance( )
                            .getReference( DataBasePath.CHAT.getValue( ) ).child( mChatID );
                    String key = messageRef.push( ).getKey( );
                    if ( key != null ) {
                        messageRef.child( key ).setValue( chatMessage );
                    }

                    adapter.notifyDataSetChanged( );
                    editText.setText( "" );
                }
            }
        } );
    }

    /**
     * Initialize a query that will be listening to any new message create on FireBase
     * RealTimeDatabase.
     * <p>
     * When a new message is written in the database it will read the message and update the
     * adapter that contain the messages.
     */
    public void newMessageListener( ) {
        DatabaseReference messageRef = FirebaseDatabase.getInstance( )
                .getReference( DataBasePath.CHAT.getValue( ) ).child( mChatID );
        messageRef.addChildEventListener( new ChildEventListener( ) {
            @Override
            public void onChildAdded( @NonNull DataSnapshot dataSnapshot,
                                      @Nullable String s ) {
                Message message = dataSnapshot.getValue( Message.class );
                chatMessages.add( message );
                adapter.notifyDataSetChanged( );
            }

            @Override
            public void onChildChanged( @NonNull DataSnapshot dataSnapshot, @Nullable String s ) {

            }

            @Override
            public void onChildRemoved( @NonNull DataSnapshot dataSnapshot ) {

            }

            @Override
            public void onChildMoved( @NonNull DataSnapshot dataSnapshot, @Nullable String s ) {

            }

            @Override
            public void onCancelled( @NonNull DatabaseError databaseError ) {

            }
        } );
    }
}
