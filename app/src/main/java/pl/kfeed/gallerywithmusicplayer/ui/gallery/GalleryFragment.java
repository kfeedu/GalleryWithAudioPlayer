package pl.kfeed.gallerywithmusicplayer.ui.gallery;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;
import pl.kfeed.gallerywithmusicplayer.R;
import pl.kfeed.gallerywithmusicplayer.dagger.ActivityScoped;
import pl.kfeed.gallerywithmusicplayer.ui.MainActivity;
import pl.kfeed.gallerywithmusicplayer.ui.gallery.adapter.GalleryAdapter;
import pl.kfeed.gallerywithmusicplayer.ui.gallery.adapter.GalleryItemDecoration;
import pl.kfeed.gallerywithmusicplayer.ui.gallery.popup.PhotoPopup;


@ActivityScoped
public class GalleryFragment extends DaggerFragment implements GalleryContract.View, GalleryAdapter.OnPhotoClick {


    private final static int VERTICAL_NUMBER_OF_COLUMNS = 4;
    private final static int HORIZONTAL_NUMBER_OF_COLUMNS = 6;

    @BindView(R.id.galleryRecyclerView)
    RecyclerView mRecyclerView;

    @Inject GalleryPresenter mPresenter;
    GalleryAdapter mGalleryAdapter;

    private Cursor mImageThumbCursor;
    private PhotoPopup mPopup;


    @Inject
    public GalleryFragment(){}

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
        return view;
    }


    //Mvp methods

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setupRecyclerView(){
        mImageThumbCursor = mPresenter.getThumbnailsAndImageCursor();
        mGalleryAdapter = new GalleryAdapter(getActivity(), mImageThumbCursor, this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), VERTICAL_NUMBER_OF_COLUMNS));
        mRecyclerView.setAdapter(mGalleryAdapter);
        mRecyclerView.addItemDecoration(new GalleryItemDecoration(getActivity(), R.dimen.gallery_space_between));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void showError(String err) {
        Toast.makeText(getActivity(), err, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    //GalleryAdapter interface methods


    @Override
    public void photoClickedOnPosition(int position) {
        showPhotoPopup(mPresenter.getPathToImageOnPosition(position),
                mPresenter.getDateFromImageOnPosition(position));
    }

    private void showPhotoPopup(String imgPath, String date){
        LayoutInflater inflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Inflate the popup view
        final View layout = inflater.inflate(R.layout.popup_photo,
                (ViewGroup) getActivity().findViewById(R.id.popup_photo_layout));
        mPopup = new PhotoPopup(layout, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT, true, getActivity(), imgPath, date);
        mPopup.showAtLocation(getView(), Gravity.CENTER, 0, 0);
        mPopup.setTouchable(true);
        mPopup.setBackgroundDrawable(new ColorDrawable());
    }


}
