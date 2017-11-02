package pl.kfeed.gallerywithmusicplayer.ui.gallery.popup;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.PopupWindow;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.kfeed.gallerywithmusicplayer.Constants;
import pl.kfeed.gallerywithmusicplayer.R;
import pl.kfeed.gallerywithmusicplayer.ui.photofilter.PhotoFilterActivity;

public class PhotoPopup extends PopupWindow {

    @BindView(R.id.popupViewPager)
    ViewPager mViewPager;

    private PhotoPopupAdapter mAdapter;
    private Context mContext;
    private int mViewPosition;

    public PhotoPopup(View contentView, int width, int height, boolean focusable, Context context, Cursor imageThumbCursor, int position) {
        super(contentView, width, height, focusable);
        mContext = context;
        ButterKnife.bind(this, contentView);
        mAdapter = new PhotoPopupAdapter(context, imageThumbCursor);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(position);
        mViewPosition = position;
    }

    @OnClick(R.id.popupFab)
    void onFabClick() {
        Intent intent = new Intent(mContext, PhotoFilterActivity.class);
        intent.putExtra(Constants.PHOTO_FILTER_INTENT_PHOTO_PATH, mAdapter.getImagePathFromPosition(mViewPager.getCurrentItem()));
        mContext.startActivity(intent);
    }

    @OnClick(R.id.popupBackBtn)
    void onBackButtonClick() {
        dismiss();
    }

    public int getViewPosition() {
        return mViewPosition;
    }
}
