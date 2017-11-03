package pl.kfeed.gallerywithmusicplayer;

import com.orm.SugarContext;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import pl.kfeed.gallerywithmusicplayer.data.model.Song;
import pl.kfeed.gallerywithmusicplayer.injection.AppComponent;
import pl.kfeed.gallerywithmusicplayer.injection.DaggerAppComponent;

public class GalleryMusicPlayerApp extends DaggerApplication {


    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
        //little hack to force SugarORM to make a SONG Table
        Song.findById(Song.class, (long) 1);
    }

    @Override
    public void onTerminate() {
        SugarContext.terminate();
        super.onTerminate();
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        AppComponent appComponent = DaggerAppComponent.builder().application(this).build();
        appComponent.inject(this);
        return appComponent;
    }
}