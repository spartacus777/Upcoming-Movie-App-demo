package movie.android.kizema.moviesampleapp.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Movie {

    @SerializedName("poster_path")
    public String poster_path;

    @SerializedName("overview")
    public String overview;

    @SerializedName("title")
    public String title;

    @SerializedName("id")
    public int id;

    @SerializedName("release_date")
    public String release_date;

    @SerializedName("popularity")
    public double popularity;

    @SerializedName("vote_count")
    public int vote_count;

    public String toString(){
        return "Movie " + title + "  Overview " + overview;
    }
}
