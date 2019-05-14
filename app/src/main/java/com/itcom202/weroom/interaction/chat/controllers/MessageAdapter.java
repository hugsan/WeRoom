package com.itcom202.weroom.interaction.chat.controllers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.itcom202.weroom.R;
import com.itcom202.weroom.account.models.Profile;
import com.itcom202.weroom.framework.ProfileSingleton;
import com.itcom202.weroom.interaction.chat.models.Message;

import java.util.List;

/**
 * Adapter used to read object from the class Message to be displayed in the ChatFragment.
 */
public class MessageAdapter extends ArrayAdapter<Message> {

    private Activity activity;
    private List<Message> messages;
    private Profile p = ProfileSingleton.getInstance( );

    /**
     * Constructor of MessageAdapter
     * @param context context of the application
     * @param resource resource value
     * @param messages list of messages
     */
    public MessageAdapter( Activity context, int resource, List<Message> messages ) {
        super( context, resource, messages );
        this.activity = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public View getView( int position, View convertView, @NonNull ViewGroup parent ) {
        ViewHolder holder;
        LayoutInflater inflater = ( LayoutInflater ) activity.getSystemService( Activity.LAYOUT_INFLATER_SERVICE );

        int layoutResource = 0; // determined by view type
        Message chatMessage = getItem( position );

        if ( chatMessage != null ) {
            if ( chatMessage.getSenderID( ).equals( p.getUserID( ) ) ) {
                layoutResource = R.layout.item_chat_right; //sending chat
            } else {
                layoutResource = R.layout.item_chat_left; //receiving chat
            }
        }

        if ( convertView != null ) {
            holder = ( ViewHolder ) convertView.getTag( );
        } else {
            convertView = inflater.inflate( layoutResource, parent, false );
            holder = new ViewHolder( convertView );
            convertView.setTag( holder );
        }

        //set message content
        if ( chatMessage != null ) {
            holder.msg.setText( chatMessage.getContent( ) );
        }
        return convertView;
    }

    @Override
    public int getViewTypeCount( ) {
        // return the total number of view types. this value should never change
        // at runtime
        return 2;
    }

    /**
     * Method that categorize the message from the adapter.
     *
     * @param position position of the message in the List<Message> message
     * @return 1 if the user is the author of the message, otherwise 0.
     */
    @Override
    public int getItemViewType( int position ) {
        // return a value between 0 and (getViewTypeCount - 1)
        if ( messages.get( position ).getSenderID( ).equals( ProfileSingleton.getInstance( ).getUserID( ) ) )
            return 1;
        return 0;
    }

    /**
     * Holder class of the message.
     * <p>
     * Now only displaying the message but should be able to display the name of the sender
     * and the date when the message was sent.
     */
    private class ViewHolder {
        private TextView msg;

        ViewHolder( View v ) {
            msg = v.findViewById( R.id.txt_msg );
        }
    }
}