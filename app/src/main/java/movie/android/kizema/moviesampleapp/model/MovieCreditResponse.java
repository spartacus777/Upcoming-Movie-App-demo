package movie.android.kizema.moviesampleapp.model;

import java.util.List;

public class MovieCreditResponse {
    public List<CastMan> cast;
    public List<Crew> crew;

    public static class CastMan {
        public String character;
        public String name;
    }

    public static class Crew {
        public String job;
        public String name;
    }
}
