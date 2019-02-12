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

    public void setupPlaylist(MusicList m) {
        for(UserPlaylist u: list) {
            u.stringToArrayPlaylist(m);
        }
    }

    public UserPlaylist getUserPlaylist(String uName) {
        for(UserPlaylist p : list)
        {
            if(p.getUsername().equals(uName))
                return p;
        }
        return null;
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
