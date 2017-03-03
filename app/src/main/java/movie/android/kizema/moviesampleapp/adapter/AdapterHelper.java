package movie.android.kizema.moviesampleapp.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import movie.android.kizema.moviesampleapp.App;
import movie.android.kizema.moviesampleapp.R;
import movie.android.kizema.moviesampleapp.events.LatestMovieEvent;
import movie.android.kizema.moviesampleapp.util.AppRecyclerView;
import movie.android.kizema.moviesampleapp.util.Logger;

public class AdapterHelper {

    private static final int INITIAL_PAGE_ID = 1;

    @BindView(R.id.rvRepos)
    AppRecyclerView rvRepos;

    @BindView(R.id.pbEmpty)
    ProgressBar pbEmpty;

    private MovieAdapter movieAdapter;

    private boolean mMoreCallOnGoing = false;
    private boolean mUserScrolled = false;

    private boolean hasMore = false;
    private int nextPageId = INITIAL_PAGE_ID;
    private int totalPages;

    public AdapterHelper(View main){
        ButterKnife.bind(this, main);

        init();
    }

    public void handleOnResume(boolean shouldGoToServer){
        if (shouldGoToServer) {
            loadMovies();
        }
    }

    private void loadMovies(){
        mMoreCallOnGoing = true;
        movieAdapter.showFooter();

        Logger.d("Loading page " + nextPageId + "/" + totalPages);
        App.getController().getLatestMovies(nextPageId);
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
        movieAdapter = new MovieAdapter();
        rvRepos.setAdapter(movieAdapter);
        LinearLayoutManager mChatLayoutManager = new LinearLayoutManager(rvRepos.getContext(), LinearLayoutManager.VERTICAL, false);
        rvRepos.setLayoutManager(mChatLayoutManager);
        rvRepos.setHasFixedSize(true);
        rvRepos.setEmptyView(pbEmpty);

        RecyclerView.OnScrollListener mScrollListner = new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    mUserScrolled = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager mg = (LinearLayoutManager) rvRepos.getLayoutManager();

                int totalCount = mg.getItemCount();

                int lastVisible = mg.findLastVisibleItemPosition();

                if (lastVisible < 0) {
                    //no item in the list view at all, do nothing since load more only happen after list
                    //have sth
                    return;
                }

                boolean loadMore = (lastVisible == (totalCount - 1));

                //Logging.log("monkey", String.format("%d, %d", lastVisible, totalCount));
                if (loadMore) {
                    //only queryMore when
                    // 1) list view is scrolled
                    // 2) queryMore call is not ongoing
                    // 3) hasMore() returns true
                    //Logging.log("monkey", mMoreCallOnGoing + ", onScroll mUserScrolled=" + mUserScrolled + ", hasMore()=" + mPaginateCallInstance.hasMore());
                    if (!mMoreCallOnGoing  && hasMore) {//&& mUserScrolled
                        // add protection to avoid multiple call
                        mUserScrolled = false;

                        loadMovies();
                    } else {
//                        PhotosListAdapter adapter = (PhotosListAdapter) mList.getAdapter();
//                        if (!mPaginateCallInstance.hasMore()) {
//                            //hide footer
//                            adapter.setFooterStatus(false);
//                        } else {
//                            adapter.setFooterStatus(true);
//                        }
                    }
                }

            }
        };

        rvRepos.addOnScrollListener(mScrollListner);
    }

}
