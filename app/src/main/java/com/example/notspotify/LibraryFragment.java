package com.example.notspotify;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Library fragment which displays all the user's playlists
 */
public class LibraryFragment extends Fragment {

    public LibraryFragment() {
        // Required empty public constructor
    }

    ListView listView;
    TextView mPlaylistUser;

    static Session session = MainActivity.getSession();
    String username;

    Button addPlaylistButton;
    EditText nameToAddEdittext;
    Button addToPlaylistButtonInvis;
    Button cancelAddingToPlaylistInvis;
    String nameToAddString;
    int playlistUserClickedOn_LibraryFragment = 0;

    // Declare global variables to be used throughout each activity/fragment
    private static List<PlaylistSearchModel> playlist = new ArrayList<>();
    private static int playlistUserClickedOn = 0;
    public static Context cxt = MainActivity.getContext();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        // Bind buttons for adding and delete playlists
        addPlaylistButton = view.findViewById(R.id.button_add_playlist);
        nameToAddEdittext = view.findViewById(R.id.edittext_libraryfragmentAddtoPlaylist);
        addToPlaylistButtonInvis = view.findViewById(R.id.button_addPlaylistInLibraryFragment);
        cancelAddingToPlaylistInvis = view.findViewById(R.id.button_cancelAddingPlaylist);

        // Bind listview
        listView = view.findViewById(R.id.list_view);

        // Get username from session
        username = session.getUsername();

        // Get path for local memory
        // Get current user's username and set the text to {Username} playlist as header
        mPlaylistUser = view.findViewById(R.id.textview_playlists);
        mPlaylistUser.setText(session.getUsername() + "'s Playlists");

        populatePlaylist();

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

        // Long click listener for when user wants to delete a playlist
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                playlistUserClickedOn_LibraryFragment = i;

                // Alert dialog to alert user whether he/she wants to delete the playlist or not
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setMessage("Do you want to delete this playlist?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int is) {
                                deletePlaylist(playlist.get(playlistUserClickedOn_LibraryFragment).getPlaylistName());
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                AlertDialog alert = alertDialog.create();
                alert.setTitle("Delete Playlist");
                alert.show();
                return true;
            }
        });

        // On click listener for add playlist button which makes views invisible
        addPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameToAddEdittext.setVisibility(View.VISIBLE);
                addToPlaylistButtonInvis.setVisibility(View.VISIBLE);
                cancelAddingToPlaylistInvis.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                addPlaylistButton.setVisibility(View.GONE);
            }
        });

        // On click button which will cancel the adding playlist views and make them invisible
        cancelAddingToPlaylistInvis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameToAddEdittext.setVisibility(View.GONE);
                addToPlaylistButtonInvis.setVisibility(View.GONE);
                cancelAddingToPlaylistInvis.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                addPlaylistButton.setVisibility(View.VISIBLE);
            }
        });

        // On click listener for getting the name of the playlist
        addToPlaylistButtonInvis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Empty input
                if(nameToAddEdittext.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Playlist Name Cannot Be Empty!", Toast.LENGTH_LONG).show();
                }
                //Non empty input which will be new playlist name
                else {
                    nameToAddString = nameToAddEdittext.getText().toString();
                    nameToAddEdittext.setVisibility(View.GONE);
                    addToPlaylistButtonInvis.setVisibility(View.GONE);
                    cancelAddingToPlaylistInvis.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    addPlaylistButton.setVisibility(View.VISIBLE);
                }
                addPlayList(nameToAddString);
            }
        });
        return view;
    }

    /**
     * Add a playlist
     */
    public void addPlayList(String pName) {
        Proxy proxy = new Proxy(cxt);
        String[] array = {  session.getUsername(),
                            pName                   };
        JsonObject ret = proxy.synchExecution("addPlaylist", array);
        session.setUser(ret.toString());
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    /**
     * Delete a playlist
     */
    public void deletePlaylist(String pName) {
        Proxy proxy = new Proxy(cxt);
        String[] array = {  session.getUsername(),
                            pName                   };
        JsonObject ret = proxy.synchExecution("deletePlaylist", array);
        session.setUser(ret.toString());
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

//    public void refreshFrag() {
//        FragmentTransaction ft =  getFragmentManager().beginTransaction();
//        ft.detach(LibraryFragment.this).attach(LibraryFragment.this).commit();
//    }

    public static void populatePlaylist() {
        playlist.clear();
        for (int i = 0; i < session.getUser().getListOfPlaylists().size(); i++) {
            playlist.add(new PlaylistSearchModel(session.getUser().getListOfPlaylists().get(i).getPlaylistName(),
                    session.getUser().getListOfPlaylists().get(i).getSongIDsAsString()));
        }
    }
    // Getters for playlist information
    public static List<PlaylistSearchModel> getPlaylist() { return playlist; }
    public static int getPlaylistUserClickedOn() { return playlistUserClickedOn; }
}