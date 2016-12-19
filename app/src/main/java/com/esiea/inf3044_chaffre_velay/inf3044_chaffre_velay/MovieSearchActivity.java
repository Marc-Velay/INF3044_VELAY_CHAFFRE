package com.esiea.inf3044_chaffre_velay.inf3044_chaffre_velay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MovieSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_search);
        Bundle b = getIntent().getExtras();
        String value = ""; // or other values
        if(b != null) value = b.getString("searchString");
        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.search_history) {
            Toast.makeText(getApplicationContext(), getString(R.string.search_history), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
