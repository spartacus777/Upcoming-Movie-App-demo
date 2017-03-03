package movie.android.kizema.moviesampleapp.model;

import com.google.gson.annotations.SerializedName;

public class VideoResponse {

    @SerializedName("id")
    public String poster_path;

    @SerializedName("overview")
    public String overview;

    @SerializedName("title")
    public String title;

    public String toString(){
        return "Movie " + title + "  Overview " + overview;
    }
}
