package com.example.notspotify;

import android.text.TextUtils;

import java .util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class UserList
{
    @SerializedName("users")
    private List<User> list = new ArrayList<User>();

    public List<User> getList()
    {
        return this.list;
    }

    public void setList(List<User> list)
    {
        this.list = list;
    }

    @Override
    public String toString()
    {
        String results = "";
        for(User user : list)
        {
            results += user.toString();
        }
        return results;
    }
}