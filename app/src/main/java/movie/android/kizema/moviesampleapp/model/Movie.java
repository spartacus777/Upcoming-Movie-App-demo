package movie.android.kizema.moviesampleapp.model;

import com.google.gson.annotations.SerializedName;

public class Movie {

    @SerializedName("poster_path")
    public String poster_path;

    @SerializedName("overview")
    public String overview;

    @SerializedName("title")
    public String title;

    @SerializedName("id")
    public int id;

    public String toString(){
        return "Movie " + title + "  Overview " + overview;
    }
}
