package pl.kfeed.gallerywithmusicplayer.ui.player;

import android.content.Context;
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
import pl.kfeed.gallerywithmusicplayer.R;
import pl.kfeed.gallerywithmusicplayer.injection.ActivityScoped;

@ActivityScoped
public class PlayerFragment extends DaggerFragment implements PlayerContract.View,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = PlayerFragment.class.getSimpleName();

    @BindView(R.id.playerRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.playerSwipeRefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Inject
    PlayerPresenter mPresenter;
    private PlayerAdapter mPlayerAdapter;

    private Cursor mSongCursor;

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
        mSongCursor = mPresenter.getSongCursor();
        mPlayerAdapter = new PlayerAdapter(getActivity(), mSongCursor);
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
    public void updateAdapter(Cursor songCursor) {
        mPlayerAdapter.updateCursor(songCursor);
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
