package pl.kfeed.gallerywithmusicplayer.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;
import pl.kfeed.gallerywithmusicplayer.R;
import pl.kfeed.gallerywithmusicplayer.ui.gallery.GalleryFragment;
import pl.kfeed.gallerywithmusicplayer.ui.player.PlayerFragment;
import pl.kfeed.gallerywithmusicplayer.ui.player.PlayerPresenter;
import pl.kfeed.gallerywithmusicplayer.util.PermissionUtil;

public class MainActivity extends DaggerAppCompatActivity  {

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
        if(PermissionUtil.requestStoragePermission(this)){
            setupViewPager();
            mTabLayout.setupWithViewPager(mViewPager);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(PermissionUtil.wasAllGranted(grantResults)){
            setupViewPager();
            mTabLayout.setupWithViewPager(mViewPager);
        }
    }

    private void setupViewPager(){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(galleryFragmentProvider.get(), getString(R.string.gallery_name));
        viewPagerAdapter.addFragment(playerFragmentProvider.get(), getString(R.string.player_name));
        mViewPager.setAdapter(viewPagerAdapter);
    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}


