package com.socialinfotech.socialchat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.google.android.gms.appindexing.Thing;
import com.socialinfotech.socialchat.adapter.UserConversationHolder;
import com.socialinfotech.socialchat.domain.chat.conversation;
import com.socialinfotech.socialchat.domain.util.LibraryClass;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;


public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.rv_users)
    RecyclerView rvUsers;
    private Firebase firebase;
    private GoogleApiClient mClient;
    private Uri mUrl;
    private String mTitle;
    private String mDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        firebase = LibraryClass.getFirebase().child("users");
        firebase = firebase.child(firebase.getAuth().getUid()).child(getString(R.string.conversation));
        Log.e("user token", firebase.getAuth().getUid());

        AppIndexing();


    }


    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init() {

        rvUsers.setHasFixedSize(true);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));

        UserRecyclerAdapter adapter = new UserRecyclerAdapter(
                conversation.class,
                R.layout.raw_coversation,
                UserConversationHolder.class,
                firebase);
        rvUsers.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //   firebase.removeEventListener(customValueEventListener);
    }


    // MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_update) {
            startActivity(new Intent(this, AddActivity.class));
        } else if (id == R.id.action_logout) {
            firebase.unauth();

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private class UserRecyclerAdapter extends FirebaseRecyclerAdapter<conversation, UserConversationHolder> {

        public UserRecyclerAdapter(
                Class<conversation> modelClass,
                int modelLayout,
                Class<UserConversationHolder> viewHolderClass,
                Query ref) {

            super(modelClass, modelLayout, viewHolderClass, ref);
        }

        @Override
        protected void populateViewHolder(
                final UserConversationHolder userViewHolder,
                final conversation user, final int i) {

            userViewHolder.text1.setText(user.getUser_name());
            if (user.getRead_number() == 0) {
                userViewHolder.read_count.setVisibility(View.GONE);
                if (user.getStatus().equals(getString(R.string.typing))) {
                    userViewHolder.text2.setTextColor(getResources().getColor(R.color.colorAccent));
                    userViewHolder.text2.setText(getString(R.string.typing));
                } else {
                    userViewHolder.text2.setTextColor(getResources().getColor(R.color.secondeyText));
                    userViewHolder.text2.setText(user.getLast_msg());
                }

            } else {
                if (user.getStatus().equals(getString(R.string.typing))) {
                    userViewHolder.text2.setText(getString(R.string.typing));

                } else {
                    userViewHolder.text2.setText(user.getLast_msg());
                }
                userViewHolder.read_count.setVisibility(View.VISIBLE);
                userViewHolder.read_count.setText(String.valueOf(user.getRead_number()));
                userViewHolder.text2.setTextColor(getResources().getColor(R.color.colorAccent));
            }

            userViewHolder.list_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.e("click", getRef(i).getKey() + " Table name:" + user.getTable_name());
                    Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                    intent.putExtra("user_id", getRef(i).getKey());
                    intent.putExtra("title", user.getUser_name());
                    intent.putExtra("table_name", user.getTable_name());
                    startActivity(intent);
//                    if (!getRef(i).getKey().equals(firebase.getAuth().getUid())) {
//                        Firebase transaction_firbase = LibraryClass.getFirebase().child("users").child(firebase.getAuth().getUid()).child(getString(R.string.conversation)).child(getRef(i).getKey());
////                    Map<String, String> timestamp = ServerValue.TIMESTAMP;
//                        conversation transactionModel = new conversation();
//                        transactionModel.setUser_id(getRef(i).getKey());
//                        transactionModel.setUser_name(user.getName());
//                        transactionModel.setLast_msg("namste");
//                        transactionModel.setRead_number(10);
//                        transactionModel.setStatus("online");
//                        transaction_firbase.setValue(transactionModel);
//                    }

                }
            });
        }
    }


    public Action getAction() {
        Thing object = new Thing.Builder()
                .setName(mTitle)
                .setDescription(mDescription)
                .setUrl(mUrl)
                .build();

        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    private void AppIndexing() {
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        mUrl = Uri.parse("http://www.socialinfotech.com/");
        mTitle = "Social Infotech";
        mDescription = getString(R.string.desc);
    }

    @Override
    public void onStart() {
        super.onStart();
        mClient.connect();
        AppIndex.AppIndexApi.start(mClient, getAction());
    }

    @Override
    public void onStop() {
        AppIndex.AppIndexApi.end(mClient, getAction());
        mClient.disconnect();
        super.onStop();
    }
}
