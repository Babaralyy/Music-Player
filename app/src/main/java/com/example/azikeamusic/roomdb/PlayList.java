package com.example.azikeamusic.roomdb;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "PlayList")
public class PlayList {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id = 0;

    @ColumnInfo(name = "playListName")
    private String playListName;

    public PlayList(String playListName) {
        this.playListName = playListName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlayListName() {
        return playListName;
    }

    public void setPlayListName(String playListName) {
        this.playListName = playListName;
    }
}
