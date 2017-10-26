package pl.kfeed.gallerywithmusicplayer.ui.player;

import android.support.annotation.Nullable;

import javax.inject.Inject;

import pl.kfeed.gallerywithmusicplayer.dagger.ActivityScoped;

/**
 * Created by Kfeed on 19.10.2017.
 */

public final class PlayerPresenter implements PlayerContract.Presenter {

    @Nullable
    private PlayerContract.View mPlayerView;

    @Inject
    PlayerPresenter(){}

    @Override
    public void takeView(PlayerContract.View view) {

    }

    @Override
    public void dropView() {

    }
}
