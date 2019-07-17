package com.example.foodtracker.Fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.example.foodtracker.DatabaseHelper;
import com.example.foodtracker.R;
import com.example.foodtracker.RegisterIng;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterProductPage extends Fragment {

    Button register;
    ViewPager viewPager;


    public RegisterProductPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        //Hide the notification tool bar color
//        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_product_page, container, false);

        register = view.findViewById(R.id.register);
        viewPager = getActivity().findViewById(R.id.viewPager);

        register.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(
                                new Intent(
                                        getActivity(), RegisterIng.class
                                )
                        );
                    }
                }
        );
        return view;
    }

}
