package pl.kfeed.gallerywithmusicplayer.dagger;

import android.app.Application;
import android.content.Context;
import android.provider.ContactsContract;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import pl.kfeed.gallerywithmusicplayer.data.DataManager;

/**
 * Created by Kfeed on 20.10.2017.
 */

@Module
public abstract class ApplicationModule {

    //expose Application as an injectable context
    @Binds
    abstract Context bindContext(Application application);

}
