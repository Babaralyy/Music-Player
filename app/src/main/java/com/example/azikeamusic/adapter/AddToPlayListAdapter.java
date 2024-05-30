package com.example.azikeamusic.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.azikeamusic.callbacks.AddToPlaylistCallback;
import com.example.azikeamusic.callbacks.PlaylistCallbacks;
import com.example.azikeamusic.databinding.AddToPlaylistLayoutBinding;
import com.example.azikeamusic.databinding.PlaylistLayoutBinding;
import com.example.azikeamusic.roomdb.PlayList;

import java.util.List;

public class AddToPlayListAdapter extends RecyclerView.Adapter<AddToPlayListAdapter.ViewHolder> {

    private static final String TAG = "PlayListAdapter";
    private Context context;
    private List<PlayList> playLists;
    private AddToPlaylistCallback addToPlaylistCallback;

    public AddToPlayListAdapter(Context context, List<PlayList> playLists, AddToPlaylistCallback addToPlaylistCallback) {
        this.context = context;
        this.playLists = playLists;
        this.addToPlaylistCallback = addToPlaylistCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        AddToPlaylistLayoutBinding mBinding = AddToPlaylistLayoutBinding.inflate(inflater, parent, false);
        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlayList playList = playLists.get(position);

        holder.mBinding.ivDetail.setVisibility(View.GONE);

        holder.mBinding.tvPlayListTitle.setText(playList.getPlayListName());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToPlaylistCallback.onAddIntoPlayClick(position, playList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return playLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AddToPlaylistLayoutBinding mBinding;

        public ViewHolder(AddToPlaylistLayoutBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }
}

