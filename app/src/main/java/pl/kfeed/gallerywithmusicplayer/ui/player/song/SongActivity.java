package pl.kfeed.gallerywithmusicplayer.ui.player.song;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import dagger.android.support.DaggerAppCompatActivity;
import pl.kfeed.gallerywithmusicplayer.R;

public class SongActivity extends DaggerAppCompatActivity implements SongContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);
    }
}
