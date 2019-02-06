package com.example.notspotify;

import org.json.JSONArray;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;

public class PlaylistHandler {
    String jsonString;
    HashMap<String, ArrayList<Music>> list;

    public PlaylistHandler() {
        jsonString = "";
        try {
            File file = new File("playlists.json");
            BufferedReader br = new BufferedReader(new FileReader(file));
            //read json text here
            while ((jsonString += br.readLine()) != null){}
        }
        catch (IOException e) {}

        //At this point, the json file has been read and saved in jsonString


    }

}
