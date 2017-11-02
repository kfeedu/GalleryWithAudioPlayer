package pl.kfeed.gallerywithmusicplayer.ui.player;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.kfeed.gallerywithmusicplayer.R;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private final static String TAG = PlayerAdapter.class.getSimpleName();

    private Cursor mSongCursor;
    private Context mContext;
    private OnSongClick mListener;

    public PlayerAdapter(Context context, Cursor songCursor, OnSongClick listener) {
        mSongCursor = songCursor;
        mContext = context;
        mListener = listener;
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_view_holder, null);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlayerViewHolder holder, int position) {
        mSongCursor.moveToPosition(position);
        holder.mTitle.setText(mSongCursor.getString(mSongCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
        holder.mAuthor.setText(mSongCursor.getString(
                mSongCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
    }

    @Override
    public int getItemCount() {
        return mSongCursor.getCount();
    }

    public void updateCursor(Cursor newSongCursor) {
        closeCursor();
        mSongCursor = newSongCursor;
        notifyDataSetChanged();
    }

    public void closeCursor(){
        if(!mSongCursor.isClosed())
            mSongCursor.close();
    }

    public interface OnSongClick {
        void startSongActivity(int position);
    }

    class PlayerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.playerViewHolderAuthor)
        TextView mAuthor;
        @BindView(R.id.playerViewHolderTitle)
        TextView mTitle;

        public PlayerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener((view) -> {
                Log.d(TAG, "Item clicked on position: " + getAdapterPosition());
                mListener.startSongActivity(getAdapterPosition());
            });
        }
    }
}
