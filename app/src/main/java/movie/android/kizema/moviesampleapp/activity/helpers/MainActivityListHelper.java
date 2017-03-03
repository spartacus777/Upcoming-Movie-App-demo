package movie.android.kizema.moviesampleapp.activity.helpers;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import movie.android.kizema.moviesampleapp.App;
import movie.android.kizema.moviesampleapp.R;
import movie.android.kizema.moviesampleapp.adapter.MovieAdapter;
import movie.android.kizema.moviesampleapp.events.LatestMovieEvent;
import movie.android.kizema.moviesampleapp.model.Movie;
import movie.android.kizema.moviesampleapp.util.AppRecyclerView;
import movie.android.kizema.moviesampleapp.util.Logger;

public class MainActivityListHelper {

    private static final String KEY_LIST = "KEY_LIST";
    private static final String KEY_HAS_MORE = "KEY_HAS_MORE";
    private static final String KEY_NEXT_PAGE_ID = "KEY_NEXT_PAGE_ID";
    private static final String KEY_TOTAL_PAGES = "KEY_TOTAL_PAGES";

    private static final int INITIAL_PAGE_ID = 1;

    @BindView(R.id.rvRepos)
    AppRecyclerView rvRepos;

    @BindView(R.id.pbEmpty)
    ProgressBar pbEmpty;

    private MovieAdapter.OnAdapterClickListener listener;

    private MovieAdapter movieAdapter;

    private boolean mMoreCallOnGoing = false;
    private boolean hasMore = false;

    private int nextPageId = INITIAL_PAGE_ID;
    private int totalPages;

    public MainActivityListHelper(View main, MovieAdapter.OnAdapterClickListener listener){
        ButterKnife.bind(this, main);
        this.listener = listener;

        init();
    }

    public void handleOnResume(boolean shouldGoToServer, Bundle savedInstanceState){
        if (shouldGoToServer) {
            loadMovies();
        } else if (savedInstanceState != null){
            List<Movie> listMovies = Parcels.unwrap(savedInstanceState.getParcelable(KEY_LIST));
            hasMore = savedInstanceState.getBoolean(KEY_HAS_MORE);
            nextPageId = savedInstanceState.getInt(KEY_NEXT_PAGE_ID);
            totalPages = savedInstanceState.getInt(KEY_TOTAL_PAGES);
            movieAdapter.setList(listMovies);
        }
    }

    public void handleLatestMovieEvent(LatestMovieEvent event){
        movieAdapter.hideFooter();
        mMoreCallOnGoing = false;

        if (!event.isSuccess){
            Toast.makeText(rvRepos.getContext(), event.errorMsg, Toast.LENGTH_SHORT).show();
            return;
        }

        hasMore = event.page < event.total_pages;
        nextPageId = event.page + 1;
        totalPages = event.total_pages;

        movieAdapter.update(event.movies);
    }

    private void init(){
        movieAdapter = new MovieAdapter(listener);
        rvRepos.setAdapter(movieAdapter);
        LinearLayoutManager mChatLayoutManager = new LinearLayoutManager(rvRepos.getContext(), LinearLayoutManager.VERTICAL, false);
        rvRepos.setLayoutManager(mChatLayoutManager);
        rvRepos.setHasFixedSize(true);
        rvRepos.setEmptyView(pbEmpty);

        RecyclerView.OnScrollListener mScrollListner = new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {}

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager mg = (LinearLayoutManager) rvRepos.getLayoutManager();

                int totalCount = mg.getItemCount();

                int lastVisible = mg.findLastVisibleItemPosition();

                if (lastVisible < 0) {
                    return;
                }

                boolean loadMore = (lastVisible == (totalCount - 1));

                if (loadMore) {
                    if (!mMoreCallOnGoing  && hasMore) {
                        loadMovies();
                    }
                }
            }
        };

        rvRepos.addOnScrollListener(mScrollListner);
    }

    private void loadMovies(){
        mMoreCallOnGoing = true;
        movieAdapter.showFooter();

        Logger.d("Loading page " + nextPageId + "/" + totalPages);
        App.getController().getLatestMovies(nextPageId);
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_LIST, Parcels.wrap( movieAdapter.getItems()));
        outState.putBoolean(KEY_HAS_MORE, hasMore);
        outState.putInt(KEY_NEXT_PAGE_ID, nextPageId);
        outState.putInt(KEY_TOTAL_PAGES, totalPages);
    }
}
