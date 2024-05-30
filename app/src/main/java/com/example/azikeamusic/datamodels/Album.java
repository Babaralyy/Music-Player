package com.example.azikeamusic.datamodels;

public class Album {
    private long id;
    private String title;
    private String artist;
    private int numberOfSongs;
    private String albumArtUri;

    public Album(long id, String title, String artist, int numberOfSongs, String albumArtUri) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.numberOfSongs = numberOfSongs;
        this.albumArtUri = albumArtUri;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public int getNumberOfSongs() {
        return numberOfSongs;
    }

    public String getAlbumArtUri() {
        return albumArtUri;
    }
}

