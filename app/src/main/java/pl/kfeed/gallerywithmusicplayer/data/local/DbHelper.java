package pl.kfeed.gallerywithmusicplayer.data.local;

import android.content.Context;

import com.orm.SugarRecord;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import pl.kfeed.gallerywithmusicplayer.data.model.Song;


@Singleton
public class DbHelper {

    @Inject
    public DbHelper() {}

    public Song getSong(String songId) {
        List<Song> songs = Song.find(Song.class, "song_id = ?", songId);
        if (songs.isEmpty())
            return null;
        else
            return songs.get(0);
    }

    public void updateSong(String songId, int pauseTime) {
        List<Song> songs = Song.find(Song.class, "song_id = ?", songId);
        if (songs.isEmpty()) {
            addSongToDb(songId, pauseTime);
        } else {
            songs.get(0).pauseTime = pauseTime;
            songs.get(0).update();
        }
    }

    private void addSongToDb(String songId, int pauseTime) {
        Song.save(new Song(songId, pauseTime));
    }

    public void removeSongFromDb(String songId) {
        List<Song> songs = Song.find(Song.class, "song_id = ?", songId);
        if (!songs.isEmpty())
            songs.get(0).delete();
    }
}
