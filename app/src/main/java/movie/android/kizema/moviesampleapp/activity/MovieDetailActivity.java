package movie.android.kizema.moviesampleapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import movie.android.kizema.moviesampleapp.R;
import movie.android.kizema.moviesampleapp.model.Movie;
import movie.android.kizema.moviesampleapp.network.controller.NetworkController;
import movie.android.kizema.moviesampleapp.util.UIHelper;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String EXTRA = "EXTRA";

    private Movie movie;

    @BindView(R.id.tvTitle)
    TextView tvName;

    @BindView(R.id.tvUserVotes)
    TextView tvUserVotes;

    @BindView(R.id.tvDate)
    TextView tvDate;

    @BindView(R.id.tvRating)
    TextView tvRating;

    @BindView(R.id.tvDescr)
    TextView tvDescr;

    @BindView(R.id.ivPoster)
    ImageView ivPoster;

    public static Intent createIntent(Activity activity, Movie movie){
        Intent intent = new Intent(activity, MovieDetailActivity.class);
        intent.putExtra(EXTRA, Parcels.wrap(movie));
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        movie = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA));

        setupUI();
    }

    private void setupUI(){
        tvName.setText(movie.title);
        tvDate.setText(movie.release_date);
        tvUserVotes.setText("" + movie.vote_count);
        tvDescr.setText(movie.overview);
        tvRating.setText(""+ UIHelper.round(movie.popularity));

        Glide.with(this)
                .load(NetworkController.IMAGE_DOMAIN + movie.poster_path)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .crossFade()
                .into(ivPoster);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.noanim, R.anim.activity_transition_out);
    }
}
