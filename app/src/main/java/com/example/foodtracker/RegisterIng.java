package com.example.foodtracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class RegisterIng extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    EditText id, name, weight, price, description;
    Button register;
    DatabaseHelper myDB;
    String spinnerValue = "Unavailable";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Hide the notification tool bar color
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_ing);

        id = findViewById(R.id.idText);
        name = findViewById(R.id.nameText);
        weight = findViewById(R.id.weightText);
        price = findViewById(R.id.priceText);
        description = findViewById(R.id.descText);

        register = findViewById(R.id.register);

        myDB = new DatabaseHelper(this);

        //Activate the alert box for the first time
        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        boolean firstStart = preferences.getBoolean("firstStart", true);

        if(firstStart){
            showStartDialogOne();
        }

        addData();
    }

    /**
     * This is a dialog box show to user when ever he/she install and open first time
     */
    private void showStartDialogOne() {

        new AlertDialog.Builder(this)
                .setTitle("Tip 01 : Add an Ingredient ")
                .setMessage("Here you can add any ingredient that you like!")
                .setCancelable(false)
                .setPositiveButton("ok",
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }
                )
                .create().show();
        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    /**
     * Adding user enter values into the database
     */
    public void addData(){
        register.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (checker()) {


                            String nameValue = name.getText().toString();
                            int weightValue = Integer.parseInt(weight.getText().toString());
                            int priceValue = Integer.parseInt(price.getText().toString());
                            String descValue = description.getText().toString();

                            Log.d("FIND", "User enter -> "+nameValue);
                            Log.d("FIND", "User enter -> "+weightValue);
                            Log.d("FIND", "User enter -> "+priceValue);
                            Log.d("FIND", "User enter -> "+descValue);


                            boolean isInsert = myDB.insertData( nameValue, weightValue,
                                    priceValue, descValue, spinnerValue);


                            if( isInsert ){
//                                Toast.makeText(
//                                        RegisterIng.this, "Ingredient is added!", Toast.LENGTH_SHORT
//                                ).show();
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterIng.this);
                                builder.setTitle("I found an ingredient");
                                builder.setMessage( nameValue+" is added!");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();

                            }else {
//                                Toast.makeText(
//                                        RegisterIng.this, "Ingredient is not add", Toast.LENGTH_SHORT
//                                ).show();
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterIng.this);
                                builder.setTitle("I missed an ingredient");
                                builder.setMessage("Can you please add again");
                                builder.setPositiveButton("Add again", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();

                            }

                            //clear when data is updated
                            name.getText().clear();
                            weight.getText().clear();
                            price.getText().clear();
                            description.getText().clear();

                        }else {
                            System.out.println("No value added");
                        }
                    }
                }

        );
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerValue = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * When user clicked update button with empty value
     * @return true
     */
    public boolean checker(){
        if (name.getText().toString().equals("") || weight.getText().toString().equals("") || price.getText().toString().equals("") ||
                description.getText().toString().equals("")){
//            Toast.makeText(
//                    RegisterIng.this, "Please fill the required fields", Toast.LENGTH_SHORT
//            ).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterIng.this);
            builder.setTitle("Waiting for the new ingredient");
            builder.setMessage("Please fill the required fields");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();


            return false;
        }
        System.out.println("All fields are filled");
        return true;
    }
}
