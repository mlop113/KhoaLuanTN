package com.android.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */

public class Tag implements Serializable
{
    @SerializedName("TagID")
    private String tagID;
    @SerializedName("Name")
    private String name;

    public Tag(String tagID, String name) {
        this.tagID = tagID;
        this.name = name;
    }

    public String getTagID() {
        return tagID;
    }

    public void setTagID(String tagID) {
        this.tagID = tagID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
