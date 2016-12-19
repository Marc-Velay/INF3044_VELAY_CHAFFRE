package com.esiea.inf3044_chaffre_velay.inf3044_chaffre_velay;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.database.sqlite.*;
//import android.database.*;

public class MainActivity extends AppCompatActivity {

    EditText movieNameField;
    public static final String MOVIE_SEARCH = "com.esiea.inf3044_chaffre_velay.inf3044_chaffre_velay";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void movieSearch(View v) {
        Intent intent = new Intent(this, MovieSearchActivity.class);

        Bundle b = new Bundle();
        movieNameField = (EditText)findViewById(R.id.movieName);
        String name = movieNameField.getText().toString();
        b.putString("searchString", name);
        intent.putExtras(b);

        startActivity(intent);
        finish();
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
            startActivity(new Intent(this, SearchHistory.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}


