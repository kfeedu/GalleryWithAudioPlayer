package pl.kfeed.gallerywithmusicplayer.ui;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pl.kfeed.gallerywithmusicplayer.dagger.ActivityScoped;
import pl.kfeed.gallerywithmusicplayer.dagger.FragmentScoped;
import pl.kfeed.gallerywithmusicplayer.ui.gallery.GalleryContract;
import pl.kfeed.gallerywithmusicplayer.ui.gallery.GalleryFragment;
import pl.kfeed.gallerywithmusicplayer.ui.gallery.GalleryPresenter;
import pl.kfeed.gallerywithmusicplayer.ui.player.PlayerContract;
import pl.kfeed.gallerywithmusicplayer.ui.player.PlayerFragment;
import pl.kfeed.gallerywithmusicplayer.ui.player.PlayerPresenter;

/**
 * Created by Kfeed on 23.10.2017.
 */
@Module
public abstract class MainModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract GalleryFragment galleryFragment();

    @ActivityScoped
    @Binds
    abstract GalleryContract.Presenter galleryPresenter(GalleryPresenter galleryPresenter);

    @FragmentScoped
    @ContributesAndroidInjector
    abstract PlayerFragment playerFragment();

    @ActivityScoped
    @Binds
    abstract PlayerContract.Presenter playerPresenter(PlayerPresenter playerPresenter);

}
