package com.esiea.inf3044_chaffre_velay.inf3044_chaffre_velay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

public class MovieSearchActivity extends AppCompatActivity {

    private static final String TAG = "Movie search ACTIVITY";
    IntentFilter intentFilter ;
    MovieAdapter ba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_search);


        Bundle b = getIntent().getExtras();
        String value = ""; // or other values
        if(b != null) value = b.getString("searchString");
        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();

        intentFilter = new IntentFilter(MovieSearch.GET_MOVIE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new MovieUpdate(), intentFilter);

        String[] parts = value.split(" ");
        for(int i = 0; i < parts.length; i++) {
            if(i==0){
                value = parts[i];
            } else {
                value+="+"+parts[i];
            }
        }
        MovieSearch.startActionSearchMovie(this, value);


        RecyclerView rv_movieResult = (RecyclerView)findViewById(R.id.rv_movieResult);
        rv_movieResult.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ba = new MovieAdapter(getMOVIEFromFile());
        rv_movieResult.setAdapter(ba);
    }



    public class MovieUpdate extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, intent.getAction());
            Toast.makeText(getApplicationContext(), "Downloaded", Toast.LENGTH_SHORT).show();
            ba.setNewMovie(getMOVIEFromFile());
        }
    }

    private class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {

        private JSONArray movies;

        public MovieAdapter(JSONArray movies) {
            this.movies = movies;
        }

        @Override
        public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            MovieHolder bh = new MovieHolder(inflater.inflate(R.layout.rv_movie_element, parent, false));
            return bh;
        }

        @Override
        public void onBindViewHolder(MovieHolder holder, int position) {
            try {
                holder.name.setText(movies.getJSONObject(position).get("Year").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {

            return movies.length();
        }

        public class MovieHolder extends RecyclerView.ViewHolder {
            TextView name;

            public MovieHolder(View itemView) {
                super(itemView);
                name = (TextView)itemView.findViewById(R.id.rv_movie_element_name);
            }
        }

        public void setNewMovie(JSONArray movies) {
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

            JSONArray jsonArray = new JSONArray();
            jsonArray.put(jsonObject);
            Log.d(TAG, jsonArray.toString());
            return jsonArray;//new JSONArray(new String(buf, "UTF-8"));
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
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.search_history) {
            Toast.makeText(getApplicationContext(), getString(R.string.search_history), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, SearchHistory.class));
            finish();
        } else if (id == R.id.menu_main) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
