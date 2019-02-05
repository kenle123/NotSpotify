package com.example.notspotify;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MusicList {
    @SerializedName("music")
    private List<Music> list = new ArrayList<Music>();

    public List<Music> getList()
    {
        return this.list;
    }

    @Override
    public String toString()
    {
        String results = "";
        for(Music m : list)
        {
            results += String.format("%" + 40 + "s", "ID: " + m.getSongID())
                    + String.format("%" + 40 + "s", "Title: " + m.getSongTitle())
                    + String.format("%" + 30 + "s", "Artist: " + m.getArtistName())
                    + "\n";
        }
        return results;
    }
}
