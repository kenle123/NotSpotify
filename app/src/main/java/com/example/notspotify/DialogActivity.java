package com.example.notspotify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class DialogActivity extends AppCompatActivity implements View.OnClickListener {

    String songTitle = BrowseFragment.getSongTitle();
    String songID = BrowseFragment.getSongID();

    Session session;
    String username;

    // Linearlayout for dialog that will display the buttons
    LinearLayout ll;
    Button playlistButton;

    // Get playlists from library fragment
    List<PlaylistSearchModel> playlistsDialog = LibraryFragment.getPlaylist();

    String playlistToAddSongTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    // playlistToAddSongTo which is the button user click when dialog box open and this is a string of a playlist name
    // On click listener to detect which playlist the user clicked on
    @Override
    public void onClick(View view) {
        String str = view.getTag().toString();
        for(int i = 0; i < playlistsDialog.size(); i++) {
            if (str.equals(playlistsDialog.get(i).getPlaylistName())) {
                playlistToAddSongTo = playlistsDialog.get(i).getPlaylistName();

                // Adding to playlist should happen here i think...
                // u might have to copy pasta ur writing functions from library fragment here
            }
        }
    }

}
