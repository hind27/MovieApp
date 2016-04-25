package com.example.hend.Themovieapp.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

	// Table Name

	public static final String FAV_MOVIES_TABLE_NAME = "fav_movies";


	// Table columns

	public static final String FAV_MOVIE_AUTO_ID = "_id";

	public static final String FAV_MOVIE_ID = "movie_id";
 	public static final String FAV_MOVIE_TITLE = "title";
	public static final String FAV_MOVIE_POSTER_PATH= "poster_path";
	public static final String FAV_MOVIE_BACKDROP_PATH = "backdrop_path";
	public static final String FAV_MOVIE_OVERVIEW = "overview";
	public static final String FAV_MOVIE_RATING = "vote_average";
	public static final String FAV_MOVIE_DATE = "date";

	// Database Information
	static final String DB_NAME = "udacitymovies.DB";

	// database version
	static final int DB_VERSION = 1;

 	private static final String CREATE_FAV_FILMS_TABLE = "create table " + FAV_MOVIES_TABLE_NAME +
			"("
			+ FAV_MOVIE_AUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ FAV_MOVIE_ID + " TEXT NOT NULL, "
			+ FAV_MOVIE_TITLE + " TEXT NOT NULL, " + FAV_MOVIE_POSTER_PATH + " TEXT NOT NULL, "
			+FAV_MOVIE_BACKDROP_PATH + " TEXT NOT NULL, " +FAV_MOVIE_OVERVIEW+ " TEXT NOT NULL, "
			+FAV_MOVIE_RATING + " TEXT NOT NULL, " + FAV_MOVIE_DATE + " TEXT NOT NULL "
			+");";



	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_FAV_FILMS_TABLE);

 	}
	
	

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + FAV_MOVIES_TABLE_NAME);
 		onCreate(db);
	}
}