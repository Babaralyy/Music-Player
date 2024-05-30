package com.example.azikeamusic.callbacks;

import com.example.azikeamusic.roomdb.AddToPlayList;

public interface PlayAudioFromPlayList {
    void onPlayFromPlayListClick(int position, AddToPlayList addToPlayList);
    void onDeleteFromPlayListClick(int position, AddToPlayList addToPlayList);
}
