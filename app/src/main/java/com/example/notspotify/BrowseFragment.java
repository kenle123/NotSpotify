package com.example.notspotify;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

/**
 * Browse fragment which is the first tab in the bottom navigation bar
 * Will display all the songs and users will be able to make a search by artist name and song title
 */
public class BrowseFragment extends Fragment {

    public BrowseFragment() {
        // Required empty constructor
    }
    private static Session session = MainActivity.getSession();
    private static MusicList musicList;
    // Declare global song variables which will be passed to play activity to play a certain song
    private static String mSongTitle;
    private static String mSongID;
    public static Context cxt = MainActivity.getContext();

    // Song list which has an arraylist of all the songs
    private static List<SearchModel> songList = new ArrayList<>();

    // Listview which will display all of the songs
    ListView listView;

    // For searching songs
    private static String songToSearchFor = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (musicList == null)
            musicList = getMusicListFromServer();
        if (session.getUser() == null)
            session.setUser(getUserFromServer());

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_browse, container, false);

        // For adding song to search dialog
        view.findViewById(R.id.button_search).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final EditText taskEditText = new EditText(getContext());
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle("Search for artist/song")
                        .setMessage("Format: artistName - songName")
                        .setView(taskEditText)
                        .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                songToSearchFor = String.valueOf(taskEditText.getText());
                                Toast.makeText(getContext(), songToSearchFor, Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;
            }
        });

        // Whenever user clicks on search button, will open on click listener to search for a song/artist
        view.findViewById(R.id.button_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(getActivity(), "Search...",
                        "Search For An Artist or Song", null, initData2(), new SearchResultListener<SearchModel>() {

                    public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, SearchModel item, int i) {

                        // Set the global variables to the song that is selected
                        mSongTitle = item.getTitle();
                        mSongID = item.getID();

                        // Alert dialog to alert user whether he/she wants to add to playlist or play the song
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                        alertDialog.setMessage("Select an option...").setCancelable(false)
                                .setPositiveButton("Add to Playlist", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int is) {
                                        // Call dialog to display detail
                                        // Create dialog activity
                                        Intent intent = new Intent(getActivity(), DialogActivity.class);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("Play Song", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // Call intent to go to play activity where user can play the song
                                        Intent intent = new Intent(getActivity(), PlayActivity.class);
                                        startActivity(intent);
                                    }
                                });

                        AlertDialog alert = alertDialog.create();
                        alert.setTitle("What would you like to do?");
                        alert.show();
                        baseSearchDialogCompat.dismiss();
                    }
                }).show();
            }
        });

        // Displays all the songs that are all playable but not searchable(have to use search button)
        listView = view.findViewById(R.id.list_view);
        List<Music> musicList2 = musicList.getList();

        // Prevent repeating songs to be added
        if(songList.size() == 0) {
            // Add songs to songList which will contain the artist name and title of each song
            for (int i = 0; i < musicList2.size(); i++) {
                songList.add(new SearchModel(musicList2.get(i).getArtistName(), musicList2.get(i).getSongID(), musicList2.get(i).getSongTitle()));
            }
        }

        // Set up adapter
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, songList);
        listView.setAdapter(arrayAdapter);

        // On click listener for when user clicks on song which will go to play activity which plays the song
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Set the global variables to the song that is selected
                mSongTitle = songList.get(i).getTitle();
                mSongID = songList.get(i).getID();

                // Call intent to go to play activity where user can play the song
                Intent intent = new Intent(getActivity(), PlayActivity.class);
                startActivity(intent);
//                playSong();
//                Runnable r = new PlayClient(getContext());
//                new Thread(r).start();
            }
        });

        // On item LONG click listener
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Set the global variables to the song that is selected
                mSongTitle = songList.get(i).getTitle();
                mSongID = songList.get(i).getID();

                // Call intent to dialog activity which displays a dialog box for which playlist
                // the user wants to add the song to
                Intent intent = new Intent(getActivity(), DialogActivity.class);
                startActivity(intent);
                return true;
            }
        });
        return view;
    }

    public static MusicList getMusicListFromServer() {
        Proxy proxy = new Proxy(cxt);
        JsonObject ret;
        JsonArray jArray = new JsonArray();
        JsonObject jObj = new JsonObject();
        String[] array2 = {"0"};
        do {
            ret = proxy.synchExecution("returnSongs", array2);
            jArray.addAll(ret.get("musiclist").getAsJsonArray());
            array2[0] = ret.get("currentIndex").getAsString();
        } while(ret.get("keepPulling").getAsString().equals("true"));
        jObj.add("music",jArray);
        return new Gson().fromJson(jObj.toString(), MusicList.class);
    }
    public static void playSong() {
        Proxy proxy = new Proxy(cxt);
        String[] array2 = {mSongID};
        proxy.synchExecution("SongHandler", array2);
    }

    public static String getUserFromServer() {
        Proxy proxy = new Proxy(cxt);
        String[] array = {  session.getUsername()   };
        JsonObject ret = proxy.synchExecution("getUser", array);
        return ret.toString();
    }
    public static MusicList getMusicList() {
        return musicList;
    }

    /**
     * Initialize the search arraylist with the songs
     * @param musicList The list filled with music from music.json
     * @return The populated list of music
     */
    private ArrayList<SearchModel> initData(List<Music> musicList) {
        ArrayList<SearchModel> items = new ArrayList<>();

        for(int i = 0; i < musicList.size(); i++) {
            items.add(new SearchModel(musicList.get(i).getArtistName(), musicList.get(i).getSongID(), musicList.get(i).getSongTitle()));
        }
        return items;

        //initData(musicList.getList())
    }

    private ArrayList<SearchModel> initData2() {
        ArrayList<SearchModel> items = new ArrayList<>();
        return items;
    }

    // Getters for the song variables
    public static String getSongTitle() {
        return mSongTitle;
    }
    public static String getSongID() {
        return mSongID;
    }

    // Getter for song list
    public static List<SearchModel> getSongList() { return songList; }
}