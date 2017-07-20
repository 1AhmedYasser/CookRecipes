package com.example.android.cookrecipes;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.cookrecipes.RecipeCatalog.LOGGING_TAG;

/**
 * {@link QueryHandler} final class is a class that handles the big oven request url.
 */
public final class QueryHandler {

    private QueryHandler() {
    }

    private static List<Recipe> extractInfoFromJson(String recipeJSON) {

        // If the JSON string is empty or null, then return.
        if (TextUtils.isEmpty(recipeJSON)) {
            return null;
        }

        // declare the recipes list.
        List<Recipe> recipes = new ArrayList<>();

        try {

            // Get the json response object from the recipe json url.
            JSONObject JSONResponse = new JSONObject(recipeJSON);

            /*
             * if the response contain the results array , then this is the catalog activity,
             * otherwise is the recipe info activity.
             */
            if (JSONResponse.has("Results")) {
                JSONArray resultsArray = JSONResponse.getJSONArray("Results");

                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject recipeDetails = resultsArray.getJSONObject(i);
                    String recipeId = recipeDetails.getString("RecipeID");
                    String recipeTitle = recipeDetails.getString("Title");
                    String recipeCategory = recipeDetails.getString("Category");
                    String recipeRating = recipeDetails.getString("StarRating");
                    String recipeImageUrl = recipeDetails.getString("PhotoUrl");

                    // create a new recipe object and pass to it the parsed variables.
                    Recipe recipe = new Recipe(recipeId, recipeTitle, recipeCategory, recipeRating, recipeImageUrl);

                    // adds the recipe object to the recipes list.
                    recipes.add(recipe);
                }
            } else {
                String recipeTitle = JSONResponse.getString("Title");
                String recipeImageUrl = JSONResponse.getString("PhotoUrl");
                String recipeDescription = JSONResponse.getString("Description");
                String recipeCategory = JSONResponse.getString("Category");
                String recipeSubCategory = JSONResponse.getString("Subcategory");
                String recipeRating = JSONResponse.getString("StarRating");
                String recipeIngredients = "";
                if (JSONResponse.has("Ingredients")) {
                    JSONArray IngredientsArray = JSONResponse.getJSONArray("Ingredients");
                    for (int i = 0; i < IngredientsArray.length(); i++) {
                        JSONObject currentIngredientObject = IngredientsArray.getJSONObject(i);
                        String tempString = currentIngredientObject.getString("DisplayQuantity") + " "
                                + currentIngredientObject.getString("Unit") + " " + currentIngredientObject.getString("Name");
                        recipeIngredients += tempString.replaceAll("null", "").trim();

                        if (i < IngredientsArray.length() - 1) {
                            recipeIngredients += "\n\n";
                        }
                    }
                }

                String recipeInstructions = JSONResponse.getString("Instructions");

                // create a new recipe object and pass to it the parsed variables.
                Recipe recipe = new Recipe(recipeTitle, recipeImageUrl, recipeDescription, recipeCategory,
                        recipeSubCategory, recipeRating, recipeIngredients, recipeInstructions);

                // adds the recipe object to the recipes list.
                recipes.add(recipe);
            }
        } catch (JSONException e) {
            Log.e(LOGGING_TAG, "Problem parsing the recipe data", e);
        }
        return recipes;
    }

    public static List<Recipe> fetchRecipeInfo(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOGGING_TAG, "Problem making the HTTP request.", e);
        }

        List<Recipe> recipes = extractInfoFromJson(jsonResponse);

        return recipes;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOGGING_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOGGING_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOGGING_TAG, "Problem retrieving the recipes JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}

