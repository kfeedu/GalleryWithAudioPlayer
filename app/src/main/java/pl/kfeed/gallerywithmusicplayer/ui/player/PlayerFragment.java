package pl.kfeed.gallerywithmusicplayer.ui.player;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;
import pl.kfeed.gallerywithmusicplayer.Constants;
import pl.kfeed.gallerywithmusicplayer.R;
import pl.kfeed.gallerywithmusicplayer.injection.ActivityScoped;
import pl.kfeed.gallerywithmusicplayer.ui.player.song.SongActivity;

@ActivityScoped
public class PlayerFragment extends DaggerFragment implements PlayerContract.View,
        SwipeRefreshLayout.OnRefreshListener, PlayerAdapter.OnSongClick {

    private static final String TAG = PlayerFragment.class.getSimpleName();

    @BindView(R.id.playerRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.playerSwipeRefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Inject
    PlayerPresenter mPresenter;
    private PlayerAdapter mPlayerAdapter;

    @Inject
    public PlayerFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        ButterKnife.bind(this, view);
        setupRecyclerView();
        mSwipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    private void setupRecyclerView(){
        mPlayerAdapter = new PlayerAdapter(getActivity(), mPresenter.getSongCursor(), this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mPlayerAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
    }


    @Override
    public void onRefresh() {
        mPresenter.refreshData();
        mSwipeRefreshLayout.setRefreshing(false);
        showToast(getString(R.string.refreshed));
    }

    //RecyclerView Adapter method
    @Override
    public void startSongActivity(int position) {
        Intent intent = new Intent(getActivity(), SongActivity.class);
        intent.putExtra(Constants.SONG_ACTIVITY_INTENT_POSITION, position);
        startActivity(intent);
    }

    //MVP methods
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPresenter.attachView(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPresenter.detachView();
    }

    @Override
    public void onPause() {
        mPlayerAdapter.closeCursor();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.refreshData();
    }

    @Override
    public void updateAdapter(Cursor songCursor) {
        mPlayerAdapter.updateCursor(songCursor);
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
