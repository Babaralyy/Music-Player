package com.example.azikeamusic.fragments;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.azikeamusic.R;
import com.example.azikeamusic.adapter.DownloadAdapter;
import com.example.azikeamusic.callbacks.DownloadCallback;
import com.example.azikeamusic.databinding.FragmentDownloadBinding;
import com.example.azikeamusic.databinding.LayDownloadBinding;
import com.example.azikeamusic.utils.Constant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2.FetchListener;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Priority;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2core.DownloadBlock;

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


public class DownloadFragment extends Fragment implements DownloadCallback {

    private static final String TAG = "DownloadFragment";
    private Dialog dialog;
    private List<String> urlList;

    Fetch fetch;


    private FragmentDownloadBinding mBinding;
    public DownloadFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentDownloadBinding.inflate(inflater);

        inIt();

        return mBinding.getRoot();
    }

    private void inIt() {

        urlList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        mBinding.rvDownload.setHasFixedSize(true);
        mBinding.rvDownload.setLayoutManager(linearLayoutManager);

        downloadAudios();
    }



    private void downloadAudios() {

        dialog = Constant.getDialog(requireContext());
        dialog.show();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("music");


        for (int i = 1; i <= 5; i++) {
            StorageReference fileRef = storageRef.child("audio_" + i + ".mp3");
            int finalI = i;
            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    dialog.dismiss();
                    Log.i(TAG, "onSuccess: " + uri.toString());
                    urlList.add(uri.toString());

                    setRecyclerview();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                }
            });


        }

    }

    private void setRecyclerview() {

        Log.i(TAG, "downloadAudios: if");
        DownloadAdapter downloadAdapter = new DownloadAdapter(requireContext(), urlList, this);
        mBinding.rvDownload.setAdapter(downloadAdapter);

    }

    @Override
    public void onDownloadClick(int position, String url, LayDownloadBinding mBinding) {


        downloadFile(url, "audio_" + position, mBinding);



//        FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(requireContext())
//                .setDownloadConcurrentLimit(5)
//                .build();
//
//        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + "audio_" +position +  ".mp3");
//        fetch = Fetch.Impl.getInstance(fetchConfiguration);
//
//
//        final Request request = new Request(url, String.valueOf(file));
//        request.setPriority(Priority.HIGH);
//        request.setNetworkType(NetworkType.ALL);
//        request.addHeader("clientKey", "SD78DF93_3947&MVNGHE1WONG");
//
//        fetch.enqueue(request, updatedRequest -> {
//            Log.i(TAG, "fetchAudio:: audio enqueued");
//        }, error -> {
//            Log.i(TAG, "fetchAudio:: audio enqueue failed ${it.throwable?.message}");
//        });
//
//        FetchListener fetchListener = new FetchListener() {
//            @Override
//            public void onAdded(@NonNull Download download) {
//
//            }
//
//            @Override
//            public void onQueued(@NonNull Download download, boolean b) {
//
//            }
//
//            @Override
//            public void onWaitingNetwork(@NonNull Download download) {
//
//            }
//
//            @Override
//            public void onCompleted(@NonNull Download download) {
//
//            }
//
//            @Override
//            public void onError(@NonNull Download download, @NonNull Error error, @Nullable Throwable throwable) {
//
//            }
//
//            @Override
//            public void onDownloadBlockUpdated(@NonNull Download download, @NonNull DownloadBlock downloadBlock, int i) {
//
//            }
//
//            @Override
//            public void onStarted(@NonNull Download download, @NonNull List<? extends DownloadBlock> list, int i) {
//
//            }
//
//            @Override
//            public void onProgress(@NonNull Download download, long l, long l1) {
//                mBinding.dark.setProgress((int)l, true);
//            }
//
//            @Override
//            public void onPaused(@NonNull Download download) {
//
//            }
//
//            @Override
//            public void onResumed(@NonNull Download download) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull Download download) {
//
//            }
//
//            @Override
//            public void onRemoved(@NonNull Download download) {
//
//            }
//
//            @Override
//            public void onDeleted(@NonNull Download download) {
//
//            }
//        };
//
//        fetch.addListener(fetchListener);

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



}