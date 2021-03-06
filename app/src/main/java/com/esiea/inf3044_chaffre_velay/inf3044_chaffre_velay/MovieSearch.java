package com.esiea.inf3044_chaffre_velay.inf3044_chaffre_velay;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MovieSearch extends IntentService {
    public static final String GET_MOVIE = "com.esiea.inf3044_chaffre_velay.inf3044_chaffre_velay.action.getMovieInfo";
    public static final String GET_HISTORY = "com.esiea.inf3044_chaffre_velay.inf3044_chaffre_velay.action.getHistory";
    private static final String TAG = "Movie search";

    private static final String EXTRA_PARAM1 = "com.esiea.inf3044_chaffre_velay.inf3044_chaffre_velay.extra.PARAM1";

    private DBHelper dbh;

    public MovieSearch() {
        super("MovieSearch");
        dbh = new DBHelper(this);

    }


    public static void startActionSearchMovie(Context context, String param1) {
        Intent intent = new Intent(context, MovieSearch.class);
        intent.setAction(GET_MOVIE);
        intent.putExtra(EXTRA_PARAM1, param1);
        context.startService(intent);
    }


    public static void startActionSearchHistory(Context context) {
        Intent intent = new Intent(context, MovieSearch.class);
        intent.setAction(GET_HISTORY);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (GET_MOVIE.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                handleActionSearchMovie(param1);
            } else if (GET_HISTORY.equals(action)) {
                handleActionHistory();
            }
        }
    }

    private void handleActionSearchMovie(String param1) {
        Log.d(TAG, "Thread service name: " + Thread.currentThread().getName());
        URL url;
        String tmpURL = "http://www.omdbapi.com/?t="+param1+"&y=&plot=full&r=json";
        Log.d(TAG, tmpURL);
        try {
            url = new URL(tmpURL);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            if(HttpURLConnection.HTTP_OK == conn.getResponseCode()){
                copyInputStreamToFile(conn.getInputStream(), new File(getCacheDir(), "movies.json"));
                Log.d(TAG, "successful query");
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(GET_MOVIE));
            } else {
                Log.e(TAG, "CONNECTION ERROR" + conn.getResponseCode());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleActionHistory() {

        List<String> movieList = dbh.getAllMovies();
        Set<String> hs = new HashSet<>();
        hs.addAll(movieList);
        movieList.clear();
        movieList.addAll(hs);
        Log.d(TAG, movieList.toString());

    for(String movie : movieList) {
        Log.d(TAG, "movie: " + movie);
            Log.d(TAG, "Thread service name: " + Thread.currentThread().getName());
            URL url;
            String tmpURL = "http://www.omdbapi.com/?i="+movie+"&plot=full&r=json";
            Log.d(TAG, tmpURL);
            try {
                url = new URL(tmpURL);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                if(HttpURLConnection.HTTP_OK == conn.getResponseCode()){
                    copyInputStreamToFile(conn.getInputStream(), new File(getCacheDir(), "movies.json"));
                    Log.d(TAG, "successful query");
                    LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(GET_HISTORY));
                } else {
                    Log.e(TAG, "CONNECTION ERROR" + conn.getResponseCode());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    private void copyInputStreamToFile(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len = in.read(buf)) > 0) {
                out.write(buf,0,len);
            }
            out.close();
            in.close();
            Log.d(TAG, "Finished loading this file");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
