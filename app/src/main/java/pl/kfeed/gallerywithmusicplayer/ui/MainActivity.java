package pl.kfeed.gallerywithmusicplayer.ui;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;
import pl.kfeed.gallerywithmusicplayer.R;
import pl.kfeed.gallerywithmusicplayer.ui.gallery.GalleryFragment;
import pl.kfeed.gallerywithmusicplayer.ui.player.PlayerFragment;
import pl.kfeed.gallerywithmusicplayer.util.PermissionUtil;

public class MainActivity extends DaggerAppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.mainToolbar)
    Toolbar mToolbar;
    @BindView(R.id.mainViewPager)
    ViewPager mViewPager;
    @BindView(R.id.mainTabLayout)
    TabLayout mTabLayout;

    @Inject
    Lazy<GalleryFragment> galleryFragmentProvider;
    @Inject
    Lazy<PlayerFragment> playerFragmentProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        //Configuring tabs on toolbar
        if (PermissionUtil.requestStoragePermission(this)) {
            setupViewPager();
            mTabLayout.setupWithViewPager(mViewPager);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PermissionUtil.wasAllGranted(grantResults)) {
            setupViewPager();
            mTabLayout.setupWithViewPager(mViewPager);
        }
    }

    private void setupViewPager() {
        MainViewPagerAdapter viewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(galleryFragmentProvider.get(), getString(R.string.gallery_name));
        viewPagerAdapter.addFragment(playerFragmentProvider.get(), getString(R.string.player_name));
        mViewPager.setAdapter(viewPagerAdapter);
    }
}


