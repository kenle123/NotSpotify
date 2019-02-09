package com.example.notspotify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlaylistSongsActivity extends AppCompatActivity {

    ImageButton backButton;
    ListView listViewPlaylistSongs;
    TextView tv_playlistSongs;

    // Get songlist from browse fragment
    List<SearchModel> songList = BrowseFragment.getSongList();

    // Get playlist name and index from library fragment
    List<PlaylistSearchModel> playlist = LibraryFragment.getPlaylist();
    int playlistUserClickedOn = LibraryFragment.getPlaylistUserClickedOn();

    // Used for songs before added to playlist
    List<String> playlistSongsBefore;
    String playlistSongsOneString;

    // Declare global song variables which will be passed to play activity 2 to play a certain song
    private static String songTitle2;
    private static String songID2;

    // Arraylist used to determine what song was picked in the playist
    List<String> title = new ArrayList<>();
    List<String> identification = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_songs);

        backButton = findViewById(R.id.button_back_playlistSongs);
        listViewPlaylistSongs = findViewById(R.id.listview_playlistSongs);
        tv_playlistSongs = findViewById(R.id.textview_playlistSongs);

        // On click listener for back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Set textview to playlist name
        tv_playlistSongs.setText(playlist.get(playlistUserClickedOn).getPlaylistName());

        // Put all playlist songs into one string that will be split up and inserted into playlistSongs arraylist
        playlistSongsOneString = playlist.get(playlistUserClickedOn).getSongs();
        playlistSongsBefore = new ArrayList<>(Arrays.asList(playlistSongsOneString.split(",")));

        // Official songs in the playlist
        List<String> playlistSongs = new ArrayList<>();

        // If the playlist song ID matches the song list ID, add to playlistSongs arraylist
        for(int i = 0; i < playlistSongsBefore.size(); i++) {
            for(int j = 0; j < songList.size(); j++) {
                if(playlistSongsBefore.get(i).equals(songList.get(j).getID())) {
                    playlistSongs.add(songList.get(j).getTitle());

                    // Store title and id into arraylists to use in onclick function
                    title.add(songList.get(j).getTitle());
                    identification.add(songList.get(j).getID());
                }
            }
        }

        // Array adapter needed to display the listview
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, playlistSongs);
        listViewPlaylistSongs.setAdapter(arrayAdapter);

        // On click listener when user clicks on a song in a playlist
        listViewPlaylistSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Set the global variables to the song that is selected
                songTitle2 = title.get(i);
                songID2 = identification.get(i);

                // Call intent to go to play activity where user can play the song
                Intent intent = new Intent(PlaylistSongsActivity.this, PlayActivity2.class);
                startActivity(intent);
            }
        });
    }

    // Getters for the song variables
    public static String getSongTitle() {
        return songTitle2;
    }
    public static String getSongID() {
        return songID2;
    }
}
