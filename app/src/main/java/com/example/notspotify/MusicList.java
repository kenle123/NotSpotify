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
            results += m.toString();
        }
        return results;
    }
}
