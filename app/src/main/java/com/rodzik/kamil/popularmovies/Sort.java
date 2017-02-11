package com.rodzik.kamil.popularmovies;


public enum Sort {
    BY_POPULARITY("popularity"),
    BY_RATES("rates");

    private final String value;

    Sort(String value) {
        this.value = value;
    }

    @Override public String toString() {
        return value;
    }
}
