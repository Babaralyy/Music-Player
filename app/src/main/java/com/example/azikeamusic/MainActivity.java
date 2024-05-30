package com.example.azikeamusic;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.azikeamusic.service.AudioService;
import com.example.azikeamusic.utils.Constant;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Boolean doubleBackToExitPressedOnce = false;
    private Boolean isDestroyed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    protected void onDestroy() {
        if (!isDestroyed){
            closeService();
        }
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            isDestroyed = true;
            closeService();
            super.onBackPressed();

            return;
        }

        doubleBackToExitPressedOnce = true;
        Toast.makeText(
                this,
                "Tap again to exit.",
                Toast.LENGTH_LONG
        ).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
        Log.i(TAG, "onBackPressed: ");

    }

    private void closeService() {
        // Assuming you have a reference to your Service instance
        // and a notification id for your foreground notification
        Intent stopIntent = new Intent(MainActivity.this, AudioService.class);
        stopIntent.setAction(Constant.STOP_FOREGROUND_ACTION);
        startService(stopIntent);
    }
}