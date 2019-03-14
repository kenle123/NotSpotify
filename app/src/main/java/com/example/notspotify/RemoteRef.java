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
        try {
            InputStream inputStream = am.open("methods.json");
            myJson = inputStreamToString(inputStream);
        }
        catch (IOException e) {
            return null;
        }

        JsonObject jFile = new Gson().fromJson(myJson, JsonObject.class);
        JsonArray jArray = jFile.get("methods").getAsJsonArray();
        for(int i = 0; i < jArray.size(); i++) {
            String interested = jArray.get(i).getAsJsonObject().get("remoteMethod").getAsString();
            if (remoteMethod.equals(interested))
                return jArray.get(i).getAsJsonObject();

        }

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
