package movie.android.kizema.moviesampleapp.events;

import java.util.List;

import movie.android.kizema.moviesampleapp.model.Movie;

public class LatestMovieEvent extends BaseEvent {

    public List<Movie> movies;

    public int page;

    public int total_pages;
}
