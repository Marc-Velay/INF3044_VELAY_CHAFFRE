package com.esiea.inf3044_chaffre_velay.inf3044_chaffre_velay;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "History.db";
    public static final String TABLE_NAME = "movies";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_IMDBID = "imdbID";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table movies " +
                        "(id integer primary key, name text, imdbID text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS movies");
        onCreate(db);
    }

    public boolean insertMovie (String name, String imdbID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("imdbID", imdbID);
        db.insert("movies", null, contentValues);
        return true;
    }


    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public ArrayList<String> getAllMovies() {
        ArrayList<String> movies = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from movies", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            movies.add(res.getString(res.getColumnIndex(COLUMN_IMDBID)));
            res.moveToNext();
        }
        return movies;
    }

}
