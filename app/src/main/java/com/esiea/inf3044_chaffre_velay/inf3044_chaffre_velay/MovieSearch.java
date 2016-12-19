package com.esiea.inf3044_chaffre_velay.inf3044_chaffre_velay;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MovieSearch extends IntentService {
    public static final String GET_MOVIE = "com.esiea.inf3044_chaffre_velay.inf3044_chaffre_velay.action.getMovieInfo";
    public static final String GET_HISTORY = "com.esiea.inf3044_chaffre_velay.inf3044_chaffre_velay.action.getHistory";
    private static final String TAG = "Movie search";

    private static final String EXTRA_PARAM1 = "com.esiea.inf3044_chaffre_velay.inf3044_chaffre_velay.extra.PARAM1";

    public MovieSearch() {
        super("MovieSearch");
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

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionSearchMovie(String param1) {
        Log.d(TAG, "Thread service name: " + Thread.currentThread().getName());
        URL url;
        String tmpURL = "http://www.omdbapi.com/?t="+param1+"&y=&plot=full&r=json";
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

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionHistory() {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
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
