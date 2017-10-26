package pl.kfeed.gallerywithmusicplayer.ui.gallery.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.kfeed.gallerywithmusicplayer.R;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    private static final String TAG = GalleryAdapter.class.getSimpleName();

    private Context mContext;
    private Cursor mThumbImageCursor;
    private OnPhotoClick mListener;

    public GalleryAdapter(Context context, Cursor thumbImageCursor, OnPhotoClick listener) {
        mThumbImageCursor = thumbImageCursor;
        mContext = context;
        mListener = listener;
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_view_holder, null);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {
        mThumbImageCursor.moveToPosition(position);
        if (mThumbImageCursor.getString(0) == null) {
            Picasso.with(mContext)
                    .load("file://" + mThumbImageCursor.getString(6))
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.rick)
                    .into(holder.image);
        } else {
            Picasso.with(mContext)
                    .load("file://" + mThumbImageCursor.getString(0))
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.thumbnail)
                    .into(holder.image);
        }
    }

    private Bitmap getThumbnailFromCursor(int position) {
        mThumbImageCursor.moveToPosition(position);
        Bitmap thumbnail = null;
        String thumbData = mThumbImageCursor.getString(0);  // thumb_path
        if (thumbData != null) {
            try {
                thumbnail = BitmapFactory.decodeFile(thumbData);
            } catch (Exception e) {
                Log.e("myapp", "PhotoAdapter.bindView() can't find thumbnail (file) on disk (thumbdata = " + thumbData + ")");
            }
        } else {
            String imgPath = mThumbImageCursor.getString(6);   // image_path
            String imgId = mThumbImageCursor.getString(1);  // ID
            Log.v("myapp", "PhotoAdapter.bindView() thumb path for image ID " + imgId + " is null. Trying to generate, with path = " + imgPath);

            try {
                thumbnail = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imgPath), 72, 72);
            } catch (Exception e) {
                Log.e("myapp", "PhotoAdapter.bindView() can't generate thumbnail for image path: " + imgPath);
            }
        }
        return thumbnail;
    }

    public void updateCursor(Cursor newImageThumbCursor) {
        mThumbImageCursor = newImageThumbCursor;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mThumbImageCursor.getCount();
    }

    public interface OnPhotoClick {
        void photoClickedOnPosition(int position);
    }

    public class GalleryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.galleryViewHolderThumbnail)
        ImageView image;

        public GalleryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "Item clicked on position: " + getAdapterPosition());
                    mListener.photoClickedOnPosition(getAdapterPosition());
                }
            });
        }
    }
}
