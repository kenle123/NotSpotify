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

public class DialogActivity extends AppCompatActivity implements View.OnClickListener {

    MusicList musicList;
    // Load the playlist from playlist.json into playlistHandler
    PlaylistHandler playlistHandler;
    String songTitle = BrowseFragment.getSongTitle();
    String songID = BrowseFragment.getSongID();

    Session session;
    String username;

    // Linearlayout for dialog that will display the buttons
    LinearLayout ll;
    Button playlistButton;

    // Get playlists from library fragment
    List<PlaylistSearchModel> playlistsDialog = LibraryFragment.getPlaylist();

    String stringOfPlaylistToAddSongTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        musicList = loadJsonIntoMusicList();
        playlistHandler = loadJsonIntoPlaylist();
        playlistHandler.setupPlaylist(musicList);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        // Get username
        session = new Session(this);
        username = session.getUsername();

        // Bind linear layout based on id declared in activity_dialog.xml
        ll = findViewById(R.id.linearLayout_Dialog);

        // Dynamically adds number of buttons to dialog box based on how many playlists the user has
        for(int i = 0; i < playlistsDialog.size(); i++) {
            playlistButton = new Button(DialogActivity.this);
            playlistButton.setId(i);
            playlistButton.setText(playlistsDialog.get(i).getPlaylistName());
            playlistButton.setTag(playlistsDialog.get(i).getPlaylistName());
            ll.addView(playlistButton);
            playlistButton.setOnClickListener(DialogActivity.this);
        }
    }

    // The variables I got are: songTitle, songID which come from song u long click in browse fragment
    // stringOfPlaylistToAddSongTo which is the button user click when dialog box open and this is a string of a playlist name
    // On click listener to detect which playlist the user clicked on
    @Override
    public void onClick(View view) {
        String str = view.getTag().toString();
        for(int i = 0; i < playlistsDialog.size(); i++) {
            if (str.equals(playlistsDialog.get(i).getPlaylistName())) {
                stringOfPlaylistToAddSongTo = playlistsDialog.get(i).getPlaylistName();
                final String path = view.getContext().getFilesDir().getAbsolutePath() + "/playlists.json";
                final File file = new File(path);
                if(file.exists())
                {
                    PlaylistHandler newPlaylistHandler =  updatePlaylistHandler(file);
                    addSongToPlaylist(newPlaylistHandler);
                }
                else
                {
                    addSongToPlaylist(playlistHandler);

                }

                // Adding to playlist should happen here i think...
                // u might have to copy pasta ur writing functions from library fragment here
            }
        }
    }

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


}
