package com.example.hend.Themovieapp.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.example.hend.Themovieapp.Fragments.DetailsFragment;
import com.example.hend.Themovieapp.R;


public class MainActivity extends AppCompatActivity {

    public static boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.movie_detail_container) != null) {
            isTablet = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new DetailsFragment(),
                                DetailsFragment.TAG)
                        .commit();
            }
        } else {
            isTablet = false;
        }
    }
    
}






