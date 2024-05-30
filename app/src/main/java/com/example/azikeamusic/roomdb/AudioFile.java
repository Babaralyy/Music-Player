package com.example.azikeamusic.roomdb;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.File;

@Entity(tableName = "AudioFile")
public class AudioFile {
    @PrimaryKey
    @NonNull
    private String id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "artist")
    private String artist;

    @ColumnInfo(name = "album")
    private String album;

    @ColumnInfo(name = "albumId")
    private String albumId;

    @ColumnInfo(name = "albumArt")
    private String albumArt;

    @ColumnInfo(name = "filePath")
    private String filePath;

    @ColumnInfo(name = "audioLyrics")
    private String audioLyrics;



    public AudioFile(@NonNull String id, String title, String artist, String album, String albumId, String albumArt, String filePath, String audioLyrics) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.albumId = albumId;
        this.albumArt = albumArt;
        this.filePath = filePath;
        this.audioLyrics = audioLyrics;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
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
