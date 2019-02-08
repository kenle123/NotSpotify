package com.example.notspotify;

import java.util.List;

public class PlaylistSearchModel {
    private String mPlaylistName;

    public PlaylistSearchModel(String playlistName) {
        this.mPlaylistName = playlistName;
    }

    public String getPlaylistName() {
        return mPlaylistName;
    }

    @Override
    public String toString() {
        return mPlaylistName;
    }

}
