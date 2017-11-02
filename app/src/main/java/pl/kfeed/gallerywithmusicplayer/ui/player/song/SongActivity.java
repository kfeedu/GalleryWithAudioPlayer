package pl.kfeed.gallerywithmusicplayer.ui.player.song;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.Provides;
import dagger.android.support.DaggerAppCompatActivity;
import pl.kfeed.gallerywithmusicplayer.Constants;
import pl.kfeed.gallerywithmusicplayer.R;
import pl.kfeed.gallerywithmusicplayer.service.MusicPlayerService;

@Singleton
public class SongActivity extends DaggerAppCompatActivity implements SongContract.View, MusicPlayerService.Callbacks {

    private static final String TAG = SongActivity.class.getSimpleName();

    @BindView(R.id.songTitle)
    TextView mTitle;
    @BindView(R.id.songArtist)
    TextView mArtsit;
    @BindView(R.id.songPlayPauseBtn)
    ImageButton mPlayPauseBtn;
    @BindView(R.id.songSeekBar)
    SeekBar mSeekBar;
    @BindView(R.id.songActualTime)
    TextView mActualTime;
    @BindView(R.id.songMaxTime)
    TextView mMaxTime;

    @Inject
    SongPresenter mPresenter;

    private int mActualPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mActualPosition = intent.getIntExtra(Constants.SONG_ACTIVITY_INTENT_POSITION, 0);
        mPresenter.prepareService(mActualPosition, this);
        mPresenter.setupViewWithSongData(this);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mPresenter.seekTo(progress * 1000);
                    updateProgress(progress);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
//        mPresenter.registerForCallbacks(this);
    }

    @Override
    protected void onDestroy() {
        mPresenter.stopServiceIfNotPlaying();
        super.onDestroy();
    }

    @OnClick(R.id.songStopBtn)
    void onStopSongClicked() {
        mPresenter.stopSong();
    }

    @OnClick(R.id.songPlayPauseBtn)
    void onPlayPauseClicked(View view) {
        mPresenter.playPauseSong();
    }

    @OnClick(R.id.songPrevBtn)
    void onPrevClicked() {
        mPresenter.prevSong();
    }

    @OnClick(R.id.songNextBtn)
    void onNextClicked() {
        mPresenter.nextSong();
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setTitle(String newTitle) {
        mTitle.setText(newTitle);
    }

    @Override
    public void setArtist(String newArtist) {
        mArtsit.setText(newArtist);
    }

    @Override
    public void setPauseButton() {
        mPlayPauseBtn.setImageResource(R.drawable.ic_pause_black_48px);
    }

    @Override
    public void setPlayButton() {
        mPlayPauseBtn.setImageResource(R.drawable.ic_play_arrow_black_48px);
    }

    @Override
    public void songEnded() {
        mPlayPauseBtn.setImageResource(R.drawable.ic_pause_black_48px);
    }

    @Override
    public void setMaxDuration(int maxDuration) {
        Log.d(TAG, "SeekBar maxDuration=" + maxDuration);
        mSeekBar.setMax(maxDuration);
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss", Locale.getDefault());
        Date date = new Date(maxDuration * 1000);
        mMaxTime.setText(sdf.format(date));
    }

    @Override
    public void updateProgress(int progress) {
        Log.d(TAG, "Updating SeekBar: progress=" + progress);
        mSeekBar.setProgress(progress);
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss", Locale.getDefault());
        Date date = new Date(progress * 1000);
        mActualTime.setText(sdf.format(date));
    }
}
