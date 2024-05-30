package com.example.azikeamusic.datamodels;

import java.io.File;

public class AudioFile {
    private String title;
    private String artist;
    private String album;
    private String albumArt;
    private File filePath;



    public AudioFile(String title, String artist, String album, String albumArt, File filePath) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.albumArt = albumArt;
        this.filePath = filePath;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }

    public File getFilePath() {
        return filePath;
    }

    public void setFilePath(File filePath) {
        this.filePath = filePath;
    }
}
