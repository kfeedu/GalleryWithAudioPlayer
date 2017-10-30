package pl.kfeed.gallerywithmusicplayer.ui.photofilter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;
import pl.kfeed.gallerywithmusicplayer.Constants;
import pl.kfeed.gallerywithmusicplayer.R;
import pl.kfeed.gallerywithmusicplayer.injection.DaggerAppComponent;

public class PhotoFilterActivity extends DaggerAppCompatActivity implements PhotoFilterContract.View {

    private String mPhotoPath;

    @Inject
    PhotoFilterPresenter mPresenter;

    @BindView(R.id.filterToolbar)
    Toolbar mToolbar;
    @BindView(R.id.filterPhotoGrowingCirclesBtn)
    ImageButton mGrowingCirclesBtn;
    @BindView(R.id.filterPhotoRotatedCheckerBtn)
    ImageButton mRotatedCheckerBtn;
    @BindView(R.id.filterPhotoWeirdCirclesBtn)
    ImageButton mWeirdCirclesBtn;
    @BindView(R.id.filterPhotoPreview)
    ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_filter);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        Intent intent = getIntent();
        mPhotoPath = intent.getStringExtra(Constants.PHOTO_FILTER_INTENT_KEY);
        loadImageToPreview();
    }

    private void loadImageToPreview() {
        Picasso.with(this)
                .load("file://" + mPhotoPath)
                .placeholder(R.drawable.rick)
                .centerInside()
                .fit()
                .into(mImage);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {

        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    public void setPreviewView(Bitmap bitmap) {
        mImage.setImageBitmap(bitmap);
    }

    @Override
    public void showError(String err) {

    }

    @Override
    public void showToast(String message) {

    }

    @OnClick(R.id.filterPhotoGrowingCirclesBtn)
    void onGrowingCirclesClick() {
        mPresenter.setGrowingCirclesPreview(mPhotoPath, mImage.getWidth(), mImage.getHeight());
    }

    @OnClick(R.id.filterPhotoWeirdCirclesBtn)
    void onWeirdCirclesClick() {
        mPresenter.setWeirdCirclesPreview(mPhotoPath, mImage.getWidth(), mImage.getHeight());
    }

    @OnClick(R.id.filterPhotoRotatedCheckerBtn)
    void onRotatedCheckerClick() {
        mPresenter.setRotatedCheckerPreview(mPhotoPath, mImage.getWidth(), mImage.getHeight());
    }
}
