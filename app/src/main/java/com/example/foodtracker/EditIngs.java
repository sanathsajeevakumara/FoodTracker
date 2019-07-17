package com.example.foodtracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditIngs extends AppCompatActivity {

    DatabaseHelper myDb;
    static String ingID;
    ListView listView;
    ArrayList<String> listName;
    ArrayList<String> listID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Hide the notification tool bar color
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ings);

        listView = findViewById(R.id.clickable_list);

        //Get data from data base and add into an array list
        myDb = new DatabaseHelper(this);

        listName = new ArrayList<>();
        listID = new ArrayList<>();

        Cursor data = myDb.viewAvailableData();

        //Activate the alert box for the first time
        SharedPreferences preferences = getSharedPreferences("preferences4", MODE_PRIVATE);
        boolean firstStart = preferences.getBoolean("firstStart4", true);

        if(firstStart){
            showStartDialogOne();
        }

            if (data.getCount() == 0){
                Toast.makeText(this, "No data found :( ", Toast.LENGTH_SHORT).show();
            }else {
                while (data.moveToNext()){

                    listName.add(data.getString(1));
                    listID.add(data.getString(0));

                    ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listName);
                    listView.setAdapter(adapter);

                }

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ingID = listID.get(position);
                        String selectedItem = ((TextView)view).getText().toString();
                        Intent intent = new Intent(EditIngs.this, EditableIngsData.class);
                        intent.putExtra("ING_NAME", selectedItem);
                        intent.putExtra("ING_ID", ingID);
                        startActivity(intent);
                    }
                });
            }

    }

    /**
     * This is a dialog box show to user when ever he/she install and open first time
     */
    private void showStartDialogOne() {

        new AlertDialog.Builder(this)
                .setTitle("Tip 04 : Edit and Delete ")
                .setMessage("You can update your ingredient and also delete them!")
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
        SharedPreferences preferences = getSharedPreferences("preferences4", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("firstStart4", false);
        editor.apply();
    }
}

