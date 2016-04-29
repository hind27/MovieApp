package com.example.hend.Themovieapp.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hend.Themovieapp.Adapters.ReviewAdapter;
import com.example.hend.Themovieapp.Adapters.TrailerAdapter;
import com.example.hend.Themovieapp.Models.Film;
import com.example.hend.Themovieapp.Models.Review;
import com.example.hend.Themovieapp.Models.Trailer;
import com.example.hend.Themovieapp.R;

import com.example.hend.Themovieapp.sqlite.DBManager;

import com.linearlistview.LinearListView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DetailsFragment extends Fragment {
    public static final String TAG = DetailsFragment.class.getSimpleName();
    public static final String FILM_DETAILS = "film_details";


    private Film film;

    private ImageView mImageView;

    private TextView mTitleView;
    private TextView mOverviewView;
    private TextView mDateView;
    private TextView mVoteAverageView;


    private LinearListView mTrailersView;
    private LinearListView mReviewsView;


    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;

    private ScrollView mDetailLayout;

    private Toast mToast;


      DBManager dbManager ;


    public ArrayList<Trailer> trailerArrayList = new ArrayList<Trailer>();
    public ArrayList<Review> reviewArrayList = new ArrayList<Review>();


    public DetailsFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (film != null) {
            inflater.inflate(R.menu.menu_fragment_detail, menu);

            final MenuItem action_favorite = menu.findItem(R.id.action_favorite);
             /*
            action_favorite.setIcon(Utility.isFavorited(getActivity(), mMovie.getId()) == 1 ?
                    R.drawable.abc_btn_rating_star_on_mtrl_alpha :
                    R.drawable.abc_btn_rating_star_off_mtrl_alpha);
            */
            new AsyncTask<Void, Void, Integer>() {
                @Override
                protected Integer doInBackground(Void... params) {
                    dbManager =  new DBManager(getActivity().getApplicationContext());
                    return dbManager.isFavorited(film.getId());
                }

                @Override
                protected void onPostExecute(Integer isFavorited) {
                    action_favorite.setIcon(isFavorited == 1 ?
                            R.drawable.abc_btn_rating_star_on_mtrl_alpha :
                            R.drawable.abc_btn_rating_star_off_mtrl_alpha);
                }
            }.execute();


        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_favorite:
                if (film != null) {
                    // check if movie is in favorites or not
                    new AsyncTask<Void, Void, Integer>() {

                        @Override
                        protected Integer doInBackground(Void... params) {
                            dbManager =  new DBManager(getActivity().getApplicationContext());
                            return dbManager.isFavorited(film.getId());
                        }

                        @Override
                        protected void onPostExecute(Integer isFavorited) {
                            // if it is in favorites
                            if (isFavorited == 1) {
                                // delete from favorites
                                new AsyncTask<Void, Void, Void>() {
                                    @Override
                                    protected Void doInBackground(Void... params) {

                                        dbManager =  new DBManager(getActivity().getApplicationContext());
                                          dbManager.fav_films_delete(film.getId());
                                    
                                    if (MainActivity.isTablet)
                                        {
                                            MainFragment mainFragment = (MainFragment)getActivity().getSupportFragmentManager().findFragmentByTag(getString(R.string.mainfragmenttag));
                                            mainFragment.updateFragment();
                                        }
                                        
                                        return null;

                                    }

                                    @Override
                                    protected void onPostExecute(Void noparams) {
                                        item.setIcon(R.drawable.abc_btn_rating_star_off_mtrl_alpha);
                                        if (mToast != null) {
                                            mToast.cancel();
                                        }
                                        mToast = Toast.makeText(getActivity(), getString(R.string.removed_from_favorites), Toast.LENGTH_SHORT);
                                        mToast.show();
                                    }
                                }.execute();
                            }
                            // if it is not in favorites
                            else {
                                // add to favorites
                                new AsyncTask<Void, Void, Void>() {
                                    @Override
                                    protected Void doInBackground(Void... params) {
                                        dbManager =  new DBManager(getActivity().getApplicationContext());
                                        dbManager.fav_films_insert(film.getId(),film.getOriginal_title()
                                        ,film.getPoster_path(),film.getBackdrop_path()
                                        ,film.getOverview(),film.getVote_average()
                                        ,film.getRelease_date());

                                      if (MainActivity.isTablet)
                                        {
                                            MainFragment mainFragment = (MainFragment)getActivity().getSupportFragmentManager().findFragmentByTag(getString(R.string.mainfragmenttag));
                                            mainFragment.updateFragment();
                                        }
                                        
                                        return null ;
                                    }

                                    @Override
                                    protected void onPostExecute(Void noparam) {
                                        item.setIcon(R.drawable.abc_btn_rating_star_on_mtrl_alpha);
                                        if (mToast != null) {
                                            mToast.cancel();
                                        }
                                        mToast = Toast.makeText(getActivity(), getString(R.string.added_to_favorites), Toast.LENGTH_SHORT);
                                        mToast.show();
                                    }
                                }.execute();
                            }
                        }
                    }.execute();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            film = arguments.getParcelable(DetailsFragment.FILM_DETAILS);
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);


       trailerArrayList = new ArrayList<Trailer>();
      reviewArrayList = new ArrayList<Review>();


        mDetailLayout = (ScrollView) rootView.findViewById(R.id.detail_layout);

        if (film != null) {
            mDetailLayout.setVisibility(View.VISIBLE);
        } else {
            mDetailLayout.setVisibility(View.INVISIBLE);
        }

        mImageView = (ImageView) rootView.findViewById(R.id.detail_image);
        mTitleView = (TextView) rootView.findViewById(R.id.detail_title);
        mOverviewView = (TextView) rootView.findViewById(R.id.detail_overview);
        mDateView = (TextView) rootView.findViewById(R.id.detail_date);
        mVoteAverageView = (TextView) rootView.findViewById(R.id.detail_vote_average);
        mTrailersView = (LinearListView) rootView.findViewById(R.id.detail_trailers);
        mReviewsView = (LinearListView) rootView.findViewById(R.id.detail_reviews);
        mTrailerAdapter = new TrailerAdapter(getActivity(), trailerArrayList);
        mTrailersView.setAdapter(mTrailerAdapter);

        mTrailersView.setOnItemClickListener(new LinearListView.OnItemClickListener() {
            @Override
            public void onItemClick(LinearListView linearListView, View view,
                                    int position, long id) {
                Trailer trailer = mTrailerAdapter.getItem(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey()));
                startActivity(intent);
            }
        });

        mReviewAdapter = new ReviewAdapter(getActivity(), reviewArrayList);
        mReviewsView.setAdapter(mReviewAdapter);

        if (film != null) {

            String image_url = buildImageUrl(342, film.getBackdrop_path());

            Picasso.with(getActivity()).load(image_url).into(mImageView);

            mTitleView.setText(film.getTitle());
            mOverviewView.setText(film.getOverview());

            String movie_date = film.getRelease_date();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
                String date = DateUtils.formatDateTime(getActivity(),
                        formatter.parse(movie_date).getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
                mDateView.setText(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            mVoteAverageView.setText(film.getVote_average());
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (film != null) {
            new FetchTrailersTask().execute(film.getId());
            new FetchReviewsTask().execute(film.getId());
        }
    }



    public class FetchTrailersTask extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = FetchTrailersTask.class.getSimpleName();

        private List<Trailer> getTrailersDataFromJson(String jsonStr) throws JSONException {
            JSONObject trailerJson = new JSONObject(jsonStr);
            JSONArray trailerArray = trailerJson.getJSONArray("results");

             trailerArrayList.clear();


            for(int i = 0; i < trailerArray.length(); i++) {
                JSONObject trailer = trailerArray.getJSONObject(i);
                // Only show Trailers which are on Youtube
                if (trailer.getString("site").contentEquals("YouTube")) {
                    Trailer trailerModel = new Trailer();

                    trailerModel.setId(trailer.getString("id"));
                    trailerModel.setKey(trailer.getString("key"));
                    trailerModel.setName(trailer.getString("name"));
                    trailerModel.setSite(trailer.getString("site"));
                    trailerModel.setType(trailer.getString("type"));
                     trailerArrayList.add(trailerModel);
                }
            }

            return trailerArrayList;
        }

        @Override
        protected Void doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String jsonStr = null;

            try {
                final String BASE_URL = "http://api.themoviedb.org/3/movie/" + params[0] + "/videos";
                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, getString(R.string.tmdb_api_key))
                        .build();

                Log.e("builtUri.toString()",builtUri.toString());
                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                jsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                 getTrailersDataFromJson(jsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(Void noparams) {
            if (trailerArrayList != null) {
                      if (mTrailerAdapter != null) {

                          try
                          {
                              mTrailerAdapter = new TrailerAdapter(getActivity(), trailerArrayList);
                              mTrailersView.setAdapter(mTrailerAdapter);
                          }
                          catch (Exception e)
                          {

                          }



                    }





            }
        }
    }

    public class FetchReviewsTask extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = FetchReviewsTask.class.getSimpleName();

        private List<Review> getReviewsDataFromJson(String jsonStr) throws JSONException {
            JSONObject reviewJson = new JSONObject(jsonStr);
            JSONArray reviewArray = reviewJson.getJSONArray("results");

            reviewArrayList.clear();



            for(int i = 0; i < reviewArray.length(); i++) {
                JSONObject review = reviewArray.getJSONObject(i);
                // Only show Trailers which are on Youtube
                     Review reviewModel = new Review();

                reviewModel.setId(review.getString("id"));
                reviewModel.setAuthor(review.getString("author"));
                reviewModel.setContent(review.getString("content"));
                reviewArrayList.add(reviewModel);

            }

            return reviewArrayList;

        }

        @Override
        protected Void doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String jsonStr = null;

            try {
                final String BASE_URL = "http://api.themoviedb.org/3/movie/" + params[0] + "/reviews";
                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, getString(R.string.tmdb_api_key))
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                jsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                  getReviewsDataFromJson(jsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(Void noparams) {
            if (reviewArrayList != null) {
                if (mReviewAdapter != null) {

                    try
                    {
                        mReviewAdapter = new ReviewAdapter(getActivity(), reviewArrayList);
                        mReviewsView.setAdapter(mReviewAdapter);
                    }
                    catch (Exception e)
                    {

                    }


                }

            }


        }
    }



    public static String buildImageUrl(int width, String fileName) {
        return "http://image.tmdb.org/t/p/w" + Integer.toString(width) + fileName;
    }

}

