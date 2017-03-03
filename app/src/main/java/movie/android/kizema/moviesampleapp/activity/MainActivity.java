package movie.android.kizema.moviesampleapp.activity;

import android.os.Bundle;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import movie.android.kizema.moviesampleapp.R;
import movie.android.kizema.moviesampleapp.adapter.MainActivityListHelper;
import movie.android.kizema.moviesampleapp.events.LatestMovieEvent;

public class MainActivity extends BaseActivity {

    @BindView(R.id.activity_main)
    View activity_main;

    private MainActivityListHelper mainActivityListHelper;

    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupUI();
    }

    private void setupUI(){
        mainActivityListHelper = new MainActivityListHelper(activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);

        mainActivityListHelper.handleOnResume(getShouldGoToServer(), savedInstanceState);
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

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(LatestMovieEvent event) {
        mainActivityListHelper.handleLatestMovieEvent(event);

        LatestMovieEvent stickyEvent = EventBus.getDefault().getStickyEvent(LatestMovieEvent.class);
        if(stickyEvent != null) {
            EventBus.getDefault().removeStickyEvent(stickyEvent);
        }
    }
}
