package movie.android.kizema.moviesampleapp.network;

import movie.android.kizema.moviesampleapp.model.LatestMoviesResponse;
import movie.android.kizema.moviesampleapp.model.MovieCreditResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GitHubService {

    @GET("3/movie/upcoming")
    Call<LatestMoviesResponse> listMovies(@Query("api_key") String api_key, @Query("page") int page);

    @GET("3/movie/{movie_id}/credits")
    Call<MovieCreditResponse> getImageByMovieId(@Path("movie_id") Integer idMovie, @Query("api_key") String api_key);

}
