package com.rodzik.kamil.popularmovies.view.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rodzik.kamil.popularmovies.R;
import com.rodzik.kamil.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailFragment extends Fragment {
    @BindView(R.id.poster_image_view)
    ImageView mMoviePoster;

    @BindView(R.id.title)
    TextView mMovieTitle;

    @BindView(R.id.date)
    TextView mMovieDate;

    @BindView(R.id.rating)
    TextView mMovieRating;

    @BindView(R.id.overview)
    TextView mMovieOverview;

    private Movie mMovie;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        ButterKnife.bind(this, view);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mMovie = bundle.getParcelable("movie");
        }

        setPoster();
        setMovieData();

        return view;
    }

    private void setPoster() {
        String imageUrl = "http://image.tmdb.org/t/p/w185/" + mMovie.posterPath;

        Picasso.with(getActivity()).load(imageUrl).
                into(mMoviePoster);
    }

    private void setMovieData() {
        mMovieTitle.setText(mMovie.originalTitle);
        mMovieDate.setText(mMovie.releaseDate);
        mMovieRating.setText(Double.toString(mMovie.voteAverage));
        mMovieOverview.setText(mMovie.overview);
    }
}
