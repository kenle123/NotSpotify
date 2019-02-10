package com.example.notspotify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

public class DialogActivity extends AppCompatActivity implements View.OnClickListener {

    // Get song information user clicked on to add to playlist
    String songTitle = BrowseFragment.getSongTitle();
    String songID = BrowseFragment.getSongID();

    // Linearlayout for dialog that will display the buttons
    LinearLayout ll;
    Button playlistButton;

    // Get playlists from library fragment
    List<PlaylistSearchModel> playlistsDialog = LibraryFragment.getPlaylist();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

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

    // On click listener to detect which playlist the user clicked on
    @Override
    public void onClick(View view) {
        String str = view.getTag().toString();
        for(int i = 0; i < playlistsDialog.size(); i++) {
            if (str.equals(playlistsDialog.get(i).getPlaylistName())) {
                Toast.makeText(getApplicationContext(), playlistsDialog.get(i).getPlaylistName(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
