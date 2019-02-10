package com.example.notspotify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;

public class DialogActivity extends AppCompatActivity {

//    Button firstPlaylist;
//    Button secondPlaylist;

    // Get song information user clicked on to add to playlist
    String songTitle = BrowseFragment.getSongTitle();
    String songID = BrowseFragment.getSongID();

    // Linearlayout for dialog that will display the buttons
    LinearLayout ll;

    // Get playlists from library fragment
    List<PlaylistSearchModel> playlistsDialog = LibraryFragment.getPlaylist();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        // Bind linear layout based on id declared in activity_dialog.xml
        ll = findViewById(R.id.linearLayout_Dialog);

//        firstPlaylist = findViewById(R.id.playlist1);
//        secondPlaylist = findViewById(R.id.playlist2);
//        firstPlaylist.setText(songTitle);
//        secondPlaylist.setText(songID);

        for(int i = 0; i < playlistsDialog.size(); i++) {
            Button playlistButton = new Button(this);
            playlistButton.setText(playlistsDialog.get(i).getPlaylistName());
            playlistButton.setId(i);
            ll.addView(playlistButton);
        }




    }
}
