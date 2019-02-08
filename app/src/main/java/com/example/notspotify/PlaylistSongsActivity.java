package com.example.notspotify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class PlaylistSongsActivity extends AppCompatActivity {

    Session session;
    String username;

    ImageButton backButton;
    ListView listViewPlaylistSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_songs);

        session = new Session(this);
        username = session.getUsername();

        backButton = findViewById(R.id.button_back_playlistSongs);
        listViewPlaylistSongs = findViewById(R.id.listview_playlistSongs);

        // On click listener for back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Test data
        List<String> playlistSongs = new ArrayList<>();
        playlistSongs.add("Song 1");
        playlistSongs.add("Song 2");
        playlistSongs.add("Song 3");
        playlistSongs.add("Song 4");
        playlistSongs.add("Song 5");
        playlistSongs.add("Song 6");
        playlistSongs.add("Song 7");
        playlistSongs.add("Song 8");
        playlistSongs.add("Song 9");
        playlistSongs.add("Song 10");
        playlistSongs.add("Song 11");
        playlistSongs.add("Song 12");
        playlistSongs.add("Song 13");
        playlistSongs.add("Song 14");
        playlistSongs.add("Song 15");
        playlistSongs.add("Song 16");

        //Array adapter needed to display the listview
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, playlistSongs);
        listViewPlaylistSongs.setAdapter(arrayAdapter);

    }
}
