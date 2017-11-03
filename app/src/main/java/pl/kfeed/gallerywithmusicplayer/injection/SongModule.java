package pl.kfeed.gallerywithmusicplayer.injection;

import dagger.Binds;
import dagger.Module;

import pl.kfeed.gallerywithmusicplayer.ui.player.song.SongActivity;
import pl.kfeed.gallerywithmusicplayer.ui.player.song.SongContract;
import pl.kfeed.gallerywithmusicplayer.ui.player.song.SongPresenter;

@Module
public abstract class SongModule {
    @ActivityScoped
    @Binds
    abstract SongContract.View songView(SongActivity songView);

    @ActivityScoped
    @Binds
    abstract SongContract.Presenter songPresenter(SongPresenter songPresenter);
}