package com.socialinfotech.socialchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.socialinfotech.socialchat.domain.User;
import com.socialinfotech.socialchat.domain.util.LibraryClass;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.email)
    AutoCompleteTextView email;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.email_sign_in_button)
    Button emailSignInButton;
    private Firebase firebase;
    private User user;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(LoginActivity.this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        firebase = LibraryClass.getFirebase();
        verifyUserLogged();
    }


    protected void initUser() {
        user = new User();
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
        user.generateCryptPassword();
    }

    public void callSignUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void sendLoginData(View view) {
        openProgressBar();
        initUser();
        verifyLogin();
    }

    private void openProgressBar() {
        progressBar = new ProgressDialog(LoginActivity.this);
        progressBar.setMessage("Processing...");
        progressBar.setCancelable(false);
        progressBar.show();
    }


    private void verifyUserLogged() {
        if (firebase.getAuth() != null) {
            callMainActivity();
        } else {
            initUser();

            if (!user.getTokenSP(this).isEmpty()) {
                firebase.authWithPassword(
                        "password",
                        user.getTokenSP(this),
                        new Firebase.AuthResultHandler() {
                            @Override
                            public void onAuthenticated(AuthData authData) {
                                user.saveTokenSP(LoginActivity.this, authData.getToken());
                                callMainActivity();
                            }

                            @Override
                            public void onAuthenticationError(FirebaseError firebaseError) {
                            }
                        }
                );
            }
        }
    }

    private void callMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    private void verifyLogin() {
        firebase.authWithPassword(
                user.getEmail(),
                user.getPassword(),
                new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        user.saveTokenSP(LoginActivity.this, authData.getToken());
                        closeProgressBar();
                        callMainActivity();
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        showSnackbar(firebaseError.getMessage());
                        closeProgressBar();
                    }
                }
        );
    }

    private void showSnackbar(String message) {
        Snackbar.make(emailSignInButton,
                message,
                Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void closeProgressBar() {
        if (progressBar.isShowing()) {
            progressBar.dismiss();
        }
    }
}
