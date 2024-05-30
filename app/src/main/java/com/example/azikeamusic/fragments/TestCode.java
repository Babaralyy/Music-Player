package com.example.azikeamusic.fragments;


import android.database.Cursor;
import android.provider.MediaStore;

import com.example.azikeamusic.datamodels.AlbumDetailModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestCode {

//    ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
    // get a list of running services
//    List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(Integer.MAX_VALUE);

    // loop through the list and check if your foreground service is running
//        for (ActivityManager.RunningServiceInfo service : runningServices) {
//        if (AudioService.class.getName().equals(service.service.getClassName())) {
            // your foreground service is running
//                mService.showNotification(requireContext());
//            break;
//        }
//    }





    // Create a list of song paths
//    List<AlbumDetailModel> albumSongsList = new ArrayList<>();
//
//    Cursor cursor = requireContext().getContentResolver().query(
//            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//            new String[]{MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM},
//            MediaStore.Audio.Media.ALBUM_ID + "=?",
//            new String[]{String.valueOf(albumFile.getId())},
//            MediaStore.Audio.Media.TRACK);
//
//        if (cursor != null) {
//        int songPathColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
//        int songTitleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
//        int songArtistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
//        int songAlbumColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
//
//
//        if (cursor.moveToFirst()) {
//            do {
//                String songPath = cursor.getString(songPathColumn);
//                String songTitle = cursor.getString(songTitleColumn);
//                String songArtist = cursor.getString(songArtistColumn);
//                String songAlbum = cursor.getString(songAlbumColumn);
//                File file = new File(songPath);
//                albumSongsList.add(new AlbumDetailModel(file, songTitle, songArtist, songAlbum, albumFile.getAlbumArtUri()));
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//    }


//        showBottomDetailDialog(albumSongsList);


}
