package com.example.notspotify;

import android.content.Context;
import android.content.res.AssetManager;
import android.renderscript.ScriptGroup;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class RemoteRef implements RemoteRefInterface {
    Context context;
    AssetManager am;
    public RemoteRef(Context cxt){
        this.context = cxt;
    }
    public JsonObject getRemoteReference(String remoteMethod) {
        JsonObject returnJson = new JsonObject();
        am = context.getAssets();

        String myJson = "";
        Log.d("GGEZ", "got here 1");

        Log.d("GGEZ", "got here 2");
        //File file = new File(path);
        Log.d("GGEZ", "got here 3");

        try {
            InputStream inputStream = am.open("methods.json");

            //InputStream inputStream = new FileInputStream(file);
            Log.d("GGEZ", "got here 4");

            myJson = inputStreamToString(inputStream);
            Log.d("GGEZ", "got here 5");
            Log.d("GGEZ", myJson);

        }
        catch (IOException e) {
            Log.d("GGEZ", "got here 6");
            return null;
        }

        JsonObject jFile = new Gson().fromJson(myJson, JsonObject.class);
        JsonArray jArray = jFile.get("methods").getAsJsonArray();
        for(int i = 0; i < jArray.size(); i++) {
            String interested = jArray.get(i).getAsJsonObject().get("remoteMethod").getAsString();
            Log.d("GGEZ", interested);
            if (remoteMethod.equals(interested))
                Log.d("DEAD", "META FOUND");
            return jArray.get(i).getAsJsonObject();

        }

//        if(remoteMethod.equals("Login")) {
//            //returnJson.addProperty("remoteMethod", "handleSignIn");
//            returnJson.addProperty("object", "SignIn"); // SignIn = classname
//        }
//
//        if(remoteMethod.equals("SignUp")) {
//            //returnJson.addProperty("remoteMethod", "handleSignIn");
//            returnJson.addProperty("object", "SignIn");
//        }
//
//        if(remoteMethod.equals("returnSongs"))
//            returnJson.addProperty("object", "MusicList");
//
//        if(     remoteMethod.equals("getUser") ||
//                remoteMethod.equals("addPlaylist") ||
//                remoteMethod.equals("deletePlaylist") ||
//                remoteMethod.equals("addSongToPlaylist") ||
//                remoteMethod.equals("deleteSongFromPlaylist")   )
//            returnJson.addProperty("object", "EditUser");
//
//        if(remoteMethod.equals("SongHandler"))
//            returnJson.addProperty("object", "SongHandler");

        return returnJson;
    }
    /**
     * Reads a file using inputstream
     *
     * @param inputStream a file to read from
     * @return a string of the read in file
     */
    public static String inputStreamToString(InputStream inputStream) {
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);
            String json = new String(bytes);
            return json;
        } catch (IOException e) {
            return null;
        }
    }
}
