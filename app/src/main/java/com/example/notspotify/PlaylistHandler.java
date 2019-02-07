package com.example.notspotify;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.*;

import java.io.FileNotFoundException;
import java.io.File;
import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;

public class PlaylistHandler{
    String jsonString;
    HashMap<String, ArrayList<Music>> list;

    public PlaylistHandler() {
        jsonString = "";
        try {
            String path = "D:\\MY STUFF\\CSULB Senior Spring 2019 Semester\\CECS 327\\NotSpotify\\app\\src\\main\\assets\\playlists.json";
            File file = new File(path);
            Scanner in = new Scanner(file);
            while(in.hasNextLine())
                jsonString += in.nextLine();
            Log.d("jsonString", jsonString);
        }
        catch(FileNotFoundException e) {Log.d("NOWORK", "File reading doesn't work");}

    }

}
