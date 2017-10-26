package pl.kfeed.gallerywithmusicplayer.dagger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by Kfeed on 23.10.2017.
 */

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface AppContext {

}
