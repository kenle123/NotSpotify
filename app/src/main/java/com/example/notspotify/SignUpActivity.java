package com.example.notspotify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Sign up page for creating an account
 */
public class SignUpActivity extends AppCompatActivity {

    boolean userExist = false;
    ImageButton backButtonSignUp;
    EditText inputUserName;
    EditText inputPassword;
    Button signUpButton;

    // Variables to be to have information loaded into
    PlaylistHandler playlistHandler;
    MusicList musicList;
    UserList userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Load the songs and playlists from their respective files
        musicList = loadJsonIntoMusicList();
        playlistHandler = loadJsonIntoPlaylist();
        userList = loadJsonIntoUserList();
        playlistHandler.setupPlaylist(musicList);

        // Bind variables
        backButtonSignUp = findViewById(R.id.button_back_signup);
        inputUserName = findViewById(R.id.text_input_uSignUp);
        inputPassword = findViewById(R.id.text_input_pSignUp);
        signUpButton = findViewById(R.id.button_signUp);

        // Get path to local memory
        final String path = getFilesDir().getAbsolutePath() + "/users.json";
        final File file = new File(path);

        // On click listener for sign up button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Checks if local memory exists
                if(file.exists()) {
                    UserList newUserList =  updateUserList(file);

                    userExist = checkUserName(inputUserName.getText().toString(), newUserList.getList(), v);
                    //Log.d("ADDUSER", "IN SIGN IN" +newUserList.toString());
                }
                else {
                    userExist = checkUserName(inputUserName.getText().toString(), userList.getList(), v);
                }

                // Since user does not exist yet, add to local memory
                if(!userExist){
                    if(file.exists()) {
                        UserList newUserList =  updateUserList(file);
                        addUser(inputUserName.getText().toString(), inputPassword.getText().toString(), "users.json", newUserList);
                    }
                    else {
                        addUser(inputUserName.getText().toString(), inputPassword.getText().toString(), "users.json", userList);
                    }
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
     * Updates userList using local memory json file
     * @param file - file of local json
     * @return UsrList - new updated user list
     */
    public UserList updateUserList(File file) {
        UserList userTemp = null;
        try {
            if (file.exists()) {
                //Log.d("ADDUSER", "file exists IN SIGN IN");
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
     * Updates userList using local memory json file
     * @param file - file of local json
     * @return pTemp - new updated PlaylistHandler
     */
    public PlaylistHandler updatePlaylistHandler(File file) {
        PlaylistHandler pTemp = null;
        try {
            if (file.exists()) {
                //Log.d("ADDUSER", "file exists IN SIGN IN");
                InputStream inputStream = new FileInputStream(file);
                String myJson = inputStreamToString(inputStream);
                pTemp = new Gson().fromJson(myJson, PlaylistHandler.class);
                inputStream.close();

                //Log.d("ADDUSER", userList.toString());

                return pTemp;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return pTemp;
    }

    /**
     * Adds user to user list
     * @param name - string, user name
     * @param password - string, user password
     * @param json - string, json filename
     * @param userlist UserList - user list
     */
    public void addUser(String name, String password, String json , UserList userlist) {
        User newUser = new User();
        newUser.setUserName(name);
        newUser.setPassword(password);
        newUser.setJson(json);

        userlist.addToList(newUser);

        //TODO: Add playlist add here
        final String path2 = getFilesDir().getAbsolutePath() + "/playlists.json";
        final File file2 = new File(path2);
        if(file2.exists()) {
            PlaylistHandler newPlaylistHandler =  updatePlaylistHandler(file2);
            addUserToPlaylist(newPlaylistHandler, name);
        }
        else {
            addUserToPlaylist(playlistHandler, name);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String strJson = gson.toJson(userlist);

        try {
            String filePath =   getFilesDir().getAbsolutePath() + "/users.json";
            File file = new File(filePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(strJson.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
            //Log.d("ADDUSER", "OUTPUTTED");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add playlist to user's account
     * @param p The user's playlists
     * @param uName The username
     */
    public void addUserToPlaylist(PlaylistHandler p, String uName) {
        p.addUserPlaylist(uName);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String strJson = gson.toJson(p);
        String fileContents = strJson;
        //Log.d("ADDSONG", p.getUserPlaylist(username).toString());
        try {
            String filePath = getFilesDir().getAbsolutePath() + "/playlists.json";
            File file = new File(filePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(strJson.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
            //Log.d("ADDUSER", "OUTPUTTED");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the users from the users.json file into userlist object using GSON
     * @return the populated user list
     */
    public UserList loadJsonIntoUserList() {
        try {
            String myJson = inputStreamToString(getAssets().open("users.json"));
            UserList userList  = new Gson().fromJson(myJson, UserList.class);
            return userList;
        }
        catch (IOException e) {
            return null;
        }
    }

    public MusicList loadJsonIntoMusicList() {
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
     * Loads the users from the playlists.json file into playlist object using GSON
     *
     * @return the populated playlists
     */
    public PlaylistHandler loadJsonIntoPlaylist() {
        try {
            String myJson = inputStreamToString(getAssets().open("playlists.json"));
            PlaylistHandler playlist = new Gson().fromJson(myJson, PlaylistHandler.class);
            return playlist;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Checks if username and password that user inputted matches a user profile provided from the json file
     * @param username The username the user inputted
     * @param userlist The user list which contains all the users
     * @param v The view object
     */
    public boolean checkUserName(String username, List<User> userlist, View v) {
        for (int i = 0; i < userlist.size(); i++)
        {

            if(username.equals(userlist.get(i).getUserName()))
            {
                return true;
            }
        }
        return false;
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
     * Create the account for the user
     * @param view The view
     */
    public void signUp(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Account created, please log in!", Toast.LENGTH_LONG).show();
    }
}
