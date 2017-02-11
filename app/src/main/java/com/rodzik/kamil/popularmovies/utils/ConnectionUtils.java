package com.rodzik.kamil.popularmovies.utils;


import android.content.Context;
import android.net.ConnectivityManager;

public class ConnectionUtils {

    public static boolean isOnline(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return(cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected());
    }
}
