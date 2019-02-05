package com.example.notspotify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    boolean login = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Store XML items into variables to be manipulated
        final EditText inputUserName = (EditText) findViewById(R.id.text_input_username);
        final EditText inputPassword = (EditText) findViewById(R.id.text_input_password);
        final Button loginButton = (Button) findViewById(R.id.button_signIn);

        final UserList userList = loadJsonIntoUserList();
        final MusicList musicList = loadJsonIntoMusicList();
        //Log.d("MUSICLIST", musicList.toString());

        // Button listener for clicking on the log in button
        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Log.d("ONCLICKUSER", inputUserName.getText().toString());
                //Log.d("ONCLICKPASSWOR",inputPassword.getText().toString());
                //Log.d("USERLIST", userList.toString());
                login = checkCredentials(inputUserName.getText().toString(), inputPassword.getText().toString(), userList.getList(), v);
                signIn(v, login);
            }

        });
    }

    /**
     * Loads the users from the users.json file into userlist object using GSON
     * @return the populated user list
     */
    public UserList loadJsonIntoUserList()
    {
        try
        {
            String myJson = inputStreamToString(getAssets().open("users.json"));
            UserList userList  = new Gson().fromJson(myJson, UserList.class);
            return userList;
        }
        catch (IOException e) {
            return null;
        }
    }

    /**
     * Loads the music from the music.json file into musicList object using GSON
     * @return the populatd music list
     */
    public MusicList loadJsonIntoMusicList()
    {
        try
        {
            String myJson = inputStreamToString(getAssets().open("music.json"));
            MusicList musicList  = new Gson().fromJson(myJson, MusicList.class);
            return musicList;
        }
        catch (IOException e) {
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
     * @param v The view object
     */
    public boolean checkCredentials(String username, String password, List<User> userlist, View v)
    {
        for (int i = 0; i < userlist.size(); i++)
        {
            if(username.equals(userlist.get(i).getUserName()) && password.equals(userlist.get(i).getPassword()))
            {
                return true;
            }

        }
        return false;
    }

    /**
     * Sign into another activity using an intent
     * @param view The view object
     */
    public void signIn(View view, boolean correctInput) {
        if(correctInput) {
            Intent intent = new Intent(this, BottomNavActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "Username or password is incorrect", Toast.LENGTH_LONG).show();
        }
    }
}
