package com.example.azikeamusic.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.azikeamusic.adapter.PlayListAdapter;
import com.example.azikeamusic.adapter.PlayListDetailAdapter;
import com.example.azikeamusic.callbacks.PassData;
import com.example.azikeamusic.callbacks.PlayAudioFromPlayList;
import com.example.azikeamusic.callbacks.PlaylistCallbacks;
import com.example.azikeamusic.databinding.FragmentPlaylistsBinding;
import com.example.azikeamusic.databinding.PlaylistDetailLayoutBinding;
import com.example.azikeamusic.roomdb.AddToPlayList;
import com.example.azikeamusic.roomdb.AppDatabase;
import com.example.azikeamusic.roomdb.AudioFile;
import com.example.azikeamusic.roomdb.PlayList;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class PlaylistsFragment extends Fragment implements PlaylistCallbacks, PlayAudioFromPlayList {
    private static final String TAG = "PlaylistsFragment";
    List<PlayList> playListList;

    PlaylistDetailLayoutBinding playDetailBinding;
    private final PassData passData;
    private BottomSheetDialog bottomSheetDialog;

    private FragmentPlaylistsBinding mBinding;

    public PlaylistsFragment(PassData passData) {
        this.passData = passData;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentPlaylistsBinding.inflate(inflater);

        inIt();

        return mBinding.getRoot();
    }

    private void inIt() {

        playListList = new ArrayList<>();

        LayoutInflater inflater = LayoutInflater.from(requireContext());
        playDetailBinding = PlaylistDetailLayoutBinding.inflate(inflater);
        bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(playDetailBinding.getRoot());
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        mBinding.rvPlayList.setHasFixedSize(true);
        mBinding.rvPlayList.setLayoutManager(linearLayoutManager);


        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(requireContext());
        playDetailBinding.rvPlayDetail.setHasFixedSize(true);
        playDetailBinding.rvPlayDetail.setLayoutManager(linearLayoutManager1);

        mBinding.btnCreatePlayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String playName = mBinding.etPlayListTitle.getText().toString().trim();
                if (!playName.isEmpty()) {

                    if (!playListList.isEmpty()) {
                        boolean found = false;

                        for (PlayList obj : playListList) {
                            if (playName.equals(obj.getPlayListName())) {
                                // Field found
                                found = true;
                                break;
                            }
                        }

                        if (found) {
                            Toast.makeText(requireContext(), "playlist already exist!", Toast.LENGTH_SHORT).show();
                        } else {
                            PlayList playList = new PlayList(playName);
                            AppDatabase.getInstance(requireContext()).audioDao().insertPlayList(playList);
                            mBinding.etPlayListTitle.setText("");
                        }
                    } else {
                        PlayList playList = new PlayList(playName);
                        AppDatabase.getInstance(requireContext()).audioDao().insertPlayList(playList);
                        mBinding.etPlayListTitle.setText("");
                    }

                } else {
                    Toast.makeText(requireContext(), "Playlist name required", Toast.LENGTH_SHORT).show();
                }
            }
        });

        getAllPlaylistsFromRoom();

    }

    private void getAllPlaylistsFromRoom() {
        AppDatabase.getInstance(requireContext()).audioDao().getAllPlayLists().observe(getViewLifecycleOwner(), new Observer<List<PlayList>>() {
            @Override
            public void onChanged(List<PlayList> playLists) {
                if (playLists.isEmpty()){
                    mBinding.tvNotFound.setVisibility(View.VISIBLE);
                    setRecyclerView(playLists);
                } else {
                    mBinding.tvNotFound.setVisibility(View.GONE);
                    playListList = playLists;
                    setRecyclerView(playLists);
                }

            }
        });
    }

    private void setRecyclerView(List<PlayList> playLists) {
        PlayListAdapter playListAdapter = new PlayListAdapter(requireContext(), playLists, this);
        mBinding.rvPlayList.setAdapter(playListAdapter);
    }

    @Override
    public void onPlayClick(int position, PlayList playList) {

        Log.i(TAG, "onPlayClick:: ");

        AppDatabase.getInstance(requireContext()).audioDao().getSongsFromPlayList(playList.getId()).observe(getViewLifecycleOwner(), new Observer<List<AddToPlayList>>() {
            @Override
            public void onChanged(List<AddToPlayList> addToPlayLists) {
                if (addToPlayLists.isEmpty()){
                    playDetailBinding.tvNotFound.setVisibility(View.VISIBLE);
                    setPlayDetailRecyclerView(addToPlayLists);
                } else {
                    playDetailBinding.tvNotFound.setVisibility(View.GONE);
                    setPlayDetailRecyclerView(addToPlayLists);
                }

            }
        });
        bottomSheetDialog.show();
    }

    @Override
    public void onDeleteClick(int position, PlayList playList) {
        AppDatabase.getInstance(requireContext()).audioDao().deleteAllPlaylistSongs(String.valueOf(playList.getId()));
        AppDatabase.getInstance(requireContext()).audioDao().deletePlaylist(playList.getId());
        Toast.makeText(requireContext(), playList.getPlayListName() + " deleted", Toast.LENGTH_SHORT).show();
        getAllPlaylistsFromRoom();
    }

    private void setPlayDetailRecyclerView(List<AddToPlayList> addToPlayLists) {
        PlayListDetailAdapter playListDetailAdapter = new PlayListDetailAdapter(requireContext(), addToPlayLists, this);
        playDetailBinding.rvPlayDetail.setAdapter(playListDetailAdapter);
    }

    @Override
    public void onPlayFromPlayListClick(int position, AddToPlayList addToPlayList) {
        AudioFile audioFile = new AudioFile(addToPlayList.getAudioId(), addToPlayList.getTitle(),
                addToPlayList.getArtist(),
                addToPlayList.getAlbum(), "", addToPlayList.getAlbumArt(),
                addToPlayList.getFilePath(), addToPlayList.getAudioLyrics());
        passData.audioData(position, audioFile);

        bottomSheetDialog.dismiss();

    }

    @Override
    public void onDeleteFromPlayListClick(int position, AddToPlayList addToPlayList) {
        AppDatabase.getInstance(requireContext()).audioDao().deleteOnePlaylistSongs(addToPlayList.getAudioId(), addToPlayList.getPlayListId());
        Toast.makeText(requireContext(), "deleted", Toast.LENGTH_SHORT).show();
//        bottomSheetDialog.dismiss();
    }
}