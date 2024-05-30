package com.example.azikeamusic.callbacks;

import com.example.azikeamusic.databinding.LayDownloadBinding;

public interface DownloadCallback {
    void onDownloadClick(int position, String url, LayDownloadBinding mBinding);
}
