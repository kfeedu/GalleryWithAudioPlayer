package pl.kfeed.gallerywithmusicplayer.data.local;

import android.content.Context;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import pl.kfeed.gallerywithmusicplayer.data.model.Song;


@Singleton
public class DbHelper {

    private Context mContext;

    @Inject
    public DbHelper(Context context){}

    public Song getSong(int songId){
        List<Song> songs = Song.find(Song.class,"songId = ?", String.valueOf(songId));
        if(songs.isEmpty()){
            return null;
        }else{
            return songs.get(0);
        }
    }

    public void updateSong(int songId, long pauseTime){
        List<Song> songs = Song.find(Song.class, "songId = ?", String.valueOf(songId));
        if(songs.isEmpty()){
            addSongToDb(songId, pauseTime);
        }else{
            songs.get(0).setTimeOfPause(pauseTime);
            songs.get(0).save();
        }
    }

    private void addSongToDb(int songId, long pauseTime){
        Song song = new Song(songId, pauseTime);
        song.save();
    }

}
