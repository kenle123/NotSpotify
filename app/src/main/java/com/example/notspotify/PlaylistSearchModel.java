package com.example.notspotify;

import java.util.List;

public class PlaylistSearchModel {
    private String mUser;
    private String mPlaylistName;
    private List<Music> mPlaylistSongs;

    public PlaylistSearchModel(String user, String playlistName, List<Music> playlistSongs) {
        this.mUser = user;
        this.mPlaylistName = playlistName;
        this.mPlaylistSongs = playlistSongs;
    }

    public String getUser() {
        return mUser;
    }

    public String getPlaylistName() {
        return mPlaylistName;
    }

    public List<Music> getPlaylistSongs() {
        return mPlaylistSongs;
    }

    @Override
    public String toString() {
        return mPlaylistName;
    }

}
