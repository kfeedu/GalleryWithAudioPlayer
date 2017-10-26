package pl.kfeed.gallerywithmusicplayer;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import pl.kfeed.gallerywithmusicplayer.injection.AppComponent;
import pl.kfeed.gallerywithmusicplayer.injection.DaggerAppComponent;

public class GalleryMusicPlayerApp extends DaggerApplication {

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        AppComponent appComponent = DaggerAppComponent.builder().application(this).build();
        appComponent.inject(this);
        return appComponent;
    }
}
