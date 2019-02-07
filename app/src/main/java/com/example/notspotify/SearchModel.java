package com.example.notspotify;

import ir.mirrajabi.searchdialog.core.Searchable;

public class SearchModel implements Searchable {
    private String mName;
    private String mID;
    private String mTitle;

    public SearchModel(String artistName, String songID, String songTitle) {
        this.mName = artistName;
        this.mID = songID;
        this.mTitle = songTitle;
    }

    public String getName() {
        return mName;
    }

    public String getID() {
        return mID;
    }

    @Override
    public String getTitle() {
        return mName + " - " + mTitle;
    }

    public SearchModel setName(String Name) {
        mName = Name;
        return this;
    }

    public SearchModel setID(String ID) {
        mID = ID;
        return this;

    }

    public SearchModel setTitle(String Title) {
        mTitle = Title;
        return this;

    }

    @Override
    public String toString() {
        return mName + " - " + mTitle;
    }
}
