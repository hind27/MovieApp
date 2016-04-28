package com.example.hend.Themovieapp.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.hend.Themovieapp.Activities.MainActivity;
import com.example.hend.Themovieapp.Activities.MoviedetailsActivity;
import com.example.hend.Themovieapp.Adapters.GridViewAdapter;
import com.example.hend.Themovieapp.Models.Film;
import com.example.hend.Themovieapp.R;
import com.example.hend.Themovieapp.Utilities.HTTPDataHandler;
import com.example.hend.Themovieapp.sqlite.DBManager;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainFragment extends Fragment {

    private static String urlPopularFilms = "http://api.themoviedb.org/3/movie/popular?api_key=" + getString(R.string.tmdb_api_key);
    private static String urlTopRatedFilms = "https://api.themoviedb.org/3/movie/top_rated?api_key" + getString(R.string.tmdb_api_key);


    //http://api.themoviedb.org/3/movie/209112/videos?api_key=
    public int current_view=1;

    GridView gv ;
    public ArrayList<Film> most_popular_films = new ArrayList<Film>();
    public ArrayList<Film> top_rated_films = new ArrayList<Film>();
    public ArrayList<Film> My_favorite_Movies = new ArrayList<Film>();


    GridViewAdapter gridViewAdapter ;


    DBManager dbManager ;


    View view ;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_main, container, false);

        current_view=1;
        most_popular_films = new ArrayList<Film>();
        top_rated_films = new ArrayList<Film>();

        gv = (GridView) view.findViewById(R.id.gridview_movies);


        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Film selected_film = new Film();
                switch (current_view)
                {
                    case 1 :
                        selected_film=most_popular_films.get(position);

                        break;

                    case  2 :
                        selected_film=top_rated_films.get(position);

                        break;


                    case  3 :
                        selected_film=My_favorite_Movies.get(position);



                        break;

                }

                if (((MainActivity)(getActivity())).isTablet)
                {
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(DetailsFragment.FILM_DETAILS, selected_film);

                    DetailsFragment fragment = new DetailsFragment();
                    fragment.setArguments(arguments);

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.movie_detail_container, fragment, DetailsFragment.TAG)
                            .commit();
                } else {
                    Intent intent = new Intent(getActivity(), MoviedetailsActivity.class)
                            .putExtra(DetailsFragment.FILM_DETAILS, selected_film);
                    startActivity(intent);
                }

            }
        });
        new FetchAsyncTask().execute(urlPopularFilms);


        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if(current_view==3)
        {
            dbManager =  new DBManager(getActivity().getApplicationContext());
            My_favorite_Movies =  dbManager.getAllFav();

            gridViewAdapter = new GridViewAdapter(getActivity(),My_favorite_Movies);
            gv.setAdapter(gridViewAdapter);

        }
    }

    public void parseJsonUpdateUI(String result)
    {

        most_popular_films.clear();
        top_rated_films.clear();
        My_favorite_Movies.clear();

        try {
            JSONObject jsonObject=new JSONObject(result);
            JSONArray MovieArray = jsonObject.getJSONArray("results");

            for (int i = 0; i <MovieArray.length() ; i++) {
                JSONObject jsonObjectfilm=MovieArray.getJSONObject(i);

                Film film = new Film();

                film.setId(jsonObjectfilm.getString("id"));
                film.setOriginal_title(jsonObjectfilm.getString("original_title"));
                film.setOverview(jsonObjectfilm.getString("overview"));
                film.setPoster_path(jsonObjectfilm.getString("poster_path"));
                film.setTitle(jsonObjectfilm.getString("title"));
                film.setBackdrop_path(jsonObjectfilm.getString("backdrop_path"));
                film.setRelease_date(jsonObjectfilm.getString("release_date"));
                film.setVote_average(jsonObjectfilm.getString("vote_average"));



                switch (current_view)
                {
                    case 1:
                        most_popular_films.add(film);
                        break;
                    case 2:
                        top_rated_films.add(film);
                        break;


                }


            }


            switch (current_view)
            {



                case 1:
                    gridViewAdapter = new GridViewAdapter(getActivity(),most_popular_films);
                    gv.setAdapter(gridViewAdapter);

                    break;
                case 2:
                    gridViewAdapter = new GridViewAdapter(getActivity(),top_rated_films);
                    gv.setAdapter(gridViewAdapter);



            }

        }
        catch (Exception e)
        {


        }


    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);

        MenuItem action_sort_by_popularity = menu.findItem(R.id.Most_popular);
        MenuItem action_sort_by_rating = menu.findItem(R.id.Top_Rated);
        MenuItem action_sort_by_favorite = menu.findItem(R.id.MyfavoriteMovies);

        switch (current_view)
        {
            case 1 :
                if (!action_sort_by_popularity.isChecked()) {
                    action_sort_by_popularity.setChecked(true);
                }
                break;

            case  2 :
                if (!action_sort_by_rating.isChecked()) {
                    action_sort_by_rating.setChecked(true);
                }
                break;


            case  3 :
                if (!action_sort_by_popularity.isChecked()) {
                    action_sort_by_favorite.setChecked(true);
                }


                break;

        }





    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.Most_popular:
                current_view = 1;
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                updateMovies();
                return true;
            case R.id.Top_Rated:
                current_view = 2;
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                updateMovies();
                return true;
            case R.id.MyfavoriteMovies:
                current_view = 3;
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                updateMovies();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateMovies() {
        switch (current_view)
        {
            case 1 :
                new FetchAsyncTask().execute(urlPopularFilms);

                break;

            case  2 :
                new FetchAsyncTask().execute(urlTopRatedFilms);

                break;


            case  3 :
                dbManager =  new DBManager(getActivity().getApplicationContext());
                My_favorite_Movies =  dbManager.getAllFav();

                gridViewAdapter = new GridViewAdapter(getActivity(),My_favorite_Movies);
                gv.setAdapter(gridViewAdapter);

                break;

        }

    }



    public class FetchAsyncTask extends AsyncTask<String, Void, String > {
        ProgressDialog progressDialog ; //loading "please wait "
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=ProgressDialog.show(getActivity(),"","please wait",true);
        }
        @Override
        protected String  doInBackground(String... strings) {
            String stream = null;
            String urlString = strings[0];

            HTTPDataHandler hh = new HTTPDataHandler();
            stream = hh.GetHTTPData(urlString);
            // Return the data from specified url
            return stream;

        }

        @Override
        protected void onPostExecute(String result) {

            progressDialog.dismiss();

            parseJsonUpdateUI(result);
        }
    }

}
