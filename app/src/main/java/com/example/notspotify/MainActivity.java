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

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;


/**
 * Main Activity class handles the Login page
 */
public class MainActivity extends AppCompatActivity {

    Session session;
    boolean login = false;

    private static InetAddress host;
    private static final int PORT=1234;
    private static DatagramSocket datagramSocket;
    private static DatagramPacket inPacket,outPacket;
    private static byte[] buffer;

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

        // Load the users into userList
        final UserList userList = loadJsonIntoUserList();

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
                // If file exists, then write new user to local memory
//                if (file.exists()) {
//                    UserList newUserList = updateUserList(file);
//
//                    login = checkCredentials(inputUserName.getText().toString(), inputPassword.getText().toString(), newUserList.getList(), v);
//                    //Log.d("ADDUSER", " IN MAIN" + newUserList.toString());
//                } else {
//                    login = checkCredentials(inputUserName.getText().toString(), inputPassword.getText().toString(), userList.getList(), v);
//                }
//
//                // Set username and password in session so user won't have to log in again when
//                // he or she opens the app again
//                if (login) {
//                    session.setUsername(inputUserName.getText().toString());
//                    session.setPassword(inputPassword.getText().toString());
//                    session.setLoginTrue("Login");
//                } else {
//                    session.setLoginFalse("Login");
//                }
//                signIn(v, login);
                String userInput = inputUserName.getText().toString() + "," + inputPassword.getText().toString();
                try
                {
                    host = InetAddress.getByName("10.0.2.2");
                }
                catch(UnknownHostException uhEx)
                {
                    System.out.println("HOST ID not found.. ");
                    System.exit(1);
                }
                accessServer(userInput, v, inputUserName.getText().toString(), inputPassword.getText().toString());
            }
        });
    }

    private void accessServer(String input, View view, String username, String password)
    {
        try
        {
            datagramSocket=new DatagramSocket();
            String message=input;
            String response;
            //JSONObject response;

            outPacket=new DatagramPacket(message.getBytes(),message.length(),host,PORT);
            datagramSocket.send(outPacket);
            buffer=new byte[256];
            inPacket=new DatagramPacket(buffer,buffer.length);
            datagramSocket.receive(inPacket);
            response=new String(inPacket.getData(),0,inPacket.getLength());
            //response=new JSONObject(inPacket.getData(),0,inPacket.getLength());
            Log.d("GGEZ", response);
            Log.d("GGEZ", "" + response.length());

            if(response.length() != 2) {
                login = true;
                session.setUsername(username);
                session.setPassword(password);
                session.setLoginTrue("Login");
            }
            else {
                session.setLoginFalse("Login");
            }
            signIn(view, login);

        }
        catch(IOException ioEx)
        {
            ioEx.printStackTrace();
        }

        finally
        {
            System.out.println("\n closing connection.... ");
            datagramSocket.close();
        }
    }

    /**
     * Updates userList using local memory json file
     * @param file - file of local json
     * @return UsrList - new updated user list
     */
    public UserList updateUserList(File file) {
        UserList userTemp = null;
        try {
            if (file.exists()) {
                //Log.d("ADDUSER", "file exists IN MAIN");
                InputStream inputStream = new FileInputStream(file);
                String myJson = inputStreamToString(inputStream);
                userTemp = new Gson().fromJson(myJson, UserList.class);
                inputStream.close();

                //Log.d("ADDUSER", userList.toString());

                return userTemp;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return userTemp;
    }

    /**
     * Loads the users from the users.json file into userlist object using GSON
     * @return the populated user list
     */
    public UserList loadJsonIntoUserList() {
        try {
            String myJson = inputStreamToString(getAssets().open("users.json"));
            UserList userList = new Gson().fromJson(myJson, UserList.class);
            return userList;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Reads a file using inputstream
     * @param inputStream a file to read from
     * @return a string of the read in file
     */
    public String inputStreamToString(InputStream inputStream) {
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);
            String json = new String(bytes);
            return json;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Checks if username and password that user inputted matches a user profile provided from the json file
     * @param username The username the user inputted
     * @param password The password the user inputted
     * @param userlist The user list which contains all the users
     * @param v        The view object
     */
    public boolean checkCredentials(String username, String password, List<User> userlist, View v) {
        for (int i = 0; i < userlist.size(); i++) {
            if (username.equals(userlist.get(i).getUserName()) && password.equals(userlist.get(i).getPassword())) {
                signIn(v, true);
                return true;
            }
        }
        return false;
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