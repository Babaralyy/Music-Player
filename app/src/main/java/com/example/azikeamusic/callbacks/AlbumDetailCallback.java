package com.example.azikeamusic.callbacks;

import com.example.azikeamusic.roomdb.AudioFile;

import java.util.List;

public interface AlbumDetailCallback {
    void onAlbumDetailClick(int position, AudioFile audioFile, List<AudioFile> albumDetailModelList);
}
