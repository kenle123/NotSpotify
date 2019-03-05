package com.example.notspotify;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;


/**
 * Main Activity class handles the Login page
 */
public class MainActivity extends AppCompatActivity {

    Session session;
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

        // Get path for local memory
        final String path = getFilesDir().getAbsolutePath() + "/users.json";
        final File file = new File(path);

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
                if(!ret.toString().equals("{}")) {
                    login = true;
                    session.setUsername(inputUserName.getText().toString());
                    session.setLoginTrue("Login");
                }
                else {
                    session.setLoginFalse("Login");
                }
                signIn(v, login);
            }
        });
    }

    /**
     * If the user has the correct credentials, then go to main app with bottom navigation
     * @param view The view object
     */
    public void signIn(View view, boolean correctInput) {
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
}