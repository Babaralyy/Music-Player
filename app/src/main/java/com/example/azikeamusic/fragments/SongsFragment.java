package com.example.azikeamusic.fragments;

import android.Manifest;
import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.azikeamusic.R;
import com.example.azikeamusic.adapter.AddToPlayListAdapter;
import com.example.azikeamusic.adapter.SongAdapter;
import com.example.azikeamusic.callbacks.AddToPlaylistCallback;
import com.example.azikeamusic.callbacks.AudioCallback;
import com.example.azikeamusic.callbacks.PassData;
import com.example.azikeamusic.callbacks.PlaylistCallbacks;
import com.example.azikeamusic.databinding.AudioLayBinding;
import com.example.azikeamusic.databinding.DialogLyricsLayoutBinding;
import com.example.azikeamusic.databinding.FragmentSongsBinding;
import com.example.azikeamusic.databinding.PlaylistBottomLayoutBinding;
import com.example.azikeamusic.roomdb.AddToPlayList;
import com.example.azikeamusic.roomdb.AppDatabase;
import com.example.azikeamusic.roomdb.AudioFile;
import com.example.azikeamusic.roomdb.PlayList;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;


public class SongsFragment extends Fragment implements AudioCallback, PlaylistCallbacks, AddToPlaylistCallback {

    private static final String TAG = "SongsFragment";
    private final PassData passData;
    private AudioFile audioFile = null;

    PlaylistBottomLayoutBinding playBinding;
    private BottomSheetDialog bottomSheetDialog;

    private FragmentSongsBinding mBinding;

    public SongsFragment(PassData passData) {
        this.passData = passData;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentSongsBinding.inflate(inflater);

        inIt();

        return mBinding.getRoot();
    }

    private void inIt() {


        LayoutInflater inflater = LayoutInflater.from(requireContext());
        playBinding = PlaylistBottomLayoutBinding.inflate(inflater);
        bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(playBinding.getRoot());
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        mBinding.rvAudio.setHasFixedSize(true);
        mBinding.rvAudio.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(requireContext());
        playBinding.rvPlay.setHasFixedSize(true);
        playBinding.rvPlay.setLayoutManager(linearLayoutManager1);


        getStoragePermission();

        getAllAudioFromRoom();


    }

