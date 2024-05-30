package com.example.azikeamusic.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.example.azikeamusic.R;
import com.example.azikeamusic.datamodels.AlbumDetailModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Constant {

   public static String songTitle;
   public static String songDetail;
   public static String songLyrics;
   public static final String START_FOREGROUND_ACTION = "START";
   public static final String STOP_FOREGROUND_ACTION = "STOP" ;

    public static Dialog getDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_lay);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }


    public static String getTrackTotalTime(Long duration) {
        Long myDuration = duration;

        long hours = TimeUnit.MILLISECONDS.toHours(myDuration);
        myDuration -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(myDuration);
        myDuration -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(myDuration);


        return String.format("%02d:%02d:%02d", hours, minutes, seconds);

    }



}
