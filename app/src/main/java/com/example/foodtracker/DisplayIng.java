package com.example.foodtracker;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DisplayIng extends AppCompatActivity{

    public static ArrayList<String> selectedItems = new ArrayList<>();
    static ArrayAdapter<String> adapter;
    ListView listView;
    Button addToKitchen;
    DatabaseHelper myDB;
    List<String> items;
    List<String> listName = new ArrayList<>();
    String name = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Hide the notification tool bar color
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_ing);

        listView = findViewById(R.id.clickable_list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        addToKitchen = findViewById(R.id.addToKitchen);

        //Get data from data base and add into an array list
        myDB = new DatabaseHelper(this);
        Cursor data = myDB.viewData();

        //Activate the alert box for the first time
        SharedPreferences preferences = getSharedPreferences("preferences2", MODE_PRIVATE);
        boolean firstStart = preferences.getBoolean("firstStart2", true);

        if(firstStart){
            showStartDialogOne();
        }

        while (data.moveToNext()){

            String name = data.getString(1);
            String availability = data.getString(5);

            System.out.println("Name : "+name +"  Available : "+availability);
            listName.add(name);


        }


        adapter = new ArrayAdapter<String>(this, R.layout.row_layout, R.id.txt_lan, listName);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = ((TextView)view).getText().toString();
                if (selectedItems.contains(selectedItem)){
                    selectedItems.remove(selectedItem);
                    Log.d("System" ,"---> removedItem "+ selectedItems);
                    showSelectedItem();
                }
                else
                    selectedItems.add(selectedItem);
                    Log.d("System" ,"---> selectedItem "+ selectedItems);
                    showSelectedItem();
            }
        });
    }


    /**
     * This is a dialog box show to user when ever he/she install and open first time
     */
    private void showStartDialogOne() {

        new AlertDialog.Builder(this)
                .setTitle("Tip 02 : Your Ingredient Collection")
                .setMessage("All the available ingredient are display in here!")
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
        SharedPreferences preferences = getSharedPreferences("preferences2", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("firstStart2", false);
        editor.apply();
    }


    /**
     * In this method automatically update user select ingredient to the add to kitchen side
     */
    String alertItems = "";
    public void showSelectedItem() {

        addToKitchen.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String unavailable = "Available";
                        Log.d("System" ,"---> new selectedItem "+ selectedItems);
                        for (final String item : selectedItems) {
                            alertItems += "Name : "+ item + "\n";
                            Log.d("OUTPUT","alertItems : " + alertItems);

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
                                        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayIng.this);
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayIng.this);
                        builder.setTitle("I added this for you");
                        builder.setMessage(alertItems);
                        alertItems = "";
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.show();

                    }
                });

    }
}
