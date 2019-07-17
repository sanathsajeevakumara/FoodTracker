package com.example.foodtracker.Fragments;


import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.example.foodtracker.DatabaseHelper;
import com.example.foodtracker.DisplayIng;
import com.example.foodtracker.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class DisplayProductsPage extends Fragment {

    Button disply;
    ViewPager viewPager;
    DatabaseHelper myDB;

    public DisplayProductsPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        //Hide the notification tool bar color
//        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_display_products_page, container, false);

        disply = view.findViewById(R.id.view);
        viewPager = getActivity().findViewById(R.id.viewPager);

        disply.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), DisplayIng.class));
                    }
                }
        );
        return view;
    }

}


