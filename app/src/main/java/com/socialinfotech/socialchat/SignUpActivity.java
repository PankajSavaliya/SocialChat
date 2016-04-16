package com.socialinfotech.socialchat;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.socialinfotech.socialchat.domain.User;
import com.socialinfotech.socialchat.domain.util.LibraryClass;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.name)
    AutoCompleteTextView name;
    @Bind(R.id.email)
    AutoCompleteTextView email;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.email_sign_up_button)
    Button emailSignUpButton;
    private Firebase firebase;
    private User user;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firebase = LibraryClass.getFirebase();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    protected void initUser() {
        user = new User();
        user.setName(name.getText().toString());
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
        user.generateCryptPassword();
    }

    public void sendSignUpData(View view) {
        openProgressBar();
        initUser();
        saveUser();
    }

    private void saveUser() {
        firebase.createUser(
                user.getEmail(),
                user.getPassword(),
                new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> stringObjectMap) {
                        user.setId(stringObjectMap.get("uid").toString());
                        user.saveDB();
                        firebase.unauth();
                        Toast.makeText(SignUpActivity.this,
                                getString(R.string.reg_suc),
                                Toast.LENGTH_LONG)
                                .show();
                        closeProgressBar();
                        finish();
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        showSnackbar(firebaseError.getMessage());
                        closeProgressBar();
                    }
                }
        );
    }

    private void showSnackbar(String message) {
        Snackbar.make(emailSignUpButton,
                message,
                Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void closeProgressBar() {
        if (progressBar.isShowing()) {
            progressBar.dismiss();
        }
    }

    private void openProgressBar() {
        progressBar = new ProgressDialog(SignUpActivity.this);
        progressBar.setMessage("Processing...");
        progressBar.setCancelable(false);
        progressBar.show();
    }
}
