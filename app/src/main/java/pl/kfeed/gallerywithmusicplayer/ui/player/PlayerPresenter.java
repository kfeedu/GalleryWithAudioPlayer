package pl.kfeed.gallerywithmusicplayer.ui.player;

import android.database.Cursor;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import pl.kfeed.gallerywithmusicplayer.data.DataManager;

public final class PlayerPresenter implements PlayerContract.Presenter {

    private PlayerContract.View mView;
    private DataManager mDataManager;

    @Inject
    PlayerPresenter(DataManager dataManager){
        mDataManager = dataManager;
    }

    @Override
    public void refreshData() {
        mView.updateAdapter(getSongCursor());
    }

    @Override
    public void attachView(PlayerContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public Cursor getSongCursor() {
        return mDataManager.getSongCursor();
    }
}
