package com.example.foodtracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SearchIngs extends AppCompatActivity {

    ListView searchView;
    public static ArrayList<String> items;
    public static ArrayList<String> selectAllItems;
    ArrayList<String> foundItems;
    ArrayList<String> viewSelected;
    DatabaseHelper myDB;
    StringBuffer buffer;
    SearchView searchButton;
    static String displaySelected;
    public Cursor data;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        //Hide the notification tool bar color
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ings);

        searchView = findViewById(R.id.searchView);
        items = new ArrayList<>();
        foundItems = new ArrayList<>();
        viewSelected = new ArrayList<>();
        selectAllItems = new ArrayList<>();
        myDB = new DatabaseHelper(this);
        buffer = new StringBuffer();
        searchButton = findViewById(R.id.searchProduct);


        //Activate the alert box for the first time
        SharedPreferences preferences = getSharedPreferences("preferences5", MODE_PRIVATE);
        boolean firstStart = preferences.getBoolean("firstStart5", true);

        if(firstStart){
            showStartDialogOne();
        }

        data = myDB.viewData();
        while (data.moveToNext()){


            // ====== This array contain all name about the selected ingredient =======
            items.add(data.getString(1));

        }

        System.out.println(viewSelected);

        // ========== This adapter will add available ingredients to the list view ===========
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout
                .simple_list_item_1, items);
        searchView.setAdapter(adapter);

        // ========== This adapter will add the ingredients into the list view after that is found ===========
        ArrayAdapter<String> adapterFound = new ArrayAdapter<>(this, android.R.layout.
                simple_list_item_1, foundItems);
        searchView.setAdapter(adapterFound);




        /**
         * This method is get the user enter word and search if there are any
         * satiable word find with thad word
         */
        searchButton.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                System.out.println("query = " + query);
                adapter.getFilter().filter(query);
                return true;

            }

            /**
             * This method will search the user enter word into the available words
             * @param newText user value
             * @return true
             */
            @Override
            public boolean onQueryTextChange(String newText) {

                if (TextUtils.isEmpty(newText)) {
                    searchView.clearTextFilter();
                    foundItems.clear();
                } else {
                    System.out.println("System ---> "+newText);
                    for (int i = 0; i < items.size(); i++) {

                        // ======= Searching  the user enter word is suitable from in
                        // the available word list ======
                        if(items.get(i).toLowerCase().contains(searchButton.getQuery().toString()
                                .toLowerCase())){

                            System.out.println("Found : "+items.get(i));
                            searchView.setFilterText(newText);
                            adapter.getFilter().filter(newText);
                            foundItems.add(items.get(i));
                            System.out.println(foundItems);

                            // ====== if there any repeat words the this will clear that
                            // repeating value =======
                            Set<String> set = new HashSet<>(foundItems);
                            foundItems.clear();
                            System.out.println("Clear Value : "+ foundItems);
                            foundItems.addAll(set);
                            System.out.println("add Value : "+ foundItems);

                        }else{
                            System.out.println("Not Found! : "+ items.get(i));
                            searchView.setFilterText(newText);
                            adapter.getFilter().filter(newText);
                        }
                    }

                    //System.out.println("length : "+ data.getString(1));

                    // ==== Get user selected ingredient =======
                    searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            System.out.println("position : "+position);

                            displaySelected = foundItems.get(position);
                            Intent intent = new Intent(SearchIngs.this, ResultsIngs.class);
                            intent.putExtra("SELECTED_VALUES", displaySelected);
                            System.out.println("System > User selected : "+displaySelected);
                            startActivity(intent);
                        }
                    });

                }
                return true;
            }
        });
    }


    /**
     * This is a dialog box show to user when ever he/she install and open first time
     */
    private void showStartDialogOne() {

        new AlertDialog.Builder(this)
                .setTitle("Tip 05 : Search")
                .setMessage("Can not find ingredient! Then search here.")
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
        SharedPreferences preferences = getSharedPreferences("preferences5", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("firstStart5", false);
        editor.apply();
    }



}
