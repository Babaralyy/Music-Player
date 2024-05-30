package com.example.azikeamusic.service;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.example.azikeamusic.R;
import com.example.azikeamusic.utils.Constant;

public class AudioService extends Service {
    private static final String TAG = "AudioService";

    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "MyChannelId";
    private static final String CHANNEL_NAME = "MyChannelName";


    public static MediaPlayer mediaPlayer;



    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: AudioService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(Constant.START_FOREGROUND_ACTION)){
            showNotification(this);
        } else if(intent.getAction().equals(Constant.STOP_FOREGROUND_ACTION)){
            stopForeground(true);
            stopSelfResult(startId);
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        if (mediaPlayer != null){

            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
        // Clean up any resources or variables you used for your service
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: AudioService");
        return null;
    }

    public void showNotification(Context context){
        // Create a notification to show that the service is running

        // Create a notification object
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(Constant.songTitle)
                .setContentText(Constant.songDetail)
                .setSmallIcon(R.drawable.logo)
                .setOnlyAlertOnce(true)
                .setShowWhen(false)
                .build();

        // Create a notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        // Start the foreground service
       startForeground(NOTIFICATION_ID, notification);

        Log.i(TAG, "onStartCommand: AudioService");
    }
}



