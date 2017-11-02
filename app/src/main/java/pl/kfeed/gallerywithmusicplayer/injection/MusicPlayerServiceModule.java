package pl.kfeed.gallerywithmusicplayer.injection;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pl.kfeed.gallerywithmusicplayer.service.MusicPlayerService;

@Module
public abstract class MusicPlayerServiceModule {

    @ContributesAndroidInjector
    abstract MusicPlayerService bindService();
}
