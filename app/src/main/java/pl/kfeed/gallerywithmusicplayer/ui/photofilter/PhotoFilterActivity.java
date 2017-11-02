package pl.kfeed.gallerywithmusicplayer.ui.photofilter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;
import pl.kfeed.gallerywithmusicplayer.Constants;
import pl.kfeed.gallerywithmusicplayer.R;

public class PhotoFilterActivity extends DaggerAppCompatActivity implements PhotoFilterContract.View {

    private String mPhotoPath;

    @Inject
    PhotoFilterPresenter mPresenter;

    @BindView(R.id.filterPhotoGrowingCirclesBtn)
    ImageButton mGrowingCirclesBtn;
    @BindView(R.id.filterPhotoRotatedCheckerBtn)
    ImageButton mRotatedCheckerBtn;
    @BindView(R.id.filterPhotoWeirdCirclesBtn)
    ImageButton mWeirdCirclesBtn;
    @BindView(R.id.filterPhotoPreview)
    ImageView mImage;
    @BindView(R.id.filterProgressBar)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_filter);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mPhotoPath = intent.getStringExtra(Constants.PHOTO_FILTER_INTENT_PHOTO_PATH);
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
    protected void onDestroy() {
        mPresenter.stopPhotoProcessing();
        super.onDestroy();
    }

    @Override
    public void setPreviewView(Bitmap bitmap) {
        mImage.setImageBitmap(bitmap);
    }

    @Override
    public void showError(String err) {
        Toast.makeText(this, err, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.filterPhotoGrowingCirclesBtn)
    void onGrowingCirclesClick() {
        showProgressBar();
        mPresenter.setGrowingCirclesPreview(mPhotoPath, mImage.getWidth(), mImage.getHeight());
    }

    @OnClick(R.id.filterPhotoWeirdCirclesBtn)
    void onWeirdCirclesClick() {
        showProgressBar();
        mPresenter.setWeirdCirclesPreview(mPhotoPath, mImage.getWidth(), mImage.getHeight());
    }

    @OnClick(R.id.filterPhotoRotatedCheckerBtn)
    void onRotatedCheckerClick() {
        showProgressBar();
        mPresenter.setRotatedCheckerPreview(mPhotoPath, mImage.getWidth(), mImage.getHeight());
    }

    @OnClick(R.id.filterSaveBtn)
    void onSaveClick() {
        mPresenter.savePhoto(((BitmapDrawable)mImage.getDrawable()).getBitmap());
    }
}
