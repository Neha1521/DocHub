package com.example.bottomnavbarwithfragments;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager=findViewById(R.id.viewPager);
        PagerViewAdapter pagerViewAdapter=new PagerViewAdapter(getSupportFragmentManager(),PagerViewAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(pagerViewAdapter);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(nav);



    }


    private BottomNavigationView.OnNavigationItemSelectedListener nav=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            int pos=0;

            switch (menuItem.getItemId()){
                case R.id.navigation_home:
                    pos=0;
                    break;

                case R.id.navigation_dashboard:
                    pos=1;
                    break;

                case R.id.navigation_notifications:
                    pos=2;
                    break;

            }
            viewPager.setCurrentItem(pos);
            return true;
        }
    };


}

