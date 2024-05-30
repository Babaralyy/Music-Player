package com.example.azikeamusic.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.azikeamusic.R;
import com.example.azikeamusic.adapter.DownloadAdapter;
import com.example.azikeamusic.adapter.ViewPagerAdapter;
import com.example.azikeamusic.callbacks.DownloadCallback;
import com.example.azikeamusic.callbacks.PassData;
import com.example.azikeamusic.databinding.DownloadBottomLayBinding;
import com.example.azikeamusic.databinding.FragmentAudioBinding;
import com.example.azikeamusic.databinding.LayDownloadBinding;
import com.example.azikeamusic.databinding.PlayerLayoutBinding;
import com.example.azikeamusic.roomdb.AppDatabase;
import com.example.azikeamusic.roomdb.AudioFile;
import com.example.azikeamusic.service.AudioService;
import com.example.azikeamusic.utils.Constant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AudioFragment extends Fragment implements PassData, DownloadCallback {

    private static final String TAG = "AudioFragment";
    private List<String> urlList;
    private File fileData;
    PlayerLayoutBinding binding;
    private BottomSheetDialog bottomSheetDialog;
    private String audioTitle;
    private String audioArtist;
    private String audioAlbum;

    private AudioFile mFile = null;

    public static AudioService mService = null;

    private FragmentAudioBinding mBinding;

    public AudioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentAudioBinding.inflate(inflater);

        inIt();

        Log.i(TAG, "onCreateView: ");

        return mBinding.getRoot();
    }

    private void inIt() {

        urlList = new ArrayList<>();

        LayoutInflater inflater = LayoutInflater.from(requireContext());
        binding = PlayerLayoutBinding.inflate(inflater);

        bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(binding.getRoot());
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mBinding.bottomSeekBar.setEnabled(false);
        setUpViewPager();

        mBinding.playerLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AudioService.mediaPlayer != null) {
                    showBottomSheetDialog();
                } else {
                    Toast.makeText(requireContext(), "Select song first!", Toast.LENGTH_SHORT).show();
                }

            }
        });


        mBinding.ivPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (AudioService.mediaPlayer != null) {


                    if (!(AudioService.mediaPlayer.isPlaying())) {
                        mBinding.ivPlayPause.setImageResource(R.drawable.pause_button);
                        AudioService.mediaPlayer.start();
                        setUpSeekBar();

                    } else {
                        mBinding.ivPlayPause.setImageResource(R.drawable.play_button);
                        AudioService.mediaPlayer.pause();
                    }
                }

            }
        });

        binding.playerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    AudioService.mediaPlayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        mBinding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDownloadDialog();
            }
        });



    }

    private void showBottomDownloadDialog() {

        LayoutInflater inflater = LayoutInflater.from(requireContext());
        DownloadBottomLayBinding downBinding = DownloadBottomLayBinding.inflate(inflater);
        BottomSheetDialog bottomDownSheetDialog = new BottomSheetDialog(requireContext());
        bottomDownSheetDialog.setContentView(downBinding.getRoot());
        bottomDownSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LinearLayoutManager downLayManager = new LinearLayoutManager(requireContext());
        downBinding.rvDownload.setHasFixedSize(true);
        downBinding.rvDownload.setLayoutManager(downLayManager);

        downloadAudios(downBinding);


        bottomDownSheetDialog.show();
    }

    private void setUpViewPager() {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(requireActivity().getSupportFragmentManager());
        pagerAdapter.addFragment(new SongsFragment(this), "Songs");
        pagerAdapter.addFragment(new PlaylistsFragment(this), "Playlists");
        pagerAdapter.addFragment(new AlbumsFragment(this), "Albums");
        mBinding.viewPager.setAdapter(pagerAdapter);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
    }

    private void showBottomSheetDialog() {

        binding.tvAudioTitle.setText(audioTitle);
        binding.tvAudioDetail.setText(audioArtist + " artist" + "-" + audioAlbum + " album");


        binding.tvLyrics.setMovementMethod(new ScrollingMovementMethod());
        binding.tvLyrics.setText("");

        if (mFile != null){
            AppDatabase.getInstance(requireContext()).audioDao().getLyrics(mFile.getId()).observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    if (s == null){
                        binding.tvLyricsNotFound.setVisibility(View.VISIBLE);
                    } else {
                        binding.tvLyricsNotFound.setVisibility(View.GONE);
                        binding.tvLyrics.setText(s);
                    }
                }
            });
        }


        Constant.songTitle = audioTitle;
        Constant.songDetail = audioArtist + " artis" + "-" + audioAlbum + " album";


        if (AudioService.mediaPlayer.isPlaying()) {
            binding.ivCircledPlayPause.setImageResource(R.drawable.circuled_pause);
        } else {
            binding.ivCircledPlayPause.setImageResource(R.drawable.circled_play);
        }

        binding.ivCircledPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AudioService.mediaPlayer != null) {
                    if (!(AudioService.mediaPlayer.isPlaying())) {
                        binding.ivCircledPlayPause.setImageResource(R.drawable.circuled_pause);
                        mBinding.ivPlayPause.setImageResource(R.drawable.pause_button);
                        AudioService.mediaPlayer.start();
                        setUpSeekBar();

                    } else {

                        AudioService.mediaPlayer.pause();
                        binding.ivCircledPlayPause.setImageResource(R.drawable.circled_play);
                        mBinding.ivPlayPause.setImageResource(R.drawable.play_button);
                    }
                }
            }
        });


        bottomSheetDialog.show();
    }

    private void startMusicService() {
        // Create an instance of your service
        mService = new AudioService();

        // Start the service as a foreground service
        Intent serviceIntent = new Intent(getActivity(), AudioService.class);
        serviceIntent.setAction(Constant.START_FOREGROUND_ACTION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireActivity().startForegroundService(serviceIntent);
        }
    }


    private void playAudio() {
        Log.i(TAG, "playAudio: playAudio");
        AudioService.mediaPlayer = new MediaPlayer();
        try {
            AudioService.mediaPlayer.setDataSource(fileData.toString());

            Log.i(TAG, "inIt:: pauseTime prePareAudio");

        } catch (IOException ioException) {
            ioException.printStackTrace();
            Log.i("uri", "IOException:");
        }

        prePareAudio();

        AudioService.mediaPlayer.setOnPreparedListener(mediaPlayer -> {

            binding.tvProgress.setText(Constant.getTrackTotalTime(((long) mediaPlayer.getCurrentPosition())));
            binding.tvTotalDuration.setText(Constant.getTrackTotalTime(((long) mediaPlayer.getDuration())));
            mBinding.bottomSeekBar.setProgress(0);
            binding.playerSeekBar.setProgress(0);
            mBinding.bottomSeekBar.setMax(mediaPlayer.getDuration());
            binding.playerSeekBar.setMax(mediaPlayer.getDuration());


            Log.i(TAG, "playAudio: setOnPreparedListener");
            startMusicService();
            mediaPlayer.start();
            mBinding.ivPlayPause.setImageResource(R.drawable.pause_button);
            setUpSeekBar();
        });


        AudioService.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mBinding.ivPlayPause.setImageResource(R.drawable.play_button);
                binding.ivCircledPlayPause.setImageResource(R.drawable.circled_play);
            }
        });

    }

    private void setUpSeekBar() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (AudioService.mediaPlayer != null) {
                    int currentPosition = AudioService.mediaPlayer.getCurrentPosition();
                    binding.tvProgress.setText(Constant.getTrackTotalTime((long) currentPosition));
                    mBinding.bottomSeekBar.setProgress(currentPosition);
                    binding.playerSeekBar.setProgress(currentPosition);
                }
                handler.postDelayed(this, 50);
            }
        };
        handler.postDelayed(runnable, 0);
    }

    private void prePareAudio() {
        Log.i(TAG, "prePareAudio:");

        try {
            AudioService.mediaPlayer.prepareAsync();
        } catch (Exception e) {
            Log.i(TAG, "playAudio:catch " + e);
        }
    }

    @Override
    public void audioData(int position, AudioFile audioFile) {
        Log.i(TAG, "audioData: audioData " + audioFile.getTitle());
        Log.i(TAG, "audioData: audioData file " + audioFile.getFilePath());

        mFile = audioFile;
        fileData = new File(audioFile.getFilePath());
        audioTitle = audioFile.getTitle();
        audioArtist = audioFile.getArtist();
        audioAlbum = audioFile.getAlbum();

        Constant.songTitle = audioTitle;
        Constant.songDetail = audioArtist + " artist";
        Constant.songLyrics = audioFile.getAudioLyrics();

        mBinding.tvAudioTitle.setText(audioTitle);

        if (AudioService.mediaPlayer != null) {

            if (AudioService.mediaPlayer.isPlaying()) {
                AudioService.mediaPlayer.stop();
            }
            AudioService.mediaPlayer.reset();
            AudioService.mediaPlayer.release();
            AudioService.mediaPlayer = null;
        }

        playAudio();
    }



    private void downloadAudios(DownloadBottomLayBinding downBinding) {

       Dialog downDialog = Constant.getDialog(requireContext());
        downDialog.show();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("music");

        urlList.clear();

        for (int i = 1; i <= 5; i++) {
            StorageReference fileRef = storageRef.child("audio_" + i + ".mp3");
            int finalI = i;
            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    downDialog.dismiss();
                    Log.i(TAG, "onSuccess: " + uri.toString());
                    urlList.add(uri.toString());

                    setRecyclerview(downBinding);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    downDialog.dismiss();
                }
            });


        }

    }

    private void setRecyclerview(DownloadBottomLayBinding downBinding) {

        Log.i(TAG, "downloadAudios: if");
        DownloadAdapter downloadAdapter = new DownloadAdapter(requireContext(), urlList, this);
        downBinding.rvDownload.setAdapter(downloadAdapter);

    }

    @Override
    public void onDownloadClick(int position, String url, LayDownloadBinding mBinding) {


        downloadFile(url, "audio_" + position, mBinding);


    }

    private void downloadFile(String url, String filename, LayDownloadBinding mBinding) {

        final ExecutorService service = Executors.newSingleThreadExecutor();

        service.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    URL downloadUrl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) downloadUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream inputStream = conn.getInputStream();

                    // Create a file to store the downloaded audio file
                    File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + filename);
                    FileOutputStream outputStream = new FileOutputStream(file);

                    // Write the downloaded audio file to the file output stream
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, len);
                        mBinding.dark.setProgress(len);

                    }
                    outputStream.close();
                    inputStream.close();


                } catch (IOException e) {

                    e.printStackTrace();
                }


            }
        });

        service.shutdown();

    }




    @Override
    public void onResume() {
        Log.i(TAG, "onResume: ");
        super.onResume();
    }
}