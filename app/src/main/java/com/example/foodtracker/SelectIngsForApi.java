package com.example.foodtracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.foodtracker.Fragments.RecipesPage;

import java.util.ArrayList;

public class SelectIngsForApi extends AppCompatActivity {

    DatabaseHelper myDB;
    ListView listView;
    ArrayList<String> selectToAPI = new ArrayList<>();
    static ArrayList<String> readyToAPI = new ArrayList<>();
    Button findRecipes;
    public  static String  items = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Hide the notification tool bar color
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        super.onCreate(savedInstanceState);

        if(!isConnected(SelectIngsForApi.this)) buildDialog(SelectIngsForApi.this).show();
        else {
            // Toast.makeText(MainActivity.this,"Welcome", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_select_ings_for_api);


            listView = findViewById(R.id.clickable_list);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            findRecipes = findViewById(R.id.confirmAndSave);
            myDB = new DatabaseHelper(this);
            Cursor data = myDB.viewData();

            //Activate the alert box for the first time
            SharedPreferences preferences = getSharedPreferences("preferences6", MODE_PRIVATE);
            boolean firstStart = preferences.getBoolean("firstStart6", true);

            if(firstStart){
                showStartDialogOne();
            }



            while (data.moveToNext()){

                String name = data.getString(1);
                String availability = data.getString(5);

                System.out.println("Name : "+name +"  Available : "+availability);
                if(availability.equals("Available")){
                    selectToAPI.add(name);
                }


            }


            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.row_layout_4, selectToAPI);
            listView.setAdapter(adapter);

            //readyToAPI = null;
            readyToAPI.clear();

            listView.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String selectedItem = ((TextView)view).getText().toString();

                            if (readyToAPI.contains(selectedItem)){
                                readyToAPI.remove(selectedItem);
                                Log.d("System" ,"---> Not readyToAPI "+ readyToAPI);
                                getToSearch();
                            }
                            else{
                                readyToAPI.add(selectedItem);
                                Log.d("System" ,"---> readyToAPI "+ readyToAPI);
                                getToSearch();
                            }
                        }
                    }
            );

            for (String item : readyToAPI){
                items += item+",";
            }
            System.out.println("Item : "+items);

        }

    }


    /**
     * This is a dialog box show to user when ever he/she install and open first time
     */
    private void showStartDialogOne() {

        new AlertDialog.Builder(this)
                .setTitle("Tip 06 : Find Recipes")
                .setMessage("Here you can find your recipes."+"\n"+"Require Mobile Data or WIFI.")
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
        SharedPreferences preferences = getSharedPreferences("preferences6", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("firstStart6", false);
        editor.apply();
    }



    /**
     * Api is call when user select the ingredients and press the search recipes button
     */
    public void getToSearch(){

        findRecipes.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(
                                new Intent(
                                        SelectIngsForApi.this, RecipeIng.class
                                )
                        );
                    }
                }
        );

    }

    /**
     * Check the internet is connected or not
     * @param context
     * @return
     */
    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        } else
            return false;
    }


    /**
     * Alert user to on the internet connection
     * @param
     * @return
     */
    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("Please connect your Mobile Data or WIFI to access this.");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder;
    }
}
