package com.example.notspotify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.JsonObject;

/**
 * Sign up page for creating an account
 */
public class SignUpActivity extends AppCompatActivity {

    ImageButton backButtonSignUp;
    EditText inputUserName;
    EditText inputPassword;
    Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Bind variables
        backButtonSignUp = findViewById(R.id.button_back_signup);
        inputUserName = findViewById(R.id.text_input_uSignUp);
        inputPassword = findViewById(R.id.text_input_pSignUp);
        signUpButton = findViewById(R.id.button_signUp);

        // On click listener for sign up button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObject ret;
                Proxy proxy = new Proxy();
                String[] array = {
                        inputUserName.getText().toString(),
                        inputPassword.getText().toString()};
                ret = proxy.synchExecution("SignUp", array);
                if(!ret.toString().equals("{}")) {
                    signUp(v);

                }
                else {
                  Toast.makeText(SignUpActivity.this, "Username already exist, please try another name", Toast.LENGTH_LONG).show();
                }
            }
        });

        // On click listener for back button
        backButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * Create the account for the user
     * @param view The view
     */
    public void signUp(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Account created, please log in!", Toast.LENGTH_LONG).show();
    }
}
