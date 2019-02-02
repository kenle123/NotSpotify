package com.example.notspotify;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class User implements Serializable {
    @SerializedName("username")
    String username;
    @SerializedName("password")
    String password;
    @SerializedName("playlistJson")
    String playlistJson;


    public void setUserName(String name)
    {
        this.username = name;
    }
    public String getUserName()
    {
        return this.username;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
    public String getPassword()
    {
        return this.password;
    }
    public void setJson(String json)
    {
        this.playlistJson = json;
    }
    public String getJson()
    {
        return this.playlistJson;
    }

    @Override
    public String toString()
    {
        return ("Username: " + username + ", Password: " + password + " ");
    }
}
