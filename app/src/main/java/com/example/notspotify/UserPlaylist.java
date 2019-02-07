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
        List<Music> songList;
        public void setPlaylistName(String n){playlistName = n;}
        public String getPlaylistName(){return playlistName;}
        public String getSongs(){return songs;}
        public List<Music> getSongList(){return songList;}
        public void setSongList(List<Music> s){songList = s;}
    }

    @SerializedName("user")
    String username;
    @SerializedName("playLists")
    List<Playlist> plst = new ArrayList<Playlist>();

    public void stringToArrayPlaylist(MusicList m) {
        for(int i = 0; i < plst.size(); i++) {
            String[] s = plst.get(i).getSongs().split(",");
            List<Music> a = new ArrayList<Music>();
            for(int j = 0; j < s.length; j++) {
                for(int k = 0; k < m.getList().size(); k++) {
                    if(s[j].equals(m.getList().get(k).getSongID())) {
                        a.add(m.getList().get(k));
                        k = m.getList().size();
                    }
                }//k
            }//j
            plst.get(i).setSongList(a);
        }//i
    }//stringToArrayPlaylist

    public List<Playlist> getPlaylist(){return plst;}
    public String toString() {
        String results = "\nUsername: " + username;
        for(Playlist p : plst)
        {
            results += "\n"
                    + "Playlist Name:" + "\n"
                    +  p.getPlaylistName() + "\n"
                    + "Songs:";
            for(Music m: p.getSongList()) {
                results += m.toString();
            }
        }
        return results;
    }
}
