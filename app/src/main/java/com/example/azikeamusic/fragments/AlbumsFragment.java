package com.example.azikeamusic.fragments;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.azikeamusic.adapter.AlbumAdapter;
import com.example.azikeamusic.adapter.AlbumDetailAdapter;
import com.example.azikeamusic.callbacks.AlbumCallback;
import com.example.azikeamusic.callbacks.AlbumDetailCallback;
import com.example.azikeamusic.callbacks.PassData;
import com.example.azikeamusic.databinding.AlbumDetailDialogLayBinding;
import com.example.azikeamusic.databinding.FragmentAlbumsBinding;
import com.example.azikeamusic.datamodels.Album;
import com.example.azikeamusic.datamodels.AlbumDetailModel;
import com.example.azikeamusic.roomdb.AppDatabase;
import com.example.azikeamusic.roomdb.AudioFile;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class AlbumsFragment extends Fragment implements AlbumCallback, AlbumDetailCallback {

    private static final String TAG = "AlbumsFragment";
    private BottomSheetDialog bottomSheetDialog;
    private PassData passData;
    AlbumDetailDialogLayBinding detailBinding;
    private FragmentAlbumsBinding mBinding;

    public AlbumsFragment(PassData passData) {
        this.passData = passData;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentAlbumsBinding.inflate(inflater);

        inIt();

        return mBinding.getRoot();
    }

    private void inIt() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        mBinding.rvAlbum.setHasFixedSize(true);
        mBinding.rvAlbum.setLayoutManager(linearLayoutManager);


        LayoutInflater inflater = LayoutInflater.from(requireContext());
        detailBinding = AlbumDetailDialogLayBinding.inflate(inflater);
        bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(detailBinding.getRoot());
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        getAlbums();
    }

    private void getAlbums() {
        ContentResolver contentResolver = requireContext().getContentResolver();

        String[] projection = new String[]{
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.ALBUM_ART,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS
        };


        Cursor cursor = contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                projection, null, null, null);


        List<Album> albums = new ArrayList<>();

        if (cursor != null) {

            long idColumn = cursor.getColumnIndex(MediaStore.Audio.Albums._ID);
            int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
            int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST);
            int albumArtUriColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
            int numberOfSongsColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS);


            while (cursor.moveToNext()) {
                long id = cursor.getLong((int) idColumn);
                String title = cursor.getString(titleColumn);
                String artist = cursor.getString(artistColumn);
                String albumArtUri = cursor.getString(albumArtUriColumn);
                int numberOfSongs = cursor.getInt(numberOfSongsColumn);

                Album album = new Album(id, title, artist, numberOfSongs, albumArtUri);
                albums.add(album);
            }

            cursor.close();
        }

        Log.i(TAG, "getAlbums: " + albums.size());


        if (!albums.isEmpty()){
            AlbumAdapter albumAdapter = new AlbumAdapter(requireContext(), albums, this);
            mBinding.rvAlbum.setAdapter(albumAdapter);
            mBinding.tvNotFound.setVisibility(View.GONE);
        } else {
            mBinding.tvNotFound.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onAlbumClick(int position, Album albumFile) {

        AppDatabase.getInstance(requireContext()).audioDao().getAlbumAudioFiles(albumFile.getId()).observe(getViewLifecycleOwner(), new Observer<List<AudioFile>>() {
            @Override
            public void onChanged(List<AudioFile> audioFiles) {
                if (!audioFiles.isEmpty()){
                    showBottomDetailDialog(audioFiles, albumFile.getAlbumArtUri());
                    detailBinding.tvNotFound.setVisibility(View.GONE);
                } else {
                    detailBinding.tvNotFound.setVisibility(View.VISIBLE);
                }
            }
        });



    }

    private void showBottomDetailDialog(List<AudioFile> albumSongsList, String albumArtUri) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        detailBinding.rvAlbumDetail.setHasFixedSize(true);
        detailBinding.rvAlbumDetail.setLayoutManager(linearLayoutManager);

        if (albumSongsList != null && !albumSongsList.isEmpty()){
            AlbumDetailAdapter albumDetailAdapter = new AlbumDetailAdapter(requireContext(), albumSongsList, albumArtUri,this);
            detailBinding.rvAlbumDetail.setAdapter(albumDetailAdapter);
        }

        bottomSheetDialog.show();

    }

    @Override
    public void onAlbumDetailClick(int position, AudioFile audioFile, List<AudioFile> albumDetailModelList) {
        passData.audioData(position, audioFile);
        bottomSheetDialog.dismiss();
    }
}
