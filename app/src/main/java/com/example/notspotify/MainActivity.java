package com.example.notspotify;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import android.content.SharedPreferences;


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

        session = new Session(getApplicationContext());

        final EditText inputUserName = (EditText) findViewById(R.id.text_input_username);
        final EditText inputPassword = (EditText) findViewById(R.id.text_input_password);
        final Button loginButton = (Button) findViewById(R.id.button_signIn);

        final UserList userList = loadJsonIntoUserList();

        final String path = getFilesDir().getAbsolutePath() + "/users.json";
        final File file = new File(path);

        //TODO: Used this for testing: ORIGINALLY NOT HERE
        final MusicList musicList = loadJsonIntoMusicList();
        //Log.d("MUSIC", musicList.toString());


        PlaylistHandler pls = loadJsonIntoPlaylist();
        pls.setupPlaylist(musicList);

        Log.d("PLAYLISTS", pls.toString());


        // Button listener for clicking on the log in button
        if (session.getLogin() == true) {
            Intent intent = new Intent(this, BottomNavActivity.class);
            startActivity(intent);
        }

        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Log.d("ONCLICKUSER", inputUserName.getText().toString());
                //Log.d("ONCLICKPASSWOR",inputPassword.getText().toString());
                //Log.d("USERLIST", userList.toString());

                if(file.exists())
                {
                    UserList newUserList =  updateUserList(file);

                    login = checkCredentials(inputUserName.getText().toString(), inputPassword.getText().toString(), newUserList.getList(), v);
                    //Log.d("ADDUSER", " IN MAIN" + newUserList.toString());
                }
                else
                {
                    login = checkCredentials(inputUserName.getText().toString(), inputPassword.getText().toString(), userList.getList(), v);
                }


                if (login == true) {
                    session.setUsername(inputUserName.getText().toString());
                    session.setPassword(inputPassword.getText().toString());
                    session.setLoginTrue("Login");
                }
                else{
                    session.setLoginFalse("Login");
                }

                signIn(v, login);
            }

        });
    }

    //TODO: Used this for testing: ORIGINALLY NOT HERE
    public MusicList loadJsonIntoMusicList()
    {
        try {
            String myJson = inputStreamToString(getAssets().open("music.json"));
            MusicList musicList  = new Gson().fromJson(myJson, MusicList.class);
            return musicList;
        }
        catch (IOException e) {
            return null;
        }
    }

    /**
     * Updates userList using local memory json file
     * @param file - file of local json
     * @return UsrList - new updated user list
     */
    public UserList updateUserList(File file)
    {
        UserList userTemp = null;
        try{


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
     * Loads the users from the playlists.json file into playlist object using GSON
     * @return the populated playlists
     */
    public PlaylistHandler loadJsonIntoPlaylist()
    {
        try
        {
            String myJson = inputStreamToString(getAssets().open("playlists.json"));
            PlaylistHandler playlist  = new Gson().fromJson(myJson, PlaylistHandler.class);
            return playlist;
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
                signIn(v, true);
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

    public void goToSignUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
