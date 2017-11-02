package pl.kfeed.gallerywithmusicplayer;

import com.orm.Database;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import pl.kfeed.gallerywithmusicplayer.injection.AppComponent;
import pl.kfeed.gallerywithmusicplayer.injection.DaggerAppComponent;

public class GalleryMusicPlayerApp extends DaggerApplication {

    private Database database;

    protected Database getDatabase() {
        return database;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.database = new Database(this);
    }

    @Override
    public void onTerminate() {
        if (this.database != null) {
            this.database.getDB().close();
        }
        super.onTerminate();
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        AppComponent appComponent = DaggerAppComponent.builder().application(this).build();
        appComponent.inject(this);
        return appComponent;
    }
}
