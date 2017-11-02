package pl.kfeed.gallerywithmusicplayer.data.model;

import com.orm.SugarRecord;

public class Song extends SugarRecord<Song> {

    int songId;
    long pauseTime;

    public Song() {}

    public Song(int songId, long pauseTime) {
        this.songId = songId;
        this.pauseTime = pauseTime;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public long getTimeOfPause() {
        return pauseTime;
    }

    public void setTimeOfPause(long timeOfPause) {
        this.pauseTime = timeOfPause;
    }
}
