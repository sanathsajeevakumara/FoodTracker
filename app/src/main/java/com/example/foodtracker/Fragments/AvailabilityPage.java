package com.example.foodtracker.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.example.foodtracker.AvailableIng;
import com.example.foodtracker.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AvailabilityPage extends Fragment {

    Button availabilty;
    ViewPager viewPager;


    public AvailabilityPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_availability_page, container, false);

//        //Hide the notification tool bar color
//        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        availabilty = view.findViewById(R.id.availabilty);
        viewPager = getActivity().findViewById(R.id.viewPager);

        availabilty.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(
                                new Intent(
                                        getActivity(), AvailableIng.class
                                )
                        );
                    }
                }
        );
        return view;
    }

}
