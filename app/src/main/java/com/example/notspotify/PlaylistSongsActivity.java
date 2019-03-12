package com.example.notspotify;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Displays all the songs in a playlist
 */
public class PlaylistSongsActivity extends AppCompatActivity {
    static Session session = MainActivity.getSession();
    MusicList musicList = BrowseFragment.getMusicList();
    ImageButton backButton;
    ListView listViewPlaylistSongs;
    TextView tv_playlistSongs;

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

        // Bind variables
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
            for(int j = 0; j < musicList.size(); j++) {
                if(playlistSongsBefore.get(i).equals(musicList.get(j).getSongID())) {
                    playlistSongs.add(musicList.get(j).getSongTitle());

                    // Store title and id into arraylists to use in onclick function
                    title.add(musicList.get(j).getSongTitle());
                    identification.add(musicList.get(j).getSongID());
                }
            }
        }

        // Array adapter needed to display the listview
        final ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, playlistSongs);
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

        listViewPlaylistSongs.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                songTitle2 = title.get(i);
                songID2 = identification.get(i);
                Toast.makeText(PlaylistSongsActivity.this, "Deleted " + songTitle2 + " from " + playlist.get(playlistUserClickedOn).getPlaylistName(), Toast.LENGTH_LONG).show();
                deleteSongFromPlaylist(playlist.get(playlistUserClickedOn).getPlaylistName());
                LibraryFragment.populatePlaylist();
                playlist = LibraryFragment.getPlaylist();
                finish();
                startActivity(getIntent());
                return true;
            }
        });
    }

    /**
     * Deletes a song from a playlist
     */
    public void deleteSongFromPlaylist(String pName) {
        Proxy proxy = new Proxy();
        String[] array = {  session.getUsername(),
                            pName,
                            songID2                  };
        JsonObject ret = proxy.synchExecution("deleteSongFromPlaylist", array);
        session.setUser(ret.toString());
    }


    // Getters for the song variables
    public static String getSongTitle() { return songTitle2; }
    public static String getSongID() { return songID2; }
}
