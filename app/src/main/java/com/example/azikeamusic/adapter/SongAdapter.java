package com.example.azikeamusic.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.azikeamusic.callbacks.AudioCallback;
import com.example.azikeamusic.databinding.AudioLayBinding;
import com.example.azikeamusic.roomdb.AudioFile;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    private static final String TAG  = "SongAdapter";
    private Context context;
    private List<AudioFile> audioList;
    private AudioCallback audioCallback;

    public SongAdapter(Context context, List<AudioFile> audioList, AudioCallback audioCallback) {
        this.context = context;
        this.audioList = audioList;
        this.audioCallback = audioCallback;
    }

    @NonNull
    @Override
    public SongAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        AudioLayBinding mBinding = AudioLayBinding.inflate(inflater, parent, false);
        return new ViewHolder(mBinding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SongAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        AudioFile audioFile = audioList.get(position);
        holder.mBinding.tvAudioTitle.setText(audioFile.getTitle());
        holder.mBinding.tvAudioDetail.setText(audioFile.getArtist() + " artis" + "-" + audioFile.getAlbum()+ " album");


        holder.mBinding.songLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audioCallback.onAudioClick(position, audioFile);
            }
        });

        holder.mBinding.ivAddToPlayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audioCallback.addToPlayList(position, audioFile, holder.mBinding);
            }
        });

    }

    @Override
    public int getItemCount() {
        return audioList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final AudioLayBinding mBinding;

        public ViewHolder(AudioLayBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }
}
