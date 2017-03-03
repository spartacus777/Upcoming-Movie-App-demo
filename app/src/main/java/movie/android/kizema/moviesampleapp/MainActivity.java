package movie.android.kizema.moviesampleapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import movie.android.kizema.moviesampleapp.model.LatestMoviesResponse;
import movie.android.kizema.moviesampleapp.model.VideoResponse;
import movie.android.kizema.moviesampleapp.network.GithubHelper;
import movie.android.kizema.moviesampleapp.util.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int SUCCESS_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Call<LatestMoviesResponse> call = GithubHelper.getInstance().getService().listMovies(getString(R.string.api_key), 1);

        call.enqueue(new Callback<LatestMoviesResponse>() {
            @Override
            public void onResponse(Call<LatestMoviesResponse> call, Response<LatestMoviesResponse> response) {
                if (response.isSuccessful() && response.code() == SUCCESS_CODE) {
                    Log.d(Logger.TAG, "Response: " + response.toString());
                    for (VideoResponse r : response.body().results) {
                        r.toString();
                    }
                } else {
                    onFailure(call, new Throwable("Wrong credentials"));
                }
            }

            @Override
            public void onFailure(Call<LatestMoviesResponse> call, Throwable t) {
                Log.d(Logger.TAG, "Response: " + t.toString());

            }
        });
    }
}
