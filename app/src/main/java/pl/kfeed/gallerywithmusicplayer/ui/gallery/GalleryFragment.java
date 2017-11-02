package pl.kfeed.gallerywithmusicplayer.ui.gallery;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;
import pl.kfeed.gallerywithmusicplayer.Constants;
import pl.kfeed.gallerywithmusicplayer.R;
import pl.kfeed.gallerywithmusicplayer.injection.ActivityScoped;
import pl.kfeed.gallerywithmusicplayer.ui.gallery.adapter.GalleryAdapter;
import pl.kfeed.gallerywithmusicplayer.ui.gallery.adapter.GalleryItemDecoration;
import pl.kfeed.gallerywithmusicplayer.ui.gallery.popup.PhotoPopup;
import pl.kfeed.gallerywithmusicplayer.util.ColumnsSpacingUtil;

@ActivityScoped
public class GalleryFragment extends DaggerFragment implements GalleryContract.View,
        GalleryAdapter.OnPhotoClick, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.galleryRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.gallerySwipeRefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Inject
    GalleryPresenter mPresenter;
    private GalleryAdapter mGalleryAdapter;

    private Cursor mImageThumbCursor;
    private PhotoPopup mPopup;

    @Inject
    public GalleryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.bind(this, view);
        setupRecyclerView();
        mSwipeRefreshLayout.setOnRefreshListener(this);

        if (mPopup != null) {
            mPopup.showAtLocation(getView(), Gravity.CENTER, 0, 0);
        }

        return view;
    }

    private void setupRecyclerView() {
        mImageThumbCursor = mPresenter.getThumbnailsAndImageCursor();
        mGalleryAdapter = new GalleryAdapter(getActivity(), mImageThumbCursor, this);
        mRecyclerView.setAdapter(mGalleryAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        ColumnsSpacingUtil csUtil = new ColumnsSpacingUtil(getActivity(), R.layout.gallery_view_holder);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), csUtil.calculateNoOfColumns()));
        mRecyclerView.addItemDecoration(new GalleryItemDecoration(getActivity(), csUtil.calculateSpacing()));
    }

    @Override
    public void onRefresh() {
        mPresenter.refreshData();
        mSwipeRefreshLayout.setRefreshing(false);
        showToast(getString(R.string.refreshed));
    }

    //GalleryAdapter interface methods
    @Override
    public void showPhotoPopup(int position) {
        LayoutInflater inflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Inflate the popup view
        final View layout = inflater.inflate(R.layout.popup_photo_view,
                getActivity().findViewById(R.id.popup_photo_layout));
        mPopup = new PhotoPopup(layout, CoordinatorLayout.LayoutParams.MATCH_PARENT,
                CoordinatorLayout.LayoutParams.MATCH_PARENT, true, getActivity(),
                mImageThumbCursor, position);
        mPopup.showAtLocation(getView(), Gravity.CENTER, 0, 0);
        mPopup.setTouchable(true);
        mPopup.setBackgroundDrawable(new ColorDrawable());
    }

    //Mvp methods
    @Override
    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateAdapter(Cursor imageAndThumbCursor) {
        mImageThumbCursor.close();
        mImageThumbCursor = imageAndThumbCursor;
        mGalleryAdapter.updateCursor(mImageThumbCursor);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPresenter.attachView(this);
    }

    @Override
    public void onDetach() {
        mPresenter.detachView();
        super.onDetach();
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        if (mPopup != null)
//            outState.putInt(Constants.POPUP_SAVE_STATE_POSITION_KEY, mPopup.getViewPosition());
//        super.onSaveInstanceState(outState);
//    }
//
//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        if (savedInstanceState != null)
//            showPhotoPopup(savedInstanceState.getInt(Constants.POPUP_SAVE_STATE_POSITION_KEY));
//        super.onViewStateRestored(savedInstanceState);
//    }
}
