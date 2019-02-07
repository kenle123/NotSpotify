package com.example.notspotify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class DialogActivity extends AppCompatActivity {

    Button firstPlaylist;
    Button secondPlaylist;

    String songTitle = BrowseFragment.getSongTitle();
    String songID = BrowseFragment.getSongID();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        firstPlaylist = findViewById(R.id.playlist1);
        secondPlaylist = findViewById(R.id.playlist2);
        firstPlaylist.setText(songTitle);
        secondPlaylist.setText(songID);
    }
}
