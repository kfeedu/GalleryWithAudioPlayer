package pl.kfeed.gallerywithmusicplayer.ui.gallery.popup;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.kfeed.gallerywithmusicplayer.Constants;
import pl.kfeed.gallerywithmusicplayer.R;
import pl.kfeed.gallerywithmusicplayer.ui.photofilter.PhotoFilterActivity;

import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class PhotoPopup extends PopupWindow {

    @BindView(R.id.popup_date)
    TextView mDate;
    @BindView(R.id.popup_time)
    TextView mTime;
    @BindView(R.id.popup_image)
    ImageView mImage;

    private String mPhotoPath;
    private Context mContext;

    public PhotoPopup(View contentView, int width, int height, boolean focusable, Context context, String photoPath, Calendar date) {
        super(contentView, width, height, focusable);
        mContext = context;
        mPhotoPath = photoPath;
        ButterKnife.bind(this, contentView);
        createView(photoPath, date);
    }

    private void createView(String photoPath, Calendar date) {
        mDate.setText(date.get(Calendar.DAY_OF_MONTH) + " " + date.getDisplayName(MONTH, Calendar.LONG, Locale.getDefault())
                + ", " + date.get(YEAR));
        mTime.setText(date.get(Calendar.HOUR_OF_DAY) + ":" + date.get(Calendar.MINUTE));
        Picasso.with(mContext).load("file://" + photoPath)
                .placeholder(R.drawable.rick)
                .fit()
                .centerInside()
                .into(mImage);
    }

    @OnClick(R.id.popup_fab)
    void onClick(View view) {
        Intent intent = new Intent(mContext, PhotoFilterActivity.class);
        intent.putExtra(Constants.PHOTO_FILTER_INTENT_KEY, mPhotoPath);
        mContext.startActivity(intent);
    }

    @OnClick(R.id.popup_back_btn)
    void onBackButtonClick(View view) {
        dismiss();
    }
}
