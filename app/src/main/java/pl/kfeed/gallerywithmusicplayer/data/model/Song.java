package pl.kfeed.gallerywithmusicplayer.data.model;

import com.orm.SugarRecord;

public class Song extends SugarRecord<Song> {

    int songId;
    long timeOfPause;

    public Song() {}

    public Song(int songId, long timeOfPause) {
        this.songId = songId;
        this.timeOfPause = timeOfPause;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public long getTimeOfPause() {
        return timeOfPause;
    }

    public void setTimeOfPause(long timeOfPause) {
        this.timeOfPause = timeOfPause;
    }
}
