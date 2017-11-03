package pl.kfeed.gallerywithmusicplayer.data.model;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

public class Song extends SugarRecord {

    @Unique
    public String songId;
    public int pauseTime;

    public Song() {
    }

    public Song(String songId, int pauseTime) {
        this.songId = songId;
        this.pauseTime = pauseTime;
    }
}
