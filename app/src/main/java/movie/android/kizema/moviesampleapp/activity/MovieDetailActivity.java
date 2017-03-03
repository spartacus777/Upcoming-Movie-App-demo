package movie.android.kizema.moviesampleapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import movie.android.kizema.moviesampleapp.App;
import movie.android.kizema.moviesampleapp.R;
import movie.android.kizema.moviesampleapp.events.MoviePosterEvent;
import movie.android.kizema.moviesampleapp.model.Movie;
import movie.android.kizema.moviesampleapp.model.MovieCreditResponse;
import movie.android.kizema.moviesampleapp.network.controller.NetworkController;
import movie.android.kizema.moviesampleapp.util.UIHelper;

public class MovieDetailActivity extends BaseActivity {

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

    @BindView(R.id.tvCast)
    TextView tvCast;

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
        ivPoster.setLayoutParams(new LinearLayout.LayoutParams(UIHelper.getW()/3, UIHelper.getW()/2));

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
                .centerCrop()
                .crossFade()
                .into(ivPoster);

    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);

        if (getShouldGoToServer()) {
            App.getController().getMovieImages(movie.id);
        }
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MoviePosterEvent event) {
        if (!event.isSuccess){
            Toast.makeText(this, event.errorMsg, Toast.LENGTH_SHORT).show();
            tvCast.setVisibility(View.GONE);
            return;
        }

        if (event.cast != null && event.cast.size() > 0){
            StringBuilder builder = new StringBuilder("Csst : \n");
            for (MovieCreditResponse.CastMan man : event.cast){
                builder.append(man.name).append(" as ").append(man.character).append("\n");
            }

            tvCast.setText(builder.toString());
        } else {
            tvCast.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.noanim, R.anim.activity_transition_out);
    }
}
