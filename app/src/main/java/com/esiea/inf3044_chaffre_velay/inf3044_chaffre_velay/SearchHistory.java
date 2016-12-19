package com.esiea.inf3044_chaffre_velay.inf3044_chaffre_velay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SearchHistory extends AppCompatActivity {

    private JSONArray jsonArray;
    private static final String TAG = "Movie search ACTIVITY";
    IntentFilter intentFilter ;
    MovieAdapterH ba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_history);

        intentFilter = new IntentFilter(MovieSearch.GET_HISTORY);
        LocalBroadcastManager.getInstance(this).registerReceiver(new MovieUpdateH(), intentFilter);

        jsonArray = new JSONArray();

        MovieSearch.startActionSearchHistory(this);


        RecyclerView rv_movieResultH = (RecyclerView)findViewById(R.id.rv_movieResultH);
        rv_movieResultH.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ba = new MovieAdapterH(getMOVIEFromFile());
        rv_movieResultH.setAdapter(ba);
    }


    public class MovieUpdateH extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, intent.getAction());
            ba.setNewMovieH(getMOVIEFromFile());
        }
    }

    private class MovieAdapterH extends RecyclerView.Adapter<MovieAdapterH.MovieHolderH> {

        private JSONArray movies;

        public MovieAdapterH(JSONArray movies) {
            this.movies = movies;
        }

        @Override
        public MovieHolderH onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            MovieHolderH bh = new MovieHolderH(inflater.inflate(R.layout.rv_movie_element, parent, false));
            return bh;
        }

        @Override
        public void onBindViewHolder(MovieHolderH holder, int position) {
            try {
                holder.name.setText(movies.getJSONObject(position).get("Title").toString());
                holder.year.setText(movies.getJSONObject(position).get("Year").toString());
                holder.runtime.setText(movies.getJSONObject(position).get("Runtime").toString());
                holder.genre.setText(movies.getJSONObject(position).get("Genre").toString());
                holder.plot.setText(movies.getJSONObject(position).get("Plot").toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {

            return movies.length();
        }

        public class MovieHolderH extends RecyclerView.ViewHolder {
            TextView name;
            TextView year;
            TextView runtime;
            TextView genre;
            TextView plot;


            public MovieHolderH(View itemView) {
                super(itemView);
                name = (TextView)itemView.findViewById(R.id.rv_movie_element_name);
                year = (TextView)itemView.findViewById(R.id.rv_movie_element_year);
                runtime = (TextView)itemView.findViewById(R.id.rv_movie_element_runtime);
                genre = (TextView)itemView.findViewById(R.id.rv_movie_element_genre);
                plot = (TextView)itemView.findViewById(R.id.rv_movie_element_plot);
            }
        }

        public void setNewMovieH(JSONArray movies) {
            this.movies = movies;
            notifyDataSetChanged();
        }
    }

    public JSONArray getMOVIEFromFile() {
        try{
            InputStream in = new FileInputStream(getCacheDir() + "/" + "movies.json");
            byte[] buf = new byte[in.available()];
            in.read(buf);
            in.close();
            Log.d(TAG, new String(buf, "UTF-8"));

            JSONObject jsonObject = new JSONObject(new String(buf, "UTF-8"));

            jsonArray.put(jsonObject);
            Log.d(TAG, "jsonArray " + jsonArray.toString());
            return jsonArray;
        } catch(IOException e) {
            e.printStackTrace();
            return new JSONArray();
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.history_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_main) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
