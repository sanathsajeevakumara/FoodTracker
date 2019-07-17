package com.example.foodtracker;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import java.sql.SQLOutput;
import java.util.ArrayList;

public class ResultsIngs extends AppCompatActivity {

    TextView result;
    DatabaseHelper myDB;
    ArrayList<String> freshList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Hide the notification tool bar color
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_ings);

        result = findViewById(R.id.result);
        myDB = new DatabaseHelper(this);
        freshList = new ArrayList<>();

        String results = getIntent().getStringExtra("SELECTED_VALUES");
        Log.d("System","Value of name ----> "+results);

        Cursor data = myDB.viewSelectedData(results);
        while (data.moveToNext()){
            freshList.add("ID : "+data.getString(0)+"\n" +
                    "Name : "+data.getString(1)+"\n" +
                    "Weight : "+data.getString(2)+"g"+"\n" +
                    "Price : "+data.getString(3)+"\n" +
                    "Description : "+data.getString(4)+"\n" +
                    "Availability : "+data.getString(5)+"\n\n");
        }
        System.out.println("Fresh Data : "+freshList);
        result.setText(freshList.get(0));
    }
}
