package com.example.fit5046_assignment2;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MyDailyDietScreenFragment extends Fragment{

    Spinner food;
    Spinner foodName;
    View vdaliyDiet;
    EditText et_newFoodInfo;
    TextView tv_shownewfood;
    Button b_search;
    ImageView img_food;
    EditText et_addNewFood;
    Button b_addFood;
    String energyKCal;
    String fat;
    public String foodCategory;
    List<String> calories = new ArrayList<String>();
    List<String> fats = new ArrayList<String>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        vdaliyDiet = inflater.inflate(R.layout.fragment_mydailydiet, container, false);
        et_newFoodInfo = (EditText) vdaliyDiet.findViewById(R.id.et_newFoodInfo);
        tv_shownewfood = (TextView) vdaliyDiet.findViewById(R.id.tv_shownewfood) ;
        b_search = (Button) vdaliyDiet.findViewById(R.id.b_search);
        img_food = (ImageView) vdaliyDiet.findViewById(R.id.img_food);
        b_addFood = (Button) vdaliyDiet.findViewById(R.id.b_addFood);
        et_addNewFood = (EditText) vdaliyDiet.findViewById(R.id.et_addNewFood);
        List<String> list = new ArrayList<String>();
        list.add("Please Select");
        list.add("Drink");
        list.add("Meal");
        list.add("Meat");
        list.add("Snack");
        list.add("Bread");
        list.add("Cake");
        list.add("Fruit");
        list.add("Vegetable");
        list.add("Other");

        food = (Spinner) vdaliyDiet.findViewById(R.id.spinnerfood);
        foodName = (Spinner) vdaliyDiet.findViewById(R.id.spinnername);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        food.setAdapter(adapter);

        food.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int
                    position, long id) {
                if(position != 0) {
                    foodCategory = parent.getItemAtPosition(position).toString();
                    System.out.println("Selected value"+" "+foodCategory);
                    FoodCategoryAsyncTask callFood = new FoodCategoryAsyncTask();
                    callFood.execute(foodCategory);
                }
                else
                    {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        foodName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int
                    position, long id) {
                if (position != 0) {
                    String name = parent.getItemAtPosition(position).toString();
                    SearchAsyncTask googleSearch = new SearchAsyncTask();
                    googleSearch.execute(name);
                    ImageAsyncTask imageAsyncTask = new ImageAsyncTask();
                    imageAsyncTask.execute(name);

                }
                else {

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        b_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                String keyword = et_newFoodInfo.getText().toString();
                SearchAsyncTask googleSearch = new SearchAsyncTask();
                googleSearch.execute(keyword);
                ImageAsyncTask imageAsyncTask = new ImageAsyncTask();
                imageAsyncTask.execute(keyword);

            }
        });

        b_addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = et_addNewFood.getText().toString();
                FoodDisplayAsync displayFood = new FoodDisplayAsync();
                displayFood.execute(name);

            }
        });

        return vdaliyDiet;
    }


    private class FoodDisplayAsync extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            return RestAPIClient.getFoodInformation(params[0]);

        }
        @Override
        protected void onPostExecute(String result) {
            System.out.println("On FoodDisplayPost execute"+ " "+result);
            JSONObject rootObject = null;
            try {
                rootObject = new JSONObject(result);
                JSONArray parsedArray =  rootObject.optJSONArray("parsed");
                JSONObject foodObject = parsedArray.optJSONObject(0).optJSONObject("food");
                energyKCal = foodObject.optJSONObject("nutrients").optString("ENERC_KCAL");
                fat = foodObject.optJSONObject("nutrients").optString("FAT");

                String valueEntered = et_addNewFood.getText().toString();
                int random_foodId = (int)(Math.random() * ((2879 - 100)+ 1) + 250);
                String id = String.valueOf(random_foodId);
                FoodInsertAsync putFoodInfo =  new FoodInsertAsync();
                Double Calorie = Double.valueOf(energyKCal);
                putFoodInfo.execute(id,valueEntered,energyKCal,fat);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class FoodInsertAsync extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

                Food food = null;
                String chkFoodname = RestAPIClient.findByFoodname(params[1]);
                if(chkFoodname.equals("[]")) {
                    food = new Food(Integer.valueOf(params[0]), params[1], foodCategory, (int) Double.parseDouble(params[2]), "Tbsp", new BigDecimal("1.25"), (int) Double.parseDouble(params[3]));
                    RestAPIClient.insertFood(food);
                    return "Success";
                }
                return "Failure";

        }
        @Override
        protected void onPostExecute(String result) {
            if(result.equals("Success")) {
                FoodCategoryAsyncTask callFood = new FoodCategoryAsyncTask();
                callFood.execute(foodCategory);
                System.out.println("Food Item is " + " " + result);
            }
            else
            {
                Toast.makeText(getActivity(), "Food Item already exists!",
                        Toast.LENGTH_LONG).show();
            }


            }
        }


    private class SearchAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            if(!(params[0].isEmpty())) {
                return RestAPIClient.googleSearch(params[0], new String[]{"num"}, new
                        String[]{"1"});
            }
            else
            {
                return "";
            }

        }
        @Override
        protected void onPostExecute(String result) {
            if (result.equals("")) {

                tv_shownewfood.setText("Edit box cannot be empty!");
            } else {

                tv_shownewfood.setText(getGoogleSnippet(result));
            }
        }
    }

    public static String getGoogleSnippet(String result){
        String snippet = null;
        try{
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            if(jsonArray != null && jsonArray.length() > 0) {
                snippet =jsonArray.getJSONObject(0).getString("snippet");
            }
        }catch (Exception e){
            e.printStackTrace();
            snippet = "NO INFO FOUND";
        }
        return snippet;
    }

    //Image Async Task

    private class ImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            String result =  RestAPIClient.imageSearch(params[0], new String[]{"num"}, new
                    String[]{"1"});
            String image = getImageSnippet(result);
            try
            {
                URL url = new URL(image);
                return BitmapFactory.decodeStream(url.openConnection().getInputStream());
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return BitmapFactory.decodeResource(getResources(), R.drawable.home);
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {

            if(bitmap != null) {
                img_food.setImageBitmap(bitmap);

            }

        }
    }

    public static String getImageSnippet(String result){
        String image = null;
        try{
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            if(jsonArray != null && jsonArray.length() > 0) {
                image =jsonArray.getJSONObject(0).getString("link");
            }
        }catch (Exception e){
            e.printStackTrace();
            image = "NO INFO FOUND";
        }
        return image;
    }


    private class FoodCategoryAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            System.out.println("Inside background"+" "+params[0]);
            return RestAPIClient.findFoodByFoodCategory(params[0]);
        }

        protected void onPostExecute(String details) {
                System.out.println("Food items:" + details);
            List<String> names = new ArrayList<String>();
            names.add("Please Select");
            calories.add("0");
            fats.add("0");

            JSONArray foodDetails = null;
            try {
                foodDetails = new JSONArray(details);
                if(foodDetails.length()>0) {
                    for (int i=0; i< foodDetails.length(); i++) {
                        JSONObject foodDetail = foodDetails.optJSONObject(i);
                        names.add(foodDetail.optString("name"));
                        calories.add(foodDetail.optString("calorie"));
                        fats.add(foodDetail.optString("fat"));
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            System.out.println("Food names :"+names.toString());
            ArrayAdapter<String> newAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, names);
            newAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            foodName.setAdapter(newAdapter);
        }
    }

}
