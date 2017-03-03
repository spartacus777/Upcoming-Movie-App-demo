package movie.android.kizema.moviesampleapp.model;

import java.util.List;

public class MovieCreditResponse {
    public List<CastMan> cast;

    public static class CastMan {
        public String character;
        public String name;
    }
}
