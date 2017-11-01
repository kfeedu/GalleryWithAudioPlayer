package pl.kfeed.gallerywithmusicplayer.ui.gallery.popup;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import pl.kfeed.gallerywithmusicplayer.Constants;
import pl.kfeed.gallerywithmusicplayer.R;

import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class PhotoPopupAdapter extends PagerAdapter {

    private final static String TAG = PhotoPopupAdapter.class.getSimpleName();

    private Cursor mImageThumbCursor;
    private Context mContext;

    private String currentImgPath;

    public PhotoPopupAdapter(Context context, Cursor imageThumbCursor) {
        mContext = context;
        mImageThumbCursor = imageThumbCursor;
    }

    @Override
    public int getCount() {
        return mImageThumbCursor.getCount();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.d(TAG, "instanting item on position =" + position);

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.popup_photo_image, container,
                false);

        mImageThumbCursor.moveToPosition(position);
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(mImageThumbCursor.getLong(Constants.CURSOR_IMAGE_DATE) * 1000);
        currentImgPath = mImageThumbCursor.getString(Constants.CURSOR_IMAGE_PATH);

        TextView dateView = viewLayout.findViewById(R.id.popupDate);
        TextView timeView = viewLayout.findViewById(R.id.popupTime);
        ImageView imageView = viewLayout.findViewById(R.id.popupImage);

        dateView.setText(getFormattedDate(calendar));
        timeView.setText(getFormattedTime(calendar));
        Picasso.with(mContext).load("file://" + currentImgPath)
                .placeholder(R.drawable.rick)
                .fit()
                .centerInside()
                .into(imageView);

        container.addView(viewLayout);
        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    private String getFormattedDate(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_MONTH) +
                " " +
                calendar.getDisplayName(MONTH, Calendar.LONG, Locale.getDefault()) +
                ", " +
                calendar.get(YEAR);
    }

    private String getFormattedTime(Calendar calendar) {
        return calendar.get(Calendar.HOUR_OF_DAY) +
                ":" +
                calendar.get(Calendar.MINUTE);
    }

    public String getImagePathFromPosition(int position) {
        mImageThumbCursor.moveToPosition(position);
        return mImageThumbCursor.getString(Constants.CURSOR_IMAGE_PATH);
    }
}
