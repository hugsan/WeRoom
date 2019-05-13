package com.itcom202.weroom.interaction.chat.controllers;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.itcom202.weroom.framework.ProfileSingleton;
import com.itcom202.weroom.R;
import com.itcom202.weroom.interaction.chat.models.Message;
import com.itcom202.weroom.account.models.Profile;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {

    private Activity activity;
    private List<Message> messages;
    private Profile p = ProfileSingleton.getInstance();

    public MessageAdapter(Activity context, int resource, List<Message> objects) {
        super(context, resource, objects);
        this.activity = context;
        this.messages = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        int layoutResource = 0; // determined by view type
        Message chatMessage = getItem(position);

        if (chatMessage.getSenderID().equals(p.getUserID())) {
            layoutResource = R.layout.item_chat_right; //sending chat
        } else {
            layoutResource = R.layout.item_chat_left; //receiving chat
        }

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        //set message content
        holder.msg.setText(chatMessage.getContent());

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        // return the total number of view types. this value should never change
        // at runtime
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        // return a value between 0 and (getViewTypeCount - 1)
        if (messages.get(position).getSenderID().equals(ProfileSingleton.getInstance().getUserID()))
            return 1;
        return  0;
    }

    private class ViewHolder {
        private TextView msg;

        public ViewHolder(View v) {
            msg = v.findViewById(R.id.txt_msg);
        }
    }
}