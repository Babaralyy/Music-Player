package com.example.azikeamusic.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.azikeamusic.callbacks.PlaylistCallbacks;
import com.example.azikeamusic.databinding.PlaylistLayoutBinding;
import com.example.azikeamusic.roomdb.PlayList;
import java.util.List;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.ViewHolder> {

    private static final String TAG = "PlayListAdapter";
    private Context context;
    private List<PlayList> playLists;
    private PlaylistCallbacks playlistCallbacks;

    public PlayListAdapter(Context context, List<PlayList> playLists, PlaylistCallbacks playlistCallbacks) {
        this.context = context;
        this.playLists = playLists;
        this.playlistCallbacks = playlistCallbacks;
    }

    @NonNull
    @Override
    public PlayListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        PlaylistLayoutBinding mBinding = PlaylistLayoutBinding.inflate(inflater, parent, false);
        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        PlayList playList = playLists.get(position);
        holder.mBinding.tvPlayListTitle.setText(playList.getPlayListName());

        holder.mBinding.tvPlayListTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: onPlayClick");
                playlistCallbacks.onPlayClick(position, playList);
            }
        });

        holder.mBinding.ivDeletePlayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playlistCallbacks.onDeleteClick(position, playList);
            }
        });

    }

    @Override
    public int getItemCount() {
        return playLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final PlaylistLayoutBinding mBinding;

        public ViewHolder(PlaylistLayoutBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }
}
