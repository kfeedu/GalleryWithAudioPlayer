package pl.kfeed.gallerywithmusicplayer.injection;

import dagger.Binds;
import dagger.Module;
import pl.kfeed.gallerywithmusicplayer.ui.photofilter.PhotoFilterActivity;
import pl.kfeed.gallerywithmusicplayer.ui.photofilter.PhotoFilterContract;
import pl.kfeed.gallerywithmusicplayer.ui.photofilter.PhotoFilterPresenter;

@Module
public abstract class FilterModule {

    @ActivityScoped
    @Binds
    abstract PhotoFilterContract.View photoFilterView(PhotoFilterActivity photoFilterView);

    @ActivityScoped
    @Binds
    abstract PhotoFilterContract.Presenter photoFilterPresenter(PhotoFilterPresenter photoFilterPresenter);
}
