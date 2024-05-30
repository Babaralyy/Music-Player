package com.example.azikeamusic.roomdb;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "AddToPlayList")
public class AddToPlayList {
    @PrimaryKey
    @NonNull
    private String audioId;

    @ColumnInfo(name = "playListId")
    private String playListId;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "artist")
    private String artist;

    @ColumnInfo(name = "album")
    private String album;

    @ColumnInfo(name = "albumArt")
    private String albumArt;

    @ColumnInfo(name = "filePath")
    private String filePath;

    @ColumnInfo(name = "audioLyrics")
    private String audioLyrics;

    public AddToPlayList(@NonNull String audioId, String playListId, String title, String artist, String album, String albumArt, String filePath, String audioLyrics) {
        this.audioId = audioId;
        this.playListId = playListId;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.albumArt = albumArt;
        this.filePath = filePath;
        this.audioLyrics = audioLyrics;
    }


    @NonNull
    public String getAudioId() {
        return audioId;
    }

    public void setAudioId(@NonNull String audioId) {
        this.audioId = audioId;
    }

    public String getPlayListId() {
        return playListId;
    }

    public void setPlayListId(String playListId) {
        this.playListId = playListId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getAudioLyrics() {
        return audioLyrics;
    }

    public void setAudioLyrics(String audioLyrics) {
        this.audioLyrics = audioLyrics;
    }
}
