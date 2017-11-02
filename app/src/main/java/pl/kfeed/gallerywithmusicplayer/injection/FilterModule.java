package pl.kfeed.gallerywithmusicplayer.injection;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pl.kfeed.gallerywithmusicplayer.ui.gallery.GalleryContract;
import pl.kfeed.gallerywithmusicplayer.ui.gallery.GalleryFragment;
import pl.kfeed.gallerywithmusicplayer.ui.gallery.GalleryPresenter;
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
