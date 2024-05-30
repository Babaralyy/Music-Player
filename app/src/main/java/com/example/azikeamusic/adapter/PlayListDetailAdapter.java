package com.example.azikeamusic.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.animation.content.Content;
import com.example.azikeamusic.callbacks.PlayAudioFromPlayList;
import com.example.azikeamusic.databinding.AudioLayBinding;
import com.example.azikeamusic.databinding.PlaylistDetailLayBinding;
import com.example.azikeamusic.roomdb.AddToPlayList;
import com.example.azikeamusic.roomdb.AudioFile;

import java.util.List;

public class PlayListDetailAdapter extends RecyclerView.Adapter<PlayListDetailAdapter.ViewHolder> {
    private Context context;
    private List<AddToPlayList> songsPlayList;
    private PlayAudioFromPlayList playAudioFromPlayList;

    public PlayListDetailAdapter(Context context, List<AddToPlayList> songsPlayList, PlayAudioFromPlayList playAudioFromPlayList) {
        this.context = context;
        this.songsPlayList = songsPlayList;
        this.playAudioFromPlayList = playAudioFromPlayList;
    }

    @NonNull
    @Override
    public PlayListDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        PlaylistDetailLayBinding mBinding = PlaylistDetailLayBinding.inflate(inflater, parent, false);


        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayListDetailAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        AddToPlayList audioFile = songsPlayList.get(position);

        holder.mBinding.tvAudioTitle.setText(audioFile.getTitle());
        holder.mBinding.tvAudioDetail.setText(audioFile.getArtist() + " artis" + "-" + audioFile.getAlbum()+ " album");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playAudioFromPlayList.onPlayFromPlayListClick(position, audioFile);
            }
        });

        holder.mBinding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playAudioFromPlayList.onDeleteFromPlayListClick(position, audioFile);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songsPlayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final PlaylistDetailLayBinding mBinding;

        public ViewHolder(PlaylistDetailLayBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }
}
