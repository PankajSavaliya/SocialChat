package com.socialinfotech.socialchat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.socialinfotech.socialchat.R;


public class UserConversationHolder extends RecyclerView.ViewHolder {

    public TextView text1;
    public TextView text2,read_count;
    public View list_item;

    public UserConversationHolder(View itemView) {
        super(itemView);
        list_item=(View)itemView.findViewById(R.id.list_item);
        text1 = (TextView) itemView.findViewById(R.id.text1);
        text2 = (TextView) itemView.findViewById(R.id.text2);
        read_count=(TextView)itemView.findViewById(R.id.read_count);

    }
}
