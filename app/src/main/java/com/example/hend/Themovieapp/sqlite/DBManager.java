package com.example.hend.Themovieapp.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


import com.example.hend.Themovieapp.Models.Film;

import java.util.ArrayList;

public class DBManager {

	private DatabaseHelper dbHelper;
	private Context context;
	private SQLiteDatabase database;

	public DBManager(Context c) {
		context = c;
        open_db();
	}

	public DBManager open_db() throws SQLException {
		dbHelper = new DatabaseHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	public void fav_films_insert
			(
			   String movie_id , String title, String poster_path , String backdrop_path ,
				String overview,String vote_average , String date
	)


	{

		ContentValues contentValue = new ContentValues();


		contentValue.put(DatabaseHelper.FAV_MOVIE_ID, movie_id);
		contentValue.put(DatabaseHelper.FAV_MOVIE_TITLE, title);
		contentValue.put(DatabaseHelper.FAV_MOVIE_POSTER_PATH, poster_path);
		contentValue.put(DatabaseHelper.FAV_MOVIE_BACKDROP_PATH, backdrop_path);
		contentValue.put(DatabaseHelper.FAV_MOVIE_OVERVIEW, overview);
		contentValue.put(DatabaseHelper.FAV_MOVIE_RATING, vote_average);
		contentValue.put(DatabaseHelper.FAV_MOVIE_DATE, date);

		database.insert(DatabaseHelper.FAV_MOVIES_TABLE_NAME, null, contentValue);

		close();

	}




	public ArrayList<Film> getAllFav() {
		ArrayList<Film> films = new ArrayList<Film>();


		Cursor cursor = database.query(DatabaseHelper.FAV_MOVIES_TABLE_NAME, null, null, null, null, null, null);
		if (cursor != null)
		{
			// looping through all rows and adding to list
			while (cursor.moveToNext())
			{
				Film film = new Film();

				film.setId(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FAV_MOVIE_ID)));
				film.setOriginal_title(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FAV_MOVIE_TITLE)));
				film.setPoster_path(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FAV_MOVIE_POSTER_PATH)));
				film.setBackdrop_path(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FAV_MOVIE_BACKDROP_PATH)));
				film.setOverview(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FAV_MOVIE_OVERVIEW)));
				film.setVote_average(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FAV_MOVIE_RATING)));
				film.setRelease_date(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FAV_MOVIE_DATE)));
				film.setTitle(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FAV_MOVIE_TITLE)));



				films.add(film);
			}

			cursor.close();
		}
		close();
		return films;
	}





	public void fav_films_delete(String fav_movie_id) {
		database.delete(DatabaseHelper.FAV_MOVIES_TABLE_NAME, DatabaseHelper.FAV_MOVIE_ID + " = '" + fav_movie_id + "'", null);
		close();
	}


	public int isFavorited(String fav_movie_id)
	{
		String countQuery = "SELECT  * FROM " + DatabaseHelper.FAV_MOVIES_TABLE_NAME + " WHERE "
				+DatabaseHelper.FAV_MOVIE_ID +" = '" + fav_movie_id + "'";
		Cursor cursor = database.rawQuery(countQuery, null);
		int cnt = cursor.getCount();
		cursor.close();
		close();
		return cnt;

	}



}
