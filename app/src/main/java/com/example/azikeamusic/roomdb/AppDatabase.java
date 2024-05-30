package com.example.azikeamusic.roomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {AudioFile.class, PlayList.class, AddToPlayList.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AudioDao audioDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "AudioDatabase").allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
