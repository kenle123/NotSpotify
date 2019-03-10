package com.example.notspotify;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;



/**
 * Main Activity class handles the Login page
 */
public class MainActivity extends AppCompatActivity {

    static Session session;
    boolean login = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Bind session object
        session = new Session(getApplicationContext());

        // Bind edittext widgets and login button
        final EditText inputUserName = (EditText) findViewById(R.id.text_input_username);
        final EditText inputPassword = (EditText) findViewById(R.id.text_input_password);
        final Button loginButton = (Button) findViewById(R.id.button_signIn);

        // Button listener for clicking on the log in button
        if (session.getLogin()) {
            Intent intent = new Intent(this, BottomNavActivity.class);
            startActivity(intent);
        }

        // On click listener for login button
        // The user will click the login button and it should send the message "Sent the message from Android" to the server running in terminal
        // I only added the below code and the function accessServer() below
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObject ret;
                Proxy proxy = new Proxy();
                String[] array = {  inputUserName.getText().toString(),
                                    inputPassword.getText().toString()};
                ret = proxy.synchExecution("Login", array);
                if(ret.size() > 0) {
                    login = true;
                    session.setUsername(inputUserName.getText().toString());
                    session.setPassword(inputPassword.getText().toString());
                    session.setLoginTrue("Login");
                }
                else {
                    session.setLoginFalse("Login");
                }
                signIn(login);
            }
        });
    }

    /**
     * If the user has the correct credentials, then go to main app with bottom navigation
     *
     */
    public void signIn(boolean correctInput) {
        if (correctInput) {
            Intent intent = new Intent(this, BottomNavActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Username or password is incorrect", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Go to create account page
     * @param view The view object
     */
    public void goToSignUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public static Session getSession() {
        return session;
    }
}