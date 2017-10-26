package pl.kfeed.gallerywithmusicplayer.dagger;

import android.app.Application;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.Provides;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.support.AndroidSupportInjectionModule;
import pl.kfeed.gallerywithmusicplayer.GalleryMusicPlayerApp;
import pl.kfeed.gallerywithmusicplayer.data.DataManager;

/**
 * Created by Kfeed on 20.10.2017.
 */

@Singleton
@Component(modules = {ApplicationModule.class,
        ActivityBindingModule.class,
        AndroidSupportInjectionModule.class})

public interface AppComponent extends AndroidInjector<DaggerApplication> {


    void inject(GalleryMusicPlayerApp app);

    @Override
    void inject(DaggerApplication instance);


    // Gives us syntactic sugar. we can then do DaggerAppComponent.builder().application(this).build().inject(this);
    // never having to instantiate any modules or say which module we are passing the application to.
    // Application will just be provided into our app graph now.
    @Component.Builder
    interface Builder {

        @BindsInstance
        AppComponent.Builder application(Application application);

        AppComponent build();
    }
}
