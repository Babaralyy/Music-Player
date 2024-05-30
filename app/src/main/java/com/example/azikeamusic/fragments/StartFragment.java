package com.example.azikeamusic.fragments;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.azikeamusic.databinding.FragmentStartBinding;
import com.example.azikeamusic.utils.Constant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StartFragment extends Fragment {

    private static final String TAG = "StartFragment";
    private Dialog dialog;

    private FragmentStartBinding mBinding;
    public StartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentStartBinding.inflate(inflater);

        inIt();

        return mBinding.getRoot();
    }

    private void inIt() {


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
                    Log.i(TAG, "onSuccess: " + uri.toString());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                }
            });
        }

    }


    private void downloadFile(String url, String filename, Dialog dialog) {

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
                    }
                    outputStream.close();
                    inputStream.close();


                } catch (IOException e) {

                    e.printStackTrace();
                }


            }
        });


        dialog.dismiss();
        service.shutdown();

    }



}