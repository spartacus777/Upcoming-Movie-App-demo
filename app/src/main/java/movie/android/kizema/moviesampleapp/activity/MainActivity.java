package movie.android.kizema.moviesampleapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import movie.android.kizema.moviesampleapp.R;
import movie.android.kizema.moviesampleapp.activity.helpers.MainActivityListHelper;
import movie.android.kizema.moviesampleapp.adapter.MovieAdapter;
import movie.android.kizema.moviesampleapp.events.LatestMovieEvent;
import movie.android.kizema.moviesampleapp.model.Movie;

public class MainActivity extends BaseActivity implements MovieAdapter.OnAdapterClickListener {

    @BindView(R.id.activity_main)
    View activity_main;

    private MainActivityListHelper mainActivityListHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupUI();
        mainActivityListHelper.hadleConfigChanges(savedInstanceState);
    }

    private void setupUI(){
        mainActivityListHelper = new MainActivityListHelper(activity_main, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);

        mainActivityListHelper.handleOnResume(getShouldGoToServer());
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mainActivityListHelper.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LatestMovieEvent event) {
        mainActivityListHelper.handleLatestMovieEvent(event);
    }

    @Override
    public void onItemCLick(Movie movie) {
        Intent intent = MovieDetailActivity.createIntent(this, movie);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_transition_in, R.anim.noanim);
    }
}
