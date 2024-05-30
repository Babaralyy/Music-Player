package com.example.azikeamusic.callbacks;

import com.example.azikeamusic.roomdb.PlayList;

public interface AddToPlaylistCallback {
    void onAddIntoPlayClick(int position, PlayList playList);
}
