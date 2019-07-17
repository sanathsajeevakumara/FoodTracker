package com.example.foodtracker;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class RecipeIng extends AppCompatActivity implements ApiAdapter.onItemClickListener {

    public static  String EXTRA_URL = "source_url";
    public static final String EXTRA_NAME = "title";

    RecyclerView recyclerView;
    ApiAdapter adapter;
    ArrayList<ApiItem> list;
    ArrayList<String> searchValue;
    RequestQueue requestQueue;
    SelectIngsForApi selectIngsForApi;
    public  static String search = "";
    public  static String searchResult = "";
    TextView showingResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Hide the notification tool bar color
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_ing);

        recyclerView = findViewById(R.id.recycler_View);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectIngsForApi = new SelectIngsForApi();
        searchValue = new ArrayList<>();
        showingResult = findViewById(R.id.showingResults);

        list = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        parseJSON();

    }



    private void parseJSON() {

        int i = 0;
        while (i < selectIngsForApi.readyToAPI.size()){
            String name = selectIngsForApi.readyToAPI.get(i);
            searchValue.add(name);
            i++;
            Log.d("System", "searchValue -> "+searchValue);
        }

        search = "";
        searchResult = "";
        for (String s : searchValue) {

            //Add selected value into the URL
            search += s+",";

            //Display selected value
            searchResult += s+" ";
        }
        System.out.println("SearchList : "+search);
        showingResult.setText(searchResult);

        String url = "https://www.food2fork.com/api/search?key=9a8aff48664c7bd562add8b01fe60902&q="+search+"&page=1";
        Log.d("System", "searchValue2 >>> "+searchValue);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("recipes");
                            for (int i = 0; i < jsonArray.length() ; i++) {
                                JSONObject recipe = jsonArray.getJSONObject(i);

                                String creator = recipe.getString("publisher");
                                String name = recipe.getString("title");
                                String pic_url = recipe.getString("image_url");
                                String url = recipe.getString("source_url");
                                //get the decimal point to integer
                                String rankValueString = recipe.getString("social_rank");
                                String[] parts = rankValueString.split(Pattern.quote("."));
                                String rankValue1 = parts[0];
                                String rank = String.valueOf(rankValue1);

                                list.add(new ApiItem(pic_url, name, creator, url, rank));
                            }

                            adapter = new ApiAdapter(RecipeIng.this, list);
                            recyclerView.setAdapter(adapter);

                            adapter.setItemOnCLickListener(RecipeIng.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }


    @Override
    public void onItemClick(int position) {

//        Intent intent = new Intent(this, ApiDetails.class);
//        ApiItem clcikedItem = list.get(position);
//
//        intent.putExtra(EXTRA_URL, clcikedItem.getImageURL());
//        intent.putExtra(EXTRA_NAME, clcikedItem.getNameOfTheFood());
//        intent.putExtra(EXTRA_CREATOR, clcikedItem.getCreator());
//
//        startActivity(intent);

        ApiItem clikedItem = list.get(position);

        EXTRA_URL = clikedItem.getUrl();

        Uri uri = Uri.parse(EXTRA_URL);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }
}
