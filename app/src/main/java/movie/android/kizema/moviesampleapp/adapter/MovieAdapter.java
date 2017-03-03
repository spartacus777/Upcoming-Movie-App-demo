package movie.android.kizema.moviesampleapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import movie.android.kizema.moviesampleapp.R;
import movie.android.kizema.moviesampleapp.model.Movie;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 1;
    private static final int FOOTER = 2;

    private List<Movie> movies;
    private boolean showFooter = true;

    public MovieAdapter() {}

    public void update(List<Movie> newMovies){
        if (this.movies == null) {
            this.movies = newMovies;
            notifyDataSetChanged();
        } else {
            this.movies.addAll(newMovies);
            notifyItemInserted(newMovies.size());
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder{
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class RepoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvName)
        TextView tvName;

        @BindView(R.id.tvStars)
        TextView tvStars;

        @BindView(R.id.tvDate)
        TextView tvDate;

        public RepoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (showFooter && movies == null || showFooter && position  == movies.size()){
            return FOOTER;
        }

        return ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case ITEM: {
                View parentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
                return new RepoViewHolder(parentView);
            }

            case FOOTER: {
                View parentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer, parent, false);
                return new FooterViewHolder(parentView);
            }
        }

        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)){
            case ITEM:
                Movie model = movies.get(position);
                RepoViewHolder repoHolder = (RepoViewHolder) holder;
                repoHolder.tvName.setText(model.title);
                repoHolder.tvDate.setText(model.overview);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (movies == null){
            if (showFooter){
                return 1;
            }
            return 0;
        }

        if (showFooter){
            return 1 + movies.size();
        }

        return movies.size();
    }

    public void clear(){
        movies.clear();
        notifyDataSetChanged();
    }

    public List<Movie> getItems(){
        return movies;
    }

    public void showFooter(){

        if (!showFooter) {
            if (movies == null) {
                notifyItemInserted(0);
            } else {
                notifyItemInserted(movies.size());
            }
        }

        showFooter = true;
    }

    public void hideFooter(){
        if (showFooter) {
            if (movies == null) {
                notifyItemRemoved(0);
            } else {
                notifyItemRemoved(movies.size());
            }
        }

        showFooter = false;
    }
}


