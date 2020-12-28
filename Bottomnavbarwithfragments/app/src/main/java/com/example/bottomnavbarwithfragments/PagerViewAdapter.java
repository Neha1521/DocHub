package com.example.bottomnavbarwithfragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class PagerViewAdapter extends FragmentPagerAdapter {

    public PagerViewAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        Fragment fragment=null;

        switch (position){
            case 0:
                fragment=new fragHome();
                break;

            case 1:
                fragment=new fragChat();
                break;

            case 2:
                fragment=new fragProf();
                break;

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
