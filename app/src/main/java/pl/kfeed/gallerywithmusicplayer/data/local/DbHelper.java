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

    public void saveSongToDb(int songId, long pauseTime){
        Song song = new Song(songId, pauseTime);
        song.save();
    }

    public long getSongPauseTime(int songId){
        List<Song> songs = Song.listAll(Song.class);
        for(Song song: songs){
            if(song.getSongId() == songId){
                return song.getTimeOfPause();
            }
        }
        return -1;
    }

    public void updateSong(int songId, long pauseTime){
        long dbId = getDbId(songId);
        if(dbId == -1){
            saveSongToDb(songId, pauseTime);
        }else{
            Song song = Song.findById(Song.class, dbId);
            song.setTimeOfPause(pauseTime);
            song.save();
        }
    }

    public long getDbId(int songId){
        List<Song> songs = Song.listAll(Song.class);
        for(Song song: songs){
            if(song.getSongId() == songId){
                return song.getId();
            }
        }
        return -1;
    }
}
