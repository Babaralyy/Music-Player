package com.example.azikeamusic.callbacks;

import com.example.azikeamusic.databinding.AudioLayBinding;
import com.example.azikeamusic.roomdb.AudioFile;

public interface AudioCallback {
    void onAudioClick(int position, AudioFile audioFile);
    void addToPlayList(int position, AudioFile audioFile, AudioLayBinding mBinding);
}
