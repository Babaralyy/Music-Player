package com.example.azikeamusic.callbacks;

import com.example.azikeamusic.datamodels.Album;
import com.example.azikeamusic.roomdb.PlayList;

public interface PlaylistCallbacks {
    void onPlayClick(int position, PlayList playList);
    void onDeleteClick(int position, PlayList playList);

}
