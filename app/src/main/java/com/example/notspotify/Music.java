package com.example.notspotify;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Music implements Serializable {
    class Artist {
        @SerializedName("name")
        String artistName;
    }
    class Song {
        @SerializedName("id")
        String songID;
        @SerializedName("title")
        String songTitle;
    }

    //Initialize classes
    @SerializedName("artist")
    Artist artist;
    @SerializedName("song")
    Song song;

    //Functions
    public void setArtistName(String n) {
        this.artist.artistName = n;
    }
    public String getArtistName() {
        return this.artist.artistName;
    }
    public void setSongID(String n) {
        this.song.songID = n;
    }
    public String getSongID() {
        return this.song.songID;
    }
    public void setSongTitle(String n) {
        this.song.songTitle = n;
    }
    public String getSongTitle() {
        return this.song.songTitle;
    }
}

