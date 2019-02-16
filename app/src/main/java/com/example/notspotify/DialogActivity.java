package com.example.notspotify;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Dialog box which appears when user clicks on a song to add to a playlist
 */
public class DialogActivity extends AppCompatActivity implements View.OnClickListener {

    // Local memory path
    String path;
    File file;
    MusicList musicList;

    // Get song information
    String songTitle = BrowseFragment.getSongTitle();
    String songID = BrowseFragment.getSongID();

    // Load the playlist from playlist.json into playlistHandler
    PlaylistHandler playlistHandler;
    UserPlaylist user;

    // Session information regarding the user's username
    Session session;
    String username;

    // Linearlayout that will display the buttons
    LinearLayout ll;
    Button playlistButton;

    // The string name of the song to add to the playlist
    String stringOfPlaylistToAddSongTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        // Get local memory information
        path = getFilesDir().getAbsolutePath() + "/playlists.json";
        file = new File(path);

        // Get username
        session = new Session(this);
        username = session.getUsername();

        // Load music list and playlist information into respective variables
        musicList = loadJsonIntoMusicList();
        playlistHandler = loadJsonIntoPlaylist();
        playlistHandler.setupPlaylist(musicList);

        // Check if file exists, otherwise, check local memory
        if(file.exists()) {
            playlistHandler = updatePlaylistHandler(file);
        }

        if(playlistHandler.getUserPlaylist(username) == null) {
            addUserToPlaylist();
            playlistHandler = updatePlaylistHandler(file);
        }

        user = playlistHandler.getUserPlaylist(username);

        // Bind linear layout based on id declared in activity_dialog.xml
        ll = findViewById(R.id.linearLayout_Dialog);

        // Dynamically adds number of buttons to dialog box based on how many playlists the user has
        for(int i = 0; i < user.getPlaylist().size(); i++) {
            playlistButton = new Button(DialogActivity.this);
            playlistButton.setId(i);
            playlistButton.setText(user.getPlaylist().get(i).getPlaylistName());
            playlistButton.setTag(user.getPlaylist().get(i).getPlaylistName());
            ll.addView(playlistButton);
            playlistButton.setOnClickListener(DialogActivity.this);
        }
    }

    // On click listener for when user selects a playlist to add a song to
    @Override
    public void onClick(View view) {
        String str = view.getTag().toString();
        for(int i = 0; i < user.getPlaylist().size(); i++) {
            if (str.equals(user.getPlaylist().get(i).getPlaylistName())) {
                stringOfPlaylistToAddSongTo = user.getPlaylist().get(i).getPlaylistName();
                if(file.exists()) {
                    PlaylistHandler newPlaylistHandler =  updatePlaylistHandler(file);
                    addSongToPlaylist(newPlaylistHandler);
                    Toast.makeText(this, songTitle + " was added to the playlist " + stringOfPlaylistToAddSongTo, Toast.LENGTH_LONG).show();
                }
                else {
                    addSongToPlaylist(playlistHandler);
                }
            }
        }
    }

    /**
     * Loads the music from the music.json file into musicList object using GSON
     * @return the populatd music list
     */
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
     * Loads the users from the playlists.json file into playlist object using GSON
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
     * Reads a file using inputstream
     *
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
     * Updates userList using local memory json file
     * @param file - file of local json
     * @return pTemp - new updated PlaylistHandler
     */
    public PlaylistHandler updatePlaylistHandler(File file)
    {
        PlaylistHandler pTemp = null;
        try{


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
     * Adds a song to a playlist
     * @param p The playlist object
     */
    public void addSongToPlaylist(PlaylistHandler p) {
        p.getUserPlaylist(username).getOnePlaylist(stringOfPlaylistToAddSongTo).addSong(musicList.getSong(songID));
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
     * Adds user to a playlist
     */
    public void addUserToPlaylist() {
        playlistHandler.addUserPlaylist(username);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String strJson = gson.toJson(playlistHandler);
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
}
