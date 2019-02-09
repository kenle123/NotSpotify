package com.example.notspotify;

import java.util.List;

public class PlaylistSearchModel {
    private String mPlaylistName;
    private String mSongs;

    public PlaylistSearchModel(String playlistName, String songs) {
        this.mPlaylistName = playlistName;
        this.mSongs = songs;
    }

    public String getPlaylistName() {
        return mPlaylistName;
    }

    public String getSongs() { return mSongs; }

    @Override
    public String toString() {
        return mPlaylistName;
    }

}
