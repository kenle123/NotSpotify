package com.example.notspotify;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PlaylistHandler {
    @SerializedName("plst")
    private List<UserPlaylist> list = new ArrayList<UserPlaylist>();

    public List<UserPlaylist> getList()
    {
        return this.list;
    }

    @Override
    public String toString()
    {
        String results = "";
        for(UserPlaylist p : list)
        {
            results += "\n" + p.toString();
        }
        return results;
    }
}
