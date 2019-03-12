package com.example.notspotify;

import com.google.gson.JsonObject;

public class RemoteRef implements RemoteRefInterface {
    public RemoteRef(){}
    public JsonObject getRemoteReference(String remoteMethod) {
        JsonObject returnJson = new JsonObject();

        if(remoteMethod.equals("Login")) {
            //returnJson.addProperty("remoteMethod", "handleSignIn");
            returnJson.addProperty("object", "SignIn"); // SignIn = classname
        }

        if(remoteMethod.equals("SignUp")) {
            //returnJson.addProperty("remoteMethod", "handleSignIn");
            returnJson.addProperty("object", "SignIn");
        }

        if(remoteMethod.equals("returnSongs"))
            returnJson.addProperty("object", "MusicList");

        if(     remoteMethod.equals("getUser") ||
                remoteMethod.equals("addPlaylist") ||
                remoteMethod.equals("deletePlaylist") ||
                remoteMethod.equals("addSongToPlaylist") ||
                remoteMethod.equals("deleteSongFromPlaylist")   )
            returnJson.addProperty("object", "EditUser");

        if(remoteMethod.equals("SongHandler"))
            returnJson.addProperty("object", "SongHandler");

        return returnJson;
    }
}
