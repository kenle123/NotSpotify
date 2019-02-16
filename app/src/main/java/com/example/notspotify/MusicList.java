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

    public Music getSong(String id) {
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).getSongID().equals(id))
                return list.get(i);
        }
        return null;
    }

    @Override
    public String toString() {
        String results = "";
        for(Music m : list) {
            results += m.toString();
        }
        return results;
    }
}
