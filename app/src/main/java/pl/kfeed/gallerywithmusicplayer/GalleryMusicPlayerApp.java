package pl.kfeed.gallerywithmusicplayer;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import pl.kfeed.gallerywithmusicplayer.dagger.AppComponent;
import pl.kfeed.gallerywithmusicplayer.dagger.DaggerAppComponent;

/**
 * Created by Kfeed on 20.10.2017.
 */

public class GalleryMusicPlayerApp extends DaggerApplication {

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        AppComponent appComponent = DaggerAppComponent.builder().application(this).build();
        appComponent.inject(this);
        return appComponent;
    }
}
