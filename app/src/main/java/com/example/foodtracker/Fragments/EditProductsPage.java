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

import com.example.foodtracker.EditIngs;
import com.example.foodtracker.R;
import com.example.foodtracker.RegisterIng;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditProductsPage extends Fragment {

    Button edit;
    ViewPager viewPager;


    public EditProductsPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        //Hide the notification tool bar color
//        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_edit_products_page, container, false);

        edit = view.findViewById(R.id.editButton);
        viewPager = getActivity().findViewById(R.id.viewPager);

        edit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(
                                new Intent(
                                        getActivity(), EditIngs.class
                                )
                        );
                    }
                }
        );


        return view;
    }

}
