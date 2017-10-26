package pl.kfeed.gallerywithmusicplayer.ui.player;

import android.support.annotation.Nullable;

import javax.inject.Inject;

/**
 * Created by Kfeed on 19.10.2017.
 */

public final class PlayerPresenter implements PlayerContract.Presenter {

    @Nullable
    private PlayerContract.View mPlayerView;

    @Inject
    PlayerPresenter(){}

    @Override
    public void attachView(PlayerContract.View view) {

    }

    @Override
    public void detachView() {

    }
}
