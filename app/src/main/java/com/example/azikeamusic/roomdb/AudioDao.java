package com.example.azikeamusic.roomdb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AudioDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertTrack(AudioFile audioFile);

    @Query("SELECT * FROM AudioFile")
    LiveData<List<AudioFile>> getAllAudioFiles();

    @Query("SELECT * FROM AudioFile WHERE albumId = :aId")
    LiveData<List<AudioFile>> getAlbumAudioFiles(long aId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertPlayList(PlayList playList);

    @Query("SELECT * FROM PlayList")
    LiveData<List<PlayList>> getAllPlayLists();

    @Query("SELECT * FROM AddToPlayList WHERE playListId = :pId")
    LiveData<List<AddToPlayList>> getSongsFromPlayList(int pId);

    @Insert
    void insertIntoPlayList(AddToPlayList addToPlayList);


    @Query("DELETE  FROM PlayList WHERE id = :pId")
    void deletePlaylist(int pId);


    @Query("DELETE  FROM AddToPlayList WHERE audioId = :pId ")
    void deleteAllPlaylistSongs(String pId);

    @Query("DELETE  FROM AddToPlayList WHERE audioId = :aId AND playListId = :pId ")
    void deleteOnePlaylistSongs(String aId, String pId);

    @Query("UPDATE   AudioFile set audioLyrics = :lyrics WHERE id = :aId ")
    void updateLyrics(String lyrics, String aId);

    @Query("SELECT audioLyrics FROM AudioFile  WHERE id = :aId ")
    LiveData<String> getLyrics(String aId);
}
