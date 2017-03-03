package movie.android.kizema.moviesampleapp.network.controller;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import movie.android.kizema.moviesampleapp.App;
import movie.android.kizema.moviesampleapp.R;
import movie.android.kizema.moviesampleapp.events.LatestMovieEvent;
import movie.android.kizema.moviesampleapp.events.MoviePosterEvent;
import movie.android.kizema.moviesampleapp.model.LatestMoviesResponse;
import movie.android.kizema.moviesampleapp.model.MovieCreditResponse;
import movie.android.kizema.moviesampleapp.network.GithubHelper;
import movie.android.kizema.moviesampleapp.util.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkController implements BaseController{

    private static final int SUCCESS_CODE = 200;
    public static final String IMAGE_DOMAIN = "https://image.tmdb.org/t/p/w500";

    @Override
    public void getLatestMovies(int page) {
        Call<LatestMoviesResponse> call = GithubHelper.getInstance().getService().listMovies(App.getAppContext().getString(R.string.api_key), page);

        call.enqueue(new Callback<LatestMoviesResponse>() {
            @Override
            public void onResponse(Call<LatestMoviesResponse> call, Response<LatestMoviesResponse> response) {
                if (response.isSuccessful() && response.code() == SUCCESS_CODE) {
                    Log.d(Logger.TAG, "Response: " + response.toString());
                    LatestMoviesResponse resp = response.body();

                    LatestMovieEvent event = new LatestMovieEvent();
                    event.isSuccess = true;
                    event.movies = resp.results;
                    event.page = resp.page;
                    event.total_pages = resp.total_pages;

                    EventBus.getDefault().postSticky(event);
                } else {
                    onFailure(call, new Throwable("Wrong credentials"));
                }
            }

            @Override
            public void onFailure(Call<LatestMoviesResponse> call, Throwable t) {
                Log.d(Logger.TAG, "Response: " + t.toString());

                LatestMovieEvent event = new LatestMovieEvent();
                event.isSuccess = false;
                event.errorMsg = t.toString();

                EventBus.getDefault().postSticky(event);
            }
        });
    }

    @Override
    public void getMovieImages(int movieId) {
        Call<MovieCreditResponse> call = GithubHelper.getInstance().getService().getImageByMovieId(movieId, App.getAppContext().getString(R.string.api_key));

        call.enqueue(new Callback<MovieCreditResponse>() {
            @Override
            public void onResponse(Call<MovieCreditResponse> call, Response<MovieCreditResponse> response) {
                if (response.isSuccessful() && response.code() == SUCCESS_CODE) {
                    Log.d(Logger.TAG, "Response: " + response.toString());
                    MovieCreditResponse resp = response.body();

                    MoviePosterEvent event = new MoviePosterEvent();
                    event.isSuccess = true;
                    event.cast = resp.cast;

                    EventBus.getDefault().post(event);
                } else {
                    onFailure(call, new Throwable("Wrong credentials"));
                }
            }

            @Override
            public void onFailure(Call<MovieCreditResponse> call, Throwable t) {
                Log.d(Logger.TAG, "Response: " + t.toString());

                MoviePosterEvent event = new MoviePosterEvent();
                event.isSuccess = false;
                event.errorMsg = t.toString();

                EventBus.getDefault().post(event);
            }
        });
    }
}
