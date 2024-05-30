package com.example.azikeamusic.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.azikeamusic.callbacks.AlbumDetailCallback;
import com.example.azikeamusic.databinding.AudioLayBinding;
import com.example.azikeamusic.roomdb.AudioFile;

import java.io.File;
import java.util.List;

public class AlbumDetailAdapter extends RecyclerView.Adapter<AlbumDetailAdapter.ViewHolder> {

    private static final String TAG  = "AlbumDetailAdapter";
    private Context context;
    private String albumArtUri;
    private List<AudioFile> albumDetailModelList;
    private AlbumDetailCallback albumDetailCallback;

    public AlbumDetailAdapter(Context context, List<AudioFile> albumDetailModelList, String albumArtUri, AlbumDetailCallback albumDetailCallback) {
        this.context = context;
        this.albumDetailModelList = albumDetailModelList;
        this.albumArtUri = albumArtUri;
        this.albumDetailCallback = albumDetailCallback;
    }

    @NonNull
    @Override
    public AlbumDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        AudioLayBinding mBinding = AudioLayBinding.inflate(inflater, parent, false);
        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumDetailAdapter.ViewHolder holder, int position) {

        AudioFile audioFile = albumDetailModelList.get(position);

        holder.mBinding.ivAddToPlayList.setVisibility(View.GONE);

        holder.mBinding.tvAudioTitle.setText(audioFile.getTitle());
        holder.mBinding.tvAudioDetail.setText(audioFile.getArtist() + " artis" + "-" + audioFile.getAlbum() + " album");

        if (albumArtUri!= null){

            File imgFile = new File(albumArtUri);
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.mBinding.ivImage.setImageBitmap(myBitmap);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                albumDetailCallback.onAlbumDetailClick(position, audioFile, albumDetailModelList);
            }
        });

    }

    @Override
    public int getItemCount() {
        return albumDetailModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AudioLayBinding mBinding;

        public ViewHolder(AudioLayBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }
}
