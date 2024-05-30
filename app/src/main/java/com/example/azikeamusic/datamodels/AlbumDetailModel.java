package com.example.azikeamusic.datamodels;

import java.io.File;

public class AlbumDetailModel {

    File songPath;
    String songTitle;
    String songArti;
    String songAlbum;
    String songAlbumArtUri;

    public AlbumDetailModel(File songPath, String songTitle, String songArti, String songAlbum, String songAlbumArtUri) {
        this.songPath = songPath;
        this.songTitle = songTitle;
        this.songArti = songArti;
        this.songAlbum = songAlbum;
        this.songAlbumArtUri = songAlbumArtUri;
    }

    public File getSongPath() {
        return songPath;
    }

    public void setSongPath(File songPath) {
        this.songPath = songPath;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongArti() {
        return songArti;
    }

    public void setSongArti(String songArti) {
        this.songArti = songArti;
    }

    public String getSongAlbum() {
        return songAlbum;
    }

    public void setSongAlbum(String songAlbum) {
        this.songAlbum = songAlbum;
    }

    public String getSongAlbumArtUri() {
        return songAlbumArtUri;
    }

    public void setSongAlbumArtUri(String songAlbumArtUri) {
        this.songAlbumArtUri = songAlbumArtUri;
    }
}
