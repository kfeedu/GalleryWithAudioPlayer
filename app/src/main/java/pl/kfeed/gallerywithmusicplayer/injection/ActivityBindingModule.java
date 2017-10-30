package pl.kfeed.gallerywithmusicplayer.injection;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pl.kfeed.gallerywithmusicplayer.ui.MainActivity;
import pl.kfeed.gallerywithmusicplayer.ui.photofilter.PhotoFilterActivity;

@Module
public abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(modules = MainModule.class)
    abstract MainActivity mainActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = FilterModule.class)
    abstract PhotoFilterActivity photoFilterActivity();
}
