package com.example.chav.poker.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.chav.poker.R;
import com.example.chav.poker.managers.UsersManager;

import model.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText mUsername;
    private EditText mEmail;
    private EditText mPass;
    private EditText mConfirmPass;
    private Button mSubmit;
    private Button mCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUsername = (EditText) findViewById(R.id.register_username);
        mEmail = (EditText) findViewById(R.id.register_email);
        mPass = (EditText) findViewById(R.id.register_password);
        mConfirmPass = (EditText) findViewById(R.id.register_confirm_password);
        mSubmit = (Button) findViewById(R.id.register_submit_button);
        mCancel = (Button) findViewById(R.id.register_cancel_button);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsername.getText().toString();
                String email = mEmail.getText().toString();
                String pass = mPass.getText().toString();
                String confirmPass = mConfirmPass.getText().toString();

                if (isLegalUsername(username)
                        && isLegalEMail(email)
                        && isLegalPassword(pass, confirmPass)) {

                    User user = new User(username, email, pass);
                    UsersManager.getInstance(v.getContext()).registerUser(user);
                    Intent i = new Intent(v.getContext(), QuizSelectionActivity.class);
                    startActivity(i);
                }

            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), StartScreenActivity.class);
                startActivity(i);
            }
        });
    }

    private boolean isLegalPassword(String pass, String confirmPass) {

        if (!pass.matches(".*[A-Z].*")) {
            mPass.setError("password must contain a small letter, a capital letter, a number and a special symbol");
            return false;
        }

        if (!pass.matches(".*[a-z].*")) {
            mPass.setError("password must contain a small letter, a capital letter, a number and a special symbol");
            return false;
        }

        if (!pass.matches(".*\\d.*")) {
            mPass.setError("password must contain a small letter, a capital letter, a number and a special symbol");
            return false;
        }

        if (!pass.matches(".*[`~!#$%^&*()-_=+\\|[{]};:'\",<.>/?].*")) {
            mPass.setError("password must contain a small letter, a capital letter, a number and a special symbol");
            return false;
        }

        if (!pass.equals(confirmPass)) {
            mPass.setError("both passwords should match");
            return false;
        }

        return true;

    }

    private boolean isLegalUsername (String username) {
        if (username.trim().length() < 3) {
            mUsername.setError("username must be at least 3 characters in length");
            return false;
        }

        boolean userExists =(UsersManager.getInstance(this).isSignedUp(username));
        if (userExists) {
            mUsername.setError("username already exists");
            return false;
        }

        return true;

    }

    private boolean isLegalEMail(String eMail){

        if (!eMail.contains("@") || !eMail.contains(".") || eMail.trim().length() < 8) {
            mEmail.setError("incorrect email entry");
            return false;
        }

        boolean userExists = (UsersManager.getInstance(this).isSignedUp(eMail));

        if (userExists) {
            mEmail.setError("user with that e-mail already exists");
            return false;
        }

        return true;
    }
}