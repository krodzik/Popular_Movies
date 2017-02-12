package com.rodzik.kamil.popularmovies.view.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rodzik.kamil.popularmovies.BuildConfig;
import com.rodzik.kamil.popularmovies.R;
import com.rodzik.kamil.popularmovies.Sort;
import com.rodzik.kamil.popularmovies.adapter.MovieGridAdapter;
import com.rodzik.kamil.popularmovies.model.Movie;
import com.rodzik.kamil.popularmovies.model.MovieResults;
import com.rodzik.kamil.popularmovies.network.TheMovieDbService;
import com.rodzik.kamil.popularmovies.utils.ApiUtils;
import com.rodzik.kamil.popularmovies.utils.SpacesItemDecoration;
import com.rodzik.kamil.popularmovies.view.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.rodzik.kamil.popularmovies.utils.Utility.calculateNoOfColumns;
import static com.rodzik.kamil.popularmovies.utils.Utility.isOnline;

public class MainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        MovieGridAdapter.FragmentCallbacks {
    @BindView(R.id.movies_grid)
    RecyclerView mMoviesGridView;

    @BindView(R.id.movies_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    @BindView(R.id.no_connection_view)
    LinearLayout mNoInternetView;

    private Sort mSortType = Sort.BY_POPULARITY;
    private List<Movie> mMovieList;
    private int mPage;
    private int mTotalPages;
    private MovieGridAdapter mMovieAdapter;
    private MainActivity mActivity;
    private TheMovieDbService mMovieService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mMovieService = ApiUtils.getTheMovieDbService();
        mMovieList = new ArrayList<>();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        int progressViewOffsetStart = 100;
        int progressViewOffsetEnd = 400;

        mSwipeRefresh.setColorSchemeResources(R.color.colorPrimaryDark);
        mSwipeRefresh.setProgressViewOffset(true, progressViewOffsetStart, progressViewOffsetEnd);

        final GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), calculateNoOfColumns(getContext()));

        mMoviesGridView.setLayoutManager(mLayoutManager);
        mMoviesGridView.setHasFixedSize(true);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        mMoviesGridView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        mMovieAdapter = new MovieGridAdapter(mMovieList, (MovieGridAdapter.ActivityCallbacks) getActivity(), this);

        mMoviesGridView.setAdapter(mMovieAdapter);
        mSwipeRefresh.setOnRefreshListener(this);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result;

        switch (item.getItemId()) {
            case R.id.sort_by_popularity:
                mSortType = Sort.BY_POPULARITY;
                result = true;
                break;
            case R.id.sort_by_rates:
                mSortType = Sort.BY_RATES;
                result = true;
                break;
            default:
                mSortType = Sort.BY_POPULARITY;
                result = super.onOptionsItemSelected(item);
                break;
        }

        item.setChecked(true);
        mPage = 1;
        mSwipeRefresh.setRefreshing(true);
        downloadMovies();
        return result;
    }

    private void downloadMovies() {
        if (!isOnline(mActivity)) {
            showNoInternetScreen(true);
            return;
        }
        showNoInternetScreen(false);

        if (mSortType == Sort.BY_RATES) {
            mMovieService.getMoviesSortByRates(BuildConfig.MOVIE_DB_API_KEY, mPage)
                    .enqueue(new Callback<MovieResults>() {
                @Override
                public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                    if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                        // Error handling
                        return;
                    }
                    mMovieList = response.body().results;
                    mTotalPages = response.body().totalPages;
                    setDownloadedMovies();
                }

                @Override
                public void onFailure(Call<MovieResults> call, Throwable t) {
                    //Network error handling
                    showNoInternetScreen(true);
                }
            });
        } else {
            mMovieService.getMoviesSortByPopularity(BuildConfig.MOVIE_DB_API_KEY, mPage)
                    .enqueue(new Callback<MovieResults>() {
                @Override
                public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                    if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                        // Error handling
                        return;
                    }
                    mMovieList = response.body().results;
                    mTotalPages = response.body().totalPages;
                    setDownloadedMovies();
                }

                @Override
                public void onFailure(Call<MovieResults> call, Throwable t) {
                    //Network error handling
                    showNoInternetScreen(true);
                }
            });
        }
    }

    private void showNoInternetScreen(boolean visible) {
        mNoInternetView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void setDownloadedMovies() {
        if (mPage > 1) {
            mMovieAdapter.addMovies(mMovieList);
        } else {
            mMovieAdapter.setMovies(mMovieList);
        }
        mSwipeRefresh.setRefreshing(false);
    }

    @Override
    public void onLoadNextPage() {
        if (mPage >= mTotalPages) {
            return;
        }
        mPage++;
        mSwipeRefresh.setRefreshing(true);
        downloadMovies();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPage = 1;
        mSwipeRefresh.setRefreshing(true);
        downloadMovies();
    }

    @Override
    public void onRefresh() {
        downloadMovies();
    }
}
