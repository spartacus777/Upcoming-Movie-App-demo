package movie.android.kizema.moviesampleapp.events;

import java.util.List;

import movie.android.kizema.moviesampleapp.model.MovieCreditResponse;

public class MoviePosterEvent extends BaseEvent {
    public List<MovieCreditResponse.CastMan> cast;
    public List<MovieCreditResponse.Crew> crew;

}
