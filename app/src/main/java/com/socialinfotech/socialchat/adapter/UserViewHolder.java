package com.socialinfotech.socialchat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.socialinfotech.socialchat.R;


public class UserViewHolder extends RecyclerView.ViewHolder {

    public TextView text1;
    public TextView text2;
    public LinearLayout list_item;

    public UserViewHolder(View itemView) {
        super(itemView);
        list_item=(LinearLayout)itemView.findViewById(R.id.list_item);
        text1 = (TextView) itemView.findViewById(R.id.text1);
        text2 = (TextView) itemView.findViewById(R.id.text2);
    }
}
