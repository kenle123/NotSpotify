package com.example.notspotify;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

/**
 * Session object to keep track of global variables
 */
public class Session  {
    private SharedPreferences pref;
    private User user;
    private static MediaPlayer mp;

    public Session(Context cntx) {
        pref = PreferenceManager.getDefaultSharedPreferences(cntx);
    }
    public void setUser(String jObj) {
        user = new Gson().fromJson(jObj, User.class);
    }
    public User getUser() {
        return user;
    }
    public void setLoginTrue(String login) {
        pref.edit().putBoolean("Login", true).apply();
    }

    public void setLoginFalse(String login) {
        pref.edit().putBoolean("Login", false).apply();
    }

    public void setUsername(String username) {
        pref.edit().putString("username", username).apply();
    }

    public String getUsername() {
        return pref.getString("username", "");
    }

    public void setPassword(String password) {
        pref.edit().putString("password", password).apply();
    }

    public String getPassword() {
        return pref.getString("password", "");
    }

    public Boolean getLogin() {
        Boolean login = pref.getBoolean("Login", false);
        return login;
    }

    public MediaPlayer getMediaPlayer()
    {
        return this.mp;
    }
    public void setMediaPlayer(MediaPlayer mp)
    {
        this.mp = mp;
    }
}

