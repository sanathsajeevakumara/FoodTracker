package com.example.foodtracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Timer timer;
    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        //Hide the notification tool bar color
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autoSlider();

        myDB = new DatabaseHelper(this);


    }

    /**
     * After 2 seconds this method will automatically slid the MainActivity class to
     * RegisterProductPage class
     */
    public void autoSlider(){
        timer = new Timer();
        timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        startActivity(
                                new Intent(
                                        MainActivity.this, FragmentStarter.class
                                )
                        );finish();
                    }
                },2000
        );
    }

}
