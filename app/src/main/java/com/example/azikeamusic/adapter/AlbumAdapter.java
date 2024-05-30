package com.example.azikeamusic.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.azikeamusic.callbacks.AlbumCallback;
import com.example.azikeamusic.databinding.AudioLayBinding;
import com.example.azikeamusic.datamodels.Album;

import java.io.File;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    private static final String TAG = "AlbumAdapter";
    private final Context context;
    private final List<Album >albumList;
    private AlbumCallback albumCallback;

    public AlbumAdapter(Context context, List<Album> albumList, AlbumCallback albumCallback) {
        this.context = context;
        this.albumList = albumList;
        this.albumCallback = albumCallback;
    }

    @NonNull
    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        AudioLayBinding mBinding = AudioLayBinding.inflate(inflater, parent, false);
        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Album albumFile = albumList.get(position);

        holder.mBinding.ivAddToPlayList.setVisibility(View.GONE);

        holder.mBinding.tvAudioTitle.setText(albumFile.getTitle());
        holder.mBinding.tvAudioDetail.setVisibility(View.GONE);

        if (albumFile.getAlbumArtUri() != null){

            File imgFile = new File(albumFile.getAlbumArtUri());
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.mBinding.ivImage.setImageBitmap(myBitmap);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                albumCallback.onAlbumClick(position, albumFile);
            }
        });

    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final AudioLayBinding mBinding;

        public ViewHolder(AudioLayBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }
}
