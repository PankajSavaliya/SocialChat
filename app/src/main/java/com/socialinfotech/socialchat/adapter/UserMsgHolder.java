package com.socialinfotech.socialchat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.socialinfotech.socialchat.R;


public class UserMsgHolder extends RecyclerView.ViewHolder {


    public TextView chatText;
    public LinearLayout singleMessageContainer;

    public UserMsgHolder(View itemView) {
        super(itemView);
        singleMessageContainer = (LinearLayout) itemView.findViewById(R.id.singleMessageContainer);
        chatText = (TextView) itemView.findViewById(R.id.singleMessage);

    }
}
