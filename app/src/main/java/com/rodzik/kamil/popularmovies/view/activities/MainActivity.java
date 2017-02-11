package com.rodzik.kamil.popularmovies.view.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.rodzik.kamil.popularmovies.R;
import com.rodzik.kamil.popularmovies.adapter.MovieGridAdapter;
import com.rodzik.kamil.popularmovies.model.Movie;
import com.rodzik.kamil.popularmovies.view.fragments.MainFragment;
import com.rodzik.kamil.popularmovies.view.fragments.MovieDetailFragment;

import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MovieGridAdapter.ActivityCallbacks {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setToolbar(false, true);

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            MainFragment mainFragment = new MainFragment();
            mainFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, mainFragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                setToolbar(false, true);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieClick(Movie movie) {
        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("movie", movie);
        movieDetailFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, movieDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        setToolbar(true, true);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setToolbar(false, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
