package com.socialinfotech.socialchat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.socialinfotech.socialchat.adapter.UserMsgHolder;
import com.socialinfotech.socialchat.domain.chat.conversation;
import com.socialinfotech.socialchat.domain.chat.message;
import com.socialinfotech.socialchat.domain.util.LibraryClass;

import java.util.HashMap;
import java.util.Map;



public class ChatActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private UserRecyclerAdapter adapter;
    private String user_id;
    private String table_name;
    private EditText editText;
    private RelativeLayout send_button;
    Firebase receiver_firbase, sender_firebase;
    static boolean active = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);


        Starting();

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().length() > 0) {
                    Firebase firebase = LibraryClass.getFirebase().child("chat").child(table_name).push();
                    message message = new message();
                    message.setSender(firebase.getAuth().getUid());
                    message.setReceiver(user_id);
                    message.setMessage(editText.getText().toString());
                    firebase.setValue(message, new Firebase.CompletionListener() {
                        @Override
                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                            if (firebaseError != null) {
                                System.out.println("Data could not be saved. " + firebaseError.getMessage());
                                Toast.makeText(ChatActivity.this, "Message does not send,try again.", Toast.LENGTH_SHORT).show();
                            } else {
                                UpdateReceiver(editText.getText().toString());
                                UpdateSender(editText.getText().toString());
                                editText.setText("");
                                System.out.println("Data saved successfully.");
                            }
                        }
                    });

                }
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.e("befror", "in");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("onTextChanged", "in");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("afterTextChanged", "in");
                Map<String, Object> alanNickname = new HashMap<String, Object>();
                alanNickname.put("status", getString(R.string.typing));
                receiver_firbase.updateChildren(alanNickname);
            }
        });
    }

    private void UpdateSender(String s) {


        Map<String, Object> alanNickname = new HashMap<String, Object>();
        alanNickname.put("read_number", 0);
        alanNickname.put("last_msg", s);

        sender_firebase.updateChildren(alanNickname);


    }

    private void UpdateReceiver(final String s) {
        receiver_firbase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!(dataSnapshot == null)) {
                    Log.e("send point", dataSnapshot.toString());
                    conversation u = dataSnapshot.getValue(conversation.class);
                    Map<String, Object> alanNickname = new HashMap<String, Object>();
                    alanNickname.put("read_number", u.getRead_number() + 1);
                    alanNickname.put("last_msg", s);
                    alanNickname.put("status", getString(R.string.online));
                    receiver_firbase.updateChildren(alanNickname);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void Starting() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        send_button = (RelativeLayout) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.editText);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        user_id = getIntent().getStringExtra("user_id");
        table_name = getIntent().getStringExtra("table_name");
        setTitle(getIntent().getStringExtra("title"));
        Log.e("user id", user_id);
        Log.e("table_name", table_name);
        receiver_firbase = LibraryClass.getFirebase().child("users");
        receiver_firbase = receiver_firbase.child(user_id).child(getString(R.string.conversation)).child(receiver_firbase.getAuth().getUid());

        sender_firebase = LibraryClass.getFirebase().child("users");
        sender_firebase = sender_firebase.child(sender_firebase.getAuth().getUid()).child(getString(R.string.conversation)).child(user_id);
        //read status
        Map<String, Object> alanNickname = new HashMap<String, Object>();
        alanNickname.put("read_number", 0);
        alanNickname.put("status", getString(R.string.online));

        sender_firebase.updateChildren(alanNickname);
        sender_firebase.addValueEventListener(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Log.e("data statnig.-----", dataSnapshot.toString());
            if (!(dataSnapshot.getValue() == null) && active) {
                final conversation conversation = dataSnapshot.getValue(conversation.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (conversation.getStatus().equals(getString(R.string.typing))) {
                            getSupportActionBar().setSubtitle(conversation.getStatus());

                        } else {
                            getSupportActionBar().setSubtitle("");
                        }
                        getSupportActionBar().invalidateOptionsMenu();
                    }
                });

            }
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        final RecyclerView rvUsers = (RecyclerView) findViewById(R.id.rv_users);
        rvUsers.setHasFixedSize(true);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        Firebase firebase = LibraryClass.getFirebase().child("chat").child(table_name);
        adapter = new UserRecyclerAdapter(
                message.class,
                R.layout.activity_chat_singlemessage,
                UserMsgHolder.class,
                firebase);
        rvUsers.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                rvUsers.scrollToPosition(positionStart);
                super.onItemRangeInserted(positionStart, itemCount);
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        //---------------
        //read status
        Map<String, Object> alanNickname = new HashMap<String, Object>();
        alanNickname.put("read_number", 0);
        alanNickname.put("status", getString(R.string.online));

        sender_firebase.updateChildren(alanNickname);
        //---------------
        Map<String, Object> alanNickname1 = new HashMap<String, Object>();
        alanNickname1.put("status", getString(R.string.online));
        receiver_firbase.updateChildren(alanNickname1);
        sender_firebase.removeEventListener(valueEventListener);
        super.onStop();
        active = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // adapter.cleanup();
    }

    private class UserRecyclerAdapter extends FirebaseRecyclerAdapter<message, UserMsgHolder> {

        public UserRecyclerAdapter(
                Class<message> modelClass,
                int modelLayout,
                Class<UserMsgHolder> viewHolderClass,
                Query ref) {

            super(modelClass, modelLayout, viewHolderClass, ref);
        }

        @Override
        protected void populateViewHolder(
                final UserMsgHolder userViewHolder,
                final message user, final int i) {
            if (getRef(i).getAuth().getUid().equals(user.getSender())) {
                userViewHolder.chatText.setBackgroundResource(R.drawable.outgoing);
                userViewHolder.singleMessageContainer.setGravity(Gravity.RIGHT);
            } else {
                userViewHolder.chatText.setBackgroundResource(R.drawable.incoming);
                userViewHolder.singleMessageContainer.setGravity(Gravity.LEFT);
            }
            userViewHolder.chatText.setText(user.getMessage());


        }
    }


}
