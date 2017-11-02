package pl.kfeed.gallerywithmusicplayer;

import com.orm.SchemaGenerator;
import com.orm.SugarContext;
import com.orm.SugarDb;

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
