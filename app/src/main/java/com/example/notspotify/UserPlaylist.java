package com.example.notspotify;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserPlaylist implements Serializable {
    class Playlist {
        @SerializedName("plName")
        String playlistName;
        @SerializedName("songs")
        String songs;
        public void setPlaylistName(String n){playlistName = n;}
        public String getPlaylistName(){return playlistName;}
        public String getSongs(){return songs;}
    }

    @SerializedName("user")
    String username;
    @SerializedName("playLists")
    List<Playlist> playlist = new ArrayList<Playlist>();

    public List<Playlist> getPlaylist(){return playlist;}
    public String toString() {
        String results = "\nUsername: " + username;
        for(Playlist p : playlist)
        {
            results += "\n"
                    + String.format("%" + 40 + "s", "Playlist Name: " + p.getPlaylistName()) + "\n"
                    + String.format("%" + 40 + "s", "Songs: " + p.getSongs());
        }
        return results;
    }
}