    private void getStoragePermission() {

        Dexter.withContext(requireContext())
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {

                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        List<AudioFile> audioFiles = getAudioFiles();
                        insertAudioIntoRoom(audioFiles);

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        Log.i(TAG, "onPermissionRationaleShouldBeShown: canceled");
                        getStoragePermission();
                    }
                }).check();
    }


    private void insertAudioIntoRoom(List<AudioFile> audioFiles) {
        for (AudioFile file : audioFiles) {
            AppDatabase.getInstance(requireContext()).audioDao().insertTrack(file);
        }
    }

    private void getAllAudioFromRoom() {
        AppDatabase.getInstance(requireContext()).audioDao().getAllAudioFiles().observe(getViewLifecycleOwner(), new Observer<List<AudioFile>>() {
            @Override
            public void onChanged(List<AudioFile> audioFiles) {
                if (!audioFiles.isEmpty()) {
                    setRecyclerView(audioFiles);
                }
            }
        });
    }

    private void setRecyclerView(List<AudioFile> audioFiles) {
        SongAdapter audioAdapter = new SongAdapter(requireContext(), audioFiles, this);
        mBinding.rvAudio.setAdapter(audioAdapter);
    }

    public List<AudioFile> getAudioFiles() {
        List<AudioFile> audioList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.ALBUM_ARTIST,
                MediaStore.Audio.Media.DATA,

        };
        Cursor cursor = requireContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
            int titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
            int artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
            int albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
            int albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID);
            int albumArtisColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ARTIST);
            int mPath = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);


            while (cursor.moveToNext()) {
                String id = cursor.getString(idColumn);
                String title = cursor.getString(titleColumn);
                String artist = cursor.getString(artistColumn);
                String album = cursor.getString(albumColumn);
                String albumId = cursor.getString(albumIdColumn);
                String albumArtist = cursor.getString(albumArtisColumn);
                String filePath = cursor.getString(mPath);

                Log.i(TAG, "getAudioFiles: path " + filePath);

                if (filePath.endsWith(".mp3") || filePath.endsWith(".aac") || filePath.endsWith(".ogg") || filePath.endsWith(".midi")) {
                    AudioFile audioFile = new AudioFile(id, title, artist, album, albumId, albumArtist, filePath, null);
                    audioList.add(audioFile);
                }
            }
            cursor.close();
        }
        return audioList;
    }


    @Override
    public void onAudioClick(int position, AudioFile audioFile) {

        passData.audioData(position, audioFile);
    }

    @Override
    public void addToPlayList(int position, AudioFile audioFile, AudioLayBinding mBinding) {
        showPopup(mBinding.ivAddToPlayList, audioFile);
    }

    private void showPlayDialog(AudioFile audioFile) {

        this.audioFile = audioFile;

        getAllPlaylistsFromRoom();

        bottomSheetDialog.show();
    }


    private void getAllPlaylistsFromRoom() {
        AppDatabase.getInstance(requireContext()).audioDao().getAllPlayLists().observe(getViewLifecycleOwner(), new Observer<List<PlayList>>() {
            @Override
            public void onChanged(List<PlayList> playLists) {
                if (!playLists.isEmpty()) {
                    playBinding.tvNotFound.setVisibility(View.GONE);
                    setBottomRecyclerView(playLists);
                } else {
                    playBinding.tvNotFound.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setBottomRecyclerView(List<PlayList> playLists) {

        AddToPlayListAdapter addToPlayListAdapter = new AddToPlayListAdapter(requireContext(), playLists, this);
        playBinding.rvPlay.setAdapter(addToPlayListAdapter);
    }

    @Override
    public void onPlayClick(int position, PlayList playList) {

    }

    @Override
    public void onDeleteClick(int position, PlayList playList) {

    }

    @Override
    public void onAddIntoPlayClick(int position, PlayList playList) {
        if (this.audioFile != null) {
            AddToPlayList addToPlayList = new AddToPlayList(this.audioFile.getId(), playList.getId() + "", this.audioFile.getTitle(), this.audioFile.getArtist(), this.audioFile.getAlbum(), this.audioFile.getAlbumArt(), this.audioFile.getFilePath(), this.audioFile.getAudioLyrics());
            try {
                AppDatabase.getInstance(requireContext()).audioDao().insertIntoPlayList(addToPlayList);
                Toast.makeText(requireContext(), "Added to PlayList", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Already Added", Toast.LENGTH_SHORT).show();
            }

            bottomSheetDialog.dismiss();
        }
    }


    private void showPopup(View v, AudioFile audioFile) {
        PopupMenu popup = new PopupMenu(requireContext(), v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.iAddToPlayList) {
                    showPlayDialog(audioFile);
                    return true;
                } else if (item.getItemId() == R.id.iLyrics) {
                    showLyricsDialog(audioFile);
                    return true;
                } else {
                    return false;
                }
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.option_menu, popup.getMenu());
        popup.show();

    }

    private void showLyricsDialog(AudioFile audioFile) {

        LayoutInflater inflater = LayoutInflater.from(requireContext());
        Dialog lyricsDialog = new Dialog(requireContext());
        DialogLyricsLayoutBinding lyricsBinding = DialogLyricsLayoutBinding.inflate(inflater);
        lyricsDialog.setContentView(lyricsBinding.getRoot());
        lyricsDialog.setCancelable(false);
        lyricsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        lyricsBinding.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lyrics = lyricsBinding.etLyrics.getText().toString().trim();
                if (lyrics.isEmpty()){
                    Toast.makeText(requireContext(), "Can't add empty lyrics!", Toast.LENGTH_SHORT).show();
                }else {
                    AppDatabase.getInstance(requireContext()).audioDao().updateLyrics(lyrics, audioFile.getId());
                    Toast.makeText(requireContext(), "Lyrics Added", Toast.LENGTH_SHORT).show();
                    lyricsDialog.dismiss();
                }
            }
        });

        lyricsBinding.ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lyricsDialog.dismiss();
            }
        });


        lyricsDialog.show();

    }

}