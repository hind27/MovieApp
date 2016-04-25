package com.example.hend.Themovieapp.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.hend.Themovieapp.Fragments.DetailsFragment;
import com.example.hend.Themovieapp.R;



public class MoviedetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();

            arguments.putParcelable(DetailsFragment.FILM_DETAILS,
                    getIntent().getParcelableExtra(DetailsFragment.FILM_DETAILS));

            DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();
        }
    }
}
