package com.rodzik.kamil.popularmovies.view.activities;


import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.rodzik.kamil.popularmovies.R;

import butterknife.BindView;


public class BaseActivity extends AppCompatActivity {
    @Nullable
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    protected void setToolbar(boolean showHomeUp, boolean showTitle) {
        setToolbar(mToolbar, showHomeUp, showTitle);
    }

    public void setToolbar(Toolbar mToolbar, boolean showHomeUp, boolean showTitle) {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(showHomeUp);
            getSupportActionBar().setDisplayShowTitleEnabled(showTitle);
        }
    }
}
