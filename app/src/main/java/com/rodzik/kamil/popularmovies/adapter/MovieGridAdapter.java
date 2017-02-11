package com.rodzik.kamil.popularmovies.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.rodzik.kamil.popularmovies.R;
import com.rodzik.kamil.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieGridAdapter extends RecyclerView.Adapter<MovieGridAdapter.ViewHolder> {

    public interface ActivityCallbacks {
        void onMovieClick(Movie movie);
    }

    public interface FragmentCallbacks {
        void onLoadNextPage();
    }

    private List<Movie> mMovieList = new ArrayList<>();
    private ActivityCallbacks mActivityCallbacks;
    private FragmentCallbacks mFragmentCallbacks;

    public MovieGridAdapter(List<Movie> movieList, ActivityCallbacks activityCallbacks, FragmentCallbacks fragmentCallbacks) {
        mMovieList = movieList;
        mActivityCallbacks = activityCallbacks;
        mFragmentCallbacks = fragmentCallbacks;
    }

    @Override
    public MovieGridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MovieGridAdapter.ViewHolder holder, final int position) {
        // Load next page when user look at last element
        if (position == mMovieList.size()-1) {
            mFragmentCallbacks.onLoadNextPage();
        }

        final Movie movie = mMovieList.get(position);
        holder.mGridItemContainer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mActivityCallbacks != null) {
                    mActivityCallbacks.onMovieClick(movie);
                }
            }
        });

        String imageUrl = "http://image.tmdb.org/t/p/w185/" + movie.posterPath;

        Picasso.with(holder.mPosterImageView.getContext()).load(imageUrl).
                into(holder.mPosterImageView);
    }

    @Override
    public int getItemCount() {
        return mMovieList == null ? 0 : mMovieList.size();
    }

    public void setMovies(List<Movie> movies) {
        if (movies == null) {
            return;
        }
        mMovieList = movies;
        notifyDataSetChanged();
    }

    public void addMovies(List<Movie> movies) {
        if (movies == null) {
            return;
        }
        mMovieList.addAll(movies);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.poster_image_view)
        ImageView mPosterImageView;

        @BindView(R.id.grid_item_container)
        FrameLayout mGridItemContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
