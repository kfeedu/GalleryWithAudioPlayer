package pl.kfeed.gallerywithmusicplayer.ui.gallery.popup;

import android.content.Context;
import android.graphics.Rect;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.kfeed.gallerywithmusicplayer.R;

/**
 * Created by Kfeed on 26.10.2017.
 */

public class PhotoPopup extends PopupWindow {

    @BindView(R.id.popup_date)
    TextView mDate;
    @BindView(R.id.popup_time)
    TextView mTime;
    @BindView(R.id.popup_image)
    ImageView mImage;

    private Context mContext;


    public PhotoPopup(View contentView, int width, int height, boolean focusable, Context mContext, String imgPath, String date) {
        super(contentView, width, height, focusable);
        this.mContext = mContext;
        ButterKnife.bind(this, contentView);
        createView(imgPath, date);
    }

    private void createView(String photoPath, String date) {

        mDate.setText(date);
        mTime.setText(date);

        //Setting image with picasso
        Picasso.with(mContext).load("file://" + photoPath)
                .placeholder(R.drawable.rick)
                .fit()
                .centerInside()
                .into(mImage);
    }

    @OnClick(R.id.popup_fab)
    void onClick(View view){
        Toast.makeText(mContext, "Photo edition activity should be started, but its not :(", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.popup_back_btn)
    void onBackButtonClick(View view){
        dismiss();
    }
}
