package pl.kfeed.gallerywithmusicplayer.ui.gallery.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class GalleryItemDecoration extends RecyclerView.ItemDecoration {

    private int mItemOffset;

    public GalleryItemDecoration(int itemOffset) {
        mItemOffset = itemOffset;
    }

    public GalleryItemDecoration(@NonNull Context context, int itemOffset) {
        this(itemOffset);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
    }
}
