package com.example.notspotify;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LibraryFragment extends Fragment {

    public LibraryFragment() {
        // Required empty public constructor
    }

    ListView listView;
    TextView mPlaylistUser;

    Session session;
    String username;
    String currUser = "";
    UserPlaylist usersPlaylist;

    Button addPlaylistButton;
    Button deletePlaylistButton;

    // Declare global variables to be used throughout each activity/fragment
    private static List<PlaylistSearchModel> playlist = new ArrayList<>();
    private static int playlistUserClickedOn = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        // Bind buttons for adding and delete playlists
        addPlaylistButton = view.findViewById(R.id.button_add_playlist);
        deletePlaylistButton = view.findViewById(R.id.button_delete_playlist);

        // Get username from session
        session = new Session(getActivity());
        username = session.getUsername();

        //Log.i("ggezcurr", currUser);
        //Log.i("ggez", username);

        // Load the playlist from playlist.json into playlistHandler
        final PlaylistHandler playlistHandler = loadJsonIntoPlaylist();

        // Get current user's username and set the text to {Username} playlist as header
        Session session = new Session(getActivity());
        mPlaylistUser = view.findViewById(R.id.textview_playlists);
        mPlaylistUser.setText(session.getUsername() + "'s Playlists");

        listView = view.findViewById(R.id.list_view);
        List<UserPlaylist> playlist2 = playlistHandler.getList();

        Log.i("gg", "curr username is: " + currUser);
        Log.i("gg", "username is: " + username);

        boolean hasPlaylist = checkIfUserHasPlaylist(playlist2, username);
        if((hasPlaylist)) {
            if(currUser.equals("")) {
                currUser = username;
                playlist.clear();
                // Add playlist names to listview which will display each playlist name
                for (int i = 0; i < usersPlaylist.getPlaylist().size(); i++) {
                    playlist.add(new PlaylistSearchModel(usersPlaylist.getPlaylist().get(i).getPlaylistName(), usersPlaylist.getPlaylist().get(i).getSongs()));
                }
            }
        }
        else {
            // User does not have a playlist, so will just display nothing
        }
        // Array adapter needed to display the listview
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, playlist);
        listView.setAdapter(arrayAdapter);

        // On click listener for when user clicks on an item in the listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                playlistUserClickedOn = i;
                Intent intent = new Intent(getActivity(), PlaylistSongsActivity.class);
                startActivity(intent);
            }
        });

        // On click listener for add playlist button
        addPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // On click listener for delete playlist button
        deletePlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

    /**
     * Loads the users from the playlists.json file into playlist object using GSON
     *
     * @return the populated playlists
     */
    public PlaylistHandler loadJsonIntoPlaylist() {
        try {
            String myJson = inputStreamToString(getActivity().getAssets().open("playlists.json"));
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
     * Checks if the user has a playlist
     * @param up The userplaylist object to loop through
     * @param username The username that is currently logged in
     * @return true if the user has a playlist, false if doesn't
     */
    public boolean checkIfUserHasPlaylist(List<UserPlaylist> up, String username) {
        for (int i = 0; i < up.size(); i++) {
            if (username.equals(up.get(i).getUsername())) {
                usersPlaylist = up.get(i);
                return true;
            }
        }
        return false;
    }

    // Getters for playlist information
    public static List<PlaylistSearchModel> getPlaylist() { return playlist; }
    public static int getPlaylistUserClickedOn() { return playlistUserClickedOn; }
}