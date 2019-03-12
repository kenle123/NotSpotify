package com.example.notspotify;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.List;

/**
 * Dialog box which appears when user clicks on a song to add to a playlist
 */
public class DialogActivity extends AppCompatActivity implements View.OnClickListener {

    // Local memory path
    MusicList musicList;

    // Get song information
    String songTitle = BrowseFragment.getSongTitle();
    String songID = BrowseFragment.getSongID();

    // Session information regarding the user's username
    static Session session = MainActivity.getSession();
    String username;

    // Linearlayout that will display the buttons
    LinearLayout ll;
    Button playlistButton;

    // The string name of the song to add to the playlist
    String stringOfPlaylistToAddSongTo;

    List<PlaylistSearchModel> playlist = LibraryFragment.getPlaylist();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        // Get username
        username = session.getUsername();

        // Load music list and playlist information into respective variables
        musicList = BrowseFragment.getMusicList();

        // Bind linear layout based on id declared in activity_dialog.xml
        ll = findViewById(R.id.linearLayout_Dialog);

        // Dynamically adds number of buttons to dialog box based on how many playlists the user has
        for(int i = 0; i < session.getUser().getListOfPlaylists().size(); i++) {
            playlistButton = new Button(DialogActivity.this);
            playlistButton.setId(i);
            playlistButton.setText(session.getUser().getListOfPlaylists().get(i).getPlaylistName());
            playlistButton.setTag(session.getUser().getListOfPlaylists().get(i).getPlaylistName());
            ll.addView(playlistButton);
            playlistButton.setOnClickListener(DialogActivity.this);
        }
    }

    // On click listener for when user selects a playlist to add a song to
    @Override
    public void onClick(View view) {
        String str = view.getTag().toString();
        for(int i = 0; i < session.getUser().getListOfPlaylists().size(); i++) {
            if (str.equals(session.getUser().getListOfPlaylists().get(i).getPlaylistName())) {
                stringOfPlaylistToAddSongTo = session.getUser().getListOfPlaylists().get(i).getPlaylistName();
                addSongToPlaylist(stringOfPlaylistToAddSongTo);
                Toast.makeText(DialogActivity.this, songTitle + " was added to the playlist " + stringOfPlaylistToAddSongTo, Toast.LENGTH_LONG).show();
            }
        }

        // Refresh playlist so when user adds the song, it appears right after
        LibraryFragment.populatePlaylist();
        playlist = LibraryFragment.getPlaylist();
        finish();
        startActivity(getIntent());
    }

    /**
     * Adds a song to a playlist
     */
    public void addSongToPlaylist(String pName) {
        Proxy proxy = new Proxy();
        String[] array = {  session.getUsername(),
                            pName,
                            songID                  };
        JsonObject ret = proxy.synchExecution("addSongToPlaylist", array);
        session.setUser(ret.toString());
    }

}
