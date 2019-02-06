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
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class BrowseFragment extends Fragment {

    public BrowseFragment() {
        // Required empty public constructor
    }

    // Declare global song variables which will be passed to play activity to play a certain song
    private static String songTitle;
    private static String songID;

    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_browse, container, false);

        // MusicList variable to hold the music information
        final MusicList musicList = loadJsonIntoMusicList();

        // Whenever user clicks on search button, will open on click listener to search for a song/artist
        view.findViewById(R.id.button_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(getActivity(), "Search...",
                        "Search For An Artist or Song", null, initData(musicList.getList()), new SearchResultListener<SearchModel>() {

                    public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, SearchModel item, int i) {

                        // Set the global variables to the song that is selected
                        songTitle = item.getTitle();
                        songID = item.getID();

                        // Call intent to go to play activity where user can play the song
                        Intent intent = new Intent(getActivity(), PlayActivity.class);
                        startActivity(intent);
                        baseSearchDialogCompat.dismiss();
                    }
                }).show();
            }
        });

        // Dislays all the songs that are all playable but not searchable(have to use search button)
        listView = view.findViewById(R.id.list_view);
        final List<SearchModel> songList = new ArrayList<>();
        List<Music> musicList2 = musicList.getList();

        // Add songs to songList which will contain the artist name and title of each song
        for(int i = 0; i < musicList2.size(); i++) {
            songList.add(new SearchModel(musicList2.get(i).getArtistName(), musicList2.get(i).getSongID(), musicList2.get(i).getSongTitle()));
        }

        // Set up adapter
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, songList);
        listView.setAdapter(arrayAdapter);

        // On click listener for when user clicks on song which will go to play activity which plays the song
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Set the global variables to the song that is selected
                songTitle = songList.get(i).getTitle();
                songID = songList.get(i).getID();

                // Call intent to go to play activity where user can play the song
                Intent intent = new Intent(getActivity(), PlayActivity.class);
                startActivity(intent);
            }
        });



        return view;
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
    }

    // Getters for the song variables
    public static String getSongTitle() {
        return songTitle;
    }

    public static String getSongID() {
        return songID;
    }
}
