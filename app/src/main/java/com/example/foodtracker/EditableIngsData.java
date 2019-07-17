package com.example.foodtracker;

import android.content.DialogInterface;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EditableIngsData extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView id, name, weight, price, description;
    Button update, delete;
    Spinner spinner;
    DatabaseHelper myDB;
    public String text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        //Hide the notification tool bar color
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editable_ings_data);

        // ==== Casting =====
        id = findViewById(R.id.idText);
        name = findViewById(R.id.nameText);
        weight = findViewById(R.id.weightText);
        price = findViewById(R.id.priceText);
        description = findViewById(R.id.descText);
        spinner = findViewById(R.id.spinner);

        update = findViewById(R.id.update);
        delete = findViewById(R.id.delete);
        myDB = new DatabaseHelper(this);

        // ==== Get ingredient id and name from EditIng Class =====
        String itemName = getIntent().getStringExtra("ING_NAME");
        Log.d("System","Value of name ----> "+itemName);
        String itemID = getIntent().getStringExtra("ING_ID");
        Log.d("System","Value of id ----> "+itemID);

        // ======Set to the relative text view ======
        id.setText(itemID);
        name.setText(itemName);
        // ==== Set itText view can't editable ====
        id.setKeyListener(null);

        //Activate the alert box for the first time
        SharedPreferences preferences = getSharedPreferences("preferences7", MODE_PRIVATE);
        boolean firstStart = preferences.getBoolean("firstStart7", true);

        if(firstStart){
            showStartDialogOne();
        }

        ArrayAdapter<CharSequence> adapter =  ArrayAdapter.createFromResource(
                this, R.array.Availability, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        upDate();
        remove();

    }


    /**
     * This is a dialog box show to user when ever he/she install and open first time
     */
    private void showStartDialogOne() {

        new AlertDialog.Builder(this)
                .setTitle("Tip 02 : For Deletion ")
                .setMessage("If Your choice is delete the ingredient then check the name and id and just click the delete button")
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
        SharedPreferences preferences = getSharedPreferences("preferences7", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("firstStart7", false);
        editor.apply();
    }


    /**
     * Delete a data in data base
     */
    public void remove(){
        delete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer isDelete = myDB.deleteData(
                                id.getText().toString()
                        );

                        if(isDelete > 0 ){

                            AlertDialog.Builder builder = new AlertDialog.Builder(EditableIngsData.this);
                            builder.setTitle("Deleted");
                            builder.setMessage(name.getText().toString()+" is deleted!");
                            builder.setCancelable(false);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            builder.show();

                        }
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(EditableIngsData.this);
                            builder.setTitle("Something went wrong");
                            builder.setMessage("Ingredient is not delete!");
                            builder.setCancelable(false);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            builder.show();
                        }
                    }
                }
        );
    }

    /**
     * update data to data base
     */
    public void upDate(){

        update.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            if(checker()==true){

                                System.out.println("Correct");

                                //Update new data to database
                                boolean isUpdate = myDB.updateData(
                                        id.getText().toString(),
                                        name.getText().toString(),
                                        Integer.parseInt(weight.getText().toString()),
                                        Integer.parseInt(price.getText().toString()),
                                        description.getText().toString(),
                                        text
                                );


                                if(isUpdate == true){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(EditableIngsData.this);
                                    builder.setTitle("Updated");
                                    builder.setMessage(name.getText().toString()+" is updated!");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    builder.show();


                                }else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(EditableIngsData.this);
                                    builder.setTitle("Something went wrong");
                                    builder.setMessage("Ingredient is not update!");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    builder.show();
                                }

                                //clear when data is updated
                                weight.setText("");
                                price.setText("");
                                description.setText("");


                            }else{
                                System.out.println("No value added");
                            }
                    }
                }
        );

    }


    /**
     * get the selected value from array adapter
     * @param parent
     * @param view
     * @param position get position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        text = parent.getItemAtPosition(position).toString();
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

            AlertDialog.Builder builder = new AlertDialog.Builder(EditableIngsData.this);
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
