package com.example.notspotify;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import com.google.gson.Gson;

import android.os.Bundle;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class Session  {
    private SharedPreferences pref;

    public Session(Context cntx) {

        pref = PreferenceManager.getDefaultSharedPreferences(cntx);
    }
    public void setLoginFalse(String login) {
        pref.edit().putBoolean("Login", false).apply();
    }

    public void setLoginTrue(String login) {
        pref.edit().putBoolean("Login", true).apply();
    }

    public Boolean getLogin() {
        Boolean login = pref.getBoolean("Login", false);
        return login;
    }

//    public UserList loadJsonIntoUserList()
//    {
//        try
//        {
//            String myJson = inputStreamToString(getAssets().open("users.json"));
//            UserList userList  = new Gson().fromJson(myJson, UserList.class);
//            return userList;
//        }
//        catch (IOException e) {
//            return null;
//        }
//    }
//
//    public String inputStreamToString(InputStream inputStream) {
//        try {
//            byte[] bytes = new byte[inputStream.available()];
//            inputStream.read(bytes, 0, bytes.length);
//            String json = new String(bytes);
//            return json;
//        } catch (IOException e) {
//            return null;
//        }
//    }
}

