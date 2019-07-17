package com.example.foodtracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AvailableIng extends AppCompatActivity {


    List<String> listName = new ArrayList<>();
    public static ArrayList<String> updateKitchen = new ArrayList<>();
    static ArrayAdapter<String> adapter;
    List<String> items;
    ListView listView;
    DatabaseHelper myDB;
    Button confirmAndSave;
    String name = "";
    public static String selectedItem = "";
    String alertItems = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Hide the notification tool bar color
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_ing);

        confirmAndSave = findViewById(R.id.confirmAndSave);
        myDB = new DatabaseHelper(this);
        Cursor data = myDB.viewData();

        //Activate the alert box for the first time
        SharedPreferences preferences = getSharedPreferences("preferences3", MODE_PRIVATE);
        boolean firstStart = preferences.getBoolean("firstStart3", true);

        if(firstStart){
            showStartDialogOne();
        }


        listView = findViewById(R.id.clickable_list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


        while (data.moveToNext()){

            String name = data.getString(1);
            String availability = data.getString(5);

            System.out.println("Name : "+name +"  Availability : "+availability);
            if(availability.equals("Available")){
                listName.add(name);
            }


        }


        adapter = new ArrayAdapter<>(this, R.layout.row_layout_2, listName);
        listView.setAdapter(adapter);

        //set checked for selected ingredients
        for (int j = 0; j < listName.size() ; j++) {

            listView.setItemChecked(j, true);
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = ((TextView)view).getText().toString();
                System.out.println("Now Selected Item : "+selectedItem);

                if (updateKitchen.contains(selectedItem)){

                    updateKitchen.remove(selectedItem);
                    Log.d("System" ,"---> removeKitchen "+ updateKitchen);

                    //listName.remove(position);
                    //adapter.remove(selectedItem);
                    //adapter.notifyDataSetChanged();

                    System.out.println("Now Removed :" +listName);
                    updateAvailability();
                }
                else{
                    updateKitchen.add(selectedItem);
                    Log.d("System" ,"---> UpdateKitchen "+ updateKitchen);
                    updateAvailability();
                }
            }
        });



    }

    /**
     * This method showing an alert box ( For Tip ) when user install the app first time
     */
    private void showStartDialogOne() {

        new AlertDialog.Builder(this)
                .setTitle("Tip 03 : Your Kitchen ")
                .setMessage("All ingredient that you selected are display in here!")
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
        SharedPreferences preferences = getSharedPreferences("preferences3", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("firstStart3", false);
        editor.apply();
    }

    /**
     * Update the availability of the user selected ingredients
     */
    public void updateAvailability(){

        //String items = "";
        confirmAndSave.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String unavailable ="Unavailable";

                        for (final String item : updateKitchen) {
                            alertItems += "Name : "+ item + "\n";
                            System.out.println("Items : " + alertItems);

                            items = new ArrayList<>();
                            items.add(item);
                            System.out.println("Items : " + items);

                            // ========= Save user selected ingredient name one by one in to the  name variable ========
                            for (int i = 0; i < items.size(); i++) {
                                name = items.get(i);
                                Log.d("System", "name ---> " + name);
                                Log.d("System", "Size ---> " + items.size());

                                // ====== Update user selected ingredient availability as Unavailable ========
                                for (int j = 0; j < items.size(); j++) {
                                    boolean isUpdate = myDB.updateAvailability(
                                            name,
                                            unavailable
                                    );

                                    // ====== Confirm user that he/she selected items are only available for him =======
                                    if (isUpdate == true) {
                                        System.out.println(name + " : Data Updated");
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(AvailableIng.this);
                                        builder.setTitle("Not added to kitchen");
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                        builder.show();

                                        System.out.println(name + " : Data not update");
                                    }
                                }
                            }
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(AvailableIng.this);
                        builder.setTitle("I removed this for you");
                        builder.setMessage(alertItems);
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                        alertItems = "";
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
