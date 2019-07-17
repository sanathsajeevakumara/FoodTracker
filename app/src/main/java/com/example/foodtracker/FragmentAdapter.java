package com.example.foodtracker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.foodtracker.Fragments.AvailabilityPage;
import com.example.foodtracker.Fragments.DisplayProductsPage;
import com.example.foodtracker.Fragments.EditProductsPage;
import com.example.foodtracker.Fragments.RecipesPage;
import com.example.foodtracker.Fragments.RegisterProductPage;
import com.example.foodtracker.Fragments.SearchPage;

public class FragmentAdapter extends FragmentPagerAdapter {

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0:
                return new RegisterProductPage();
            case 1:
                return new DisplayProductsPage();
            case 2:
                return new AvailabilityPage();
            case 3:
                return new EditProductsPage();
            case 4:
                return new SearchPage();
            case 5:
                return new RecipesPage();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 6;
    }
}
