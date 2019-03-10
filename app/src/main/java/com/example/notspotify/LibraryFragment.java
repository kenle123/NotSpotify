package com.example.notspotify;

import android.app.AlertDialog;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

    Session session;
    String username;

    Button addPlaylistButton;
    EditText nameToAddEdittext;
    Button addToPlaylistButtonInvis;
    Button cancelAddingToPlaylistInvis;
    String nameToAddString;
    int playlistUserClickedOn_LibraryFragment = 0;

    PlaylistHandler playlistHandler;
    String path;
    File file;

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
        nameToAddEdittext = view.findViewById(R.id.edittext_libraryfragmentAddtoPlaylist);
        addToPlaylistButtonInvis = view.findViewById(R.id.button_addPlaylistInLibraryFragment);
        cancelAddingToPlaylistInvis = view.findViewById(R.id.button_cancelAddingPlaylist);

        // Bind listview
        listView = view.findViewById(R.id.list_view);

        // Get username from session
        session = new Session(getActivity());
        username = session.getUsername();
        MusicList musicList = loadJsonIntoMusicList();

        // Get path for local memory
        path = view.getContext().getFilesDir().getAbsolutePath() + "/playlists.json";
        file = new File(path);

        // Get current user's username and set the text to {Username} playlist as header
        mPlaylistUser = view.findViewById(R.id.textview_playlists);
        mPlaylistUser.setText(session.getUsername() + "'s Playlists");

        // Load the playlist from playlist.json into playlistHandler
        playlistHandler = loadJsonIntoPlaylist();
        playlistHandler.setupPlaylist(musicList);

        if(file.exists()) {
            playlistHandler =  updatePlaylistHandler(file);
        }

        // Checks if a user has at least one playlist
        boolean hasPlaylist = checkIfUserHasPlaylist();
        if((hasPlaylist)) {
            playlist.clear();
            UserPlaylist uPList;

            if(file.exists()) {
                PlaylistHandler newPlaylistHandler = updatePlaylistHandler(file);
                uPList = newPlaylistHandler.getUserPlaylist(username);
            }
            else {
                uPList = playlistHandler.getUserPlaylist(username);
            }

            // Add playlist names to listview which will display each playlist name
            for (int i = 0; i < uPList.getPlaylist().size(); i++) {
                playlist.add(new PlaylistSearchModel(uPList.getPlaylist().get(i).getPlaylistName(), uPList.getPlaylist().get(i).getSongs()));
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

        // Long click listener for when user wants to delete a playlist
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String path = view.getContext().getFilesDir().getAbsolutePath() + "/playlists.json";
                final File file = new File(path);
                playlistUserClickedOn_LibraryFragment = i;

                // Alert dialog to alert user whether he/she wants to delete the playlist or not
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setMessage("Do you want to delete this playlist?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int is) {
                                if(file.exists()) {
                                    PlaylistHandler newPlaylistHandler =  updatePlaylistHandler(file);
                                    deletePlaylist(newPlaylistHandler, playlistUserClickedOn_LibraryFragment);
                                }
                                else {
                                    deletePlaylist(playlistHandler, playlistUserClickedOn_LibraryFragment);
                                }
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

                boolean playlistExists = false;
                if(file.exists()) {
                    PlaylistHandler newPlaylistHandler = updatePlaylistHandler(file);
                    //playlistExists = checkUserName(inputUserName.getText().toString(), newUserList.getList(), v);
                    //Log.d("ADDUSER", "IN SIGN IN" +newUserList.toString());
                }
                else {
                    //playlistExists = checkUserName(inputUserName.getText().toString(), userList.getList(), v);
                }


                if(playlistExists == false){
                    if(file.exists()) {
                        playlistHandler =  updatePlaylistHandler(file);
                    }
                    addPlayList(playlistHandler);
                }
            }
        });
        return view;
    }

    /**
     * Loads the users from the playlists.json file into playlist object using GSON
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
     * @return true if the user has a playlist, false if doesn't
     */
    public boolean checkIfUserHasPlaylist() {
        if(playlistHandler.getUserPlaylist(username) == null) {
            addUserToPlaylist();
            playlistHandler = updatePlaylistHandler(file);
        }
        if(playlistHandler.getUserPlaylist(username).getPlaylist().size() > 0)
            return true;
        else
            return false;
    }

    /**
     * Loads the music from the music.json file into musicList object using GSON
     * @return the populatd music list
     */
    public MusicList loadJsonIntoMusicList()
    {
        try {
            String myJson = inputStreamToString(getActivity().getAssets().open("music.json"));
            MusicList musicList  = new Gson().fromJson(myJson, MusicList.class);
            return musicList;
        }
        catch (IOException e) {
            return null;
        }
    }

    /**
     * Updates userList using local memory json file
     * @param file - file of local json
     * @return pTemp - new updated PlaylistHandler
     */
    public PlaylistHandler updatePlaylistHandler(File file)
    {
        PlaylistHandler pTemp = null;
        try {
            if (file.exists()) {
                //Log.d("ADDUSER", "file exists IN SIGN IN");
                InputStream inputStream = new FileInputStream(file);
                String myJson = inputStreamToString(inputStream);
                pTemp = new Gson().fromJson(myJson, PlaylistHandler.class);
                inputStream.close();

                //Log.d("ADDUSER", userList.toString());
                return pTemp;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return pTemp;
    }

    /**
     * Add a playlist
     * @param p The playlists the user has
     */
    public void addPlayList(PlaylistHandler p) {
        p.getUserPlaylist(username).addPlaylist(nameToAddString);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String strJson = gson.toJson(p);
        String fileContents = strJson;
        //Log.d("ADDPLAYLIST", p.getUserPlaylist(username).toString());
        try {
            String filePath = getActivity().getFilesDir().getAbsolutePath() + "/playlists.json";
            File file = new File(filePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(strJson.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    /**
     * Delete a playlist
     * @param p The playlists the user has
     * @param i The index of the playlist to delete
     */
    public void deletePlaylist(PlaylistHandler p, int i) {
        p.getUserPlaylist(username).deletePlaylist(i);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String strJson = gson.toJson(p);
        String fileContents = strJson;
        //Log.d("DELETEPLAYLIST", p.getUserPlaylist(username).toString());
        try {
            String filePath = getActivity().getFilesDir().getAbsolutePath() + "/playlists.json";
            File file = new File(filePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(strJson.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }


    /**
     * Adds a use rto a playlist
     */
    public void addUserToPlaylist() {
        playlistHandler.addUserPlaylist(username);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String strJson = gson.toJson(playlistHandler);
        String fileContents = strJson;
        //Log.d("ADDSONG", p.getUserPlaylist(username).toString());
        try {
            String filePath = getActivity().getApplicationContext().getFilesDir().getAbsolutePath() + "/playlists.json";
            File file = new File(filePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(strJson.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
            //Log.d("ADDUSER", "OUTPUTTED");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Getters for playlist information
    public static List<PlaylistSearchModel> getPlaylist() { return playlist; }
    public static int getPlaylistUserClickedOn() { return playlistUserClickedOn; }
}