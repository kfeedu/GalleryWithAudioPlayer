package pl.kfeed.gallerywithmusicplayer.dagger;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pl.kfeed.gallerywithmusicplayer.ui.MainActivity;
import pl.kfeed.gallerywithmusicplayer.ui.MainModule;

/**
 * Created by Kfeed on 20.10.2017.
 */
@Module
public abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(modules = MainModule.class)
    abstract MainActivity mainActivity();
}
