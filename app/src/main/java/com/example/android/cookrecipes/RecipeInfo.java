package com.example.android.cookrecipes;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link RecipeInfo} class is a class the represents information about a particular
 * recipe chosen by the user from the recipe catalog.
 */
public class RecipeInfo extends AppCompatActivity implements LoaderCallbacks<List<Recipe>> {

    // declare a constant id for the recipe loader.
    private static final int RECIPE_LOADER_ID = 1;

    //declare the big oven request url.
    private static String DETAIL_REQUEST_URL;

    // declare and bind the view automatically using the butter knife library
    @BindView(R.id.CollapsingToolBar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.recipe_description_text)
    TextView descriptionText;
    @BindView(R.id.recipe_category_text)
    TextView categoryText;
    @BindView(R.id.recipe_subCategory_text)
    TextView subCategoryText;
    @BindView(R.id.recipe_rating_text)
    TextView ratingText;
    @BindView(R.id.recipe_ingredients_text)
    TextView ingredientsText;
    @BindView(R.id.recipe_instructions_text)
    TextView instructionsText;
    @BindView(R.id.recipeCollapsingImage)
    ImageView recipeCollapsingImage;

    // Declare the loader manager and the network info and the connectivity manager.
    LoaderManager loaderManager;
    NetworkInfo networkInfo;
    ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_info);
        ButterKnife.bind(this);

        // get the intent and extract the recipe id and append it to the detail request url.
        Intent intent = getIntent();
        String recipeID = intent.getStringExtra("recipeID");
        DETAIL_REQUEST_URL = "https://api2.bigoven.com/recipe/" + recipeID + "?api_key=axV15293h59oU9Z853fw48CmI1H1Js";

        // Set collapsing toolbar custom text appearance style.
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedCollapsingToolbarTextAppearance);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedCollapsingToolbarTextAppearance);

        connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check for network connectivity before starting the loader.
        networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            loaderManager = getLoaderManager();
            loaderManager.initLoader(RECIPE_LOADER_ID, null, RecipeInfo.this);
        }
    }

    @Override
    public Loader<List<Recipe>> onCreateLoader(int i, Bundle bundle) {
        // Create a new recipe loader for the given URL
        return new RecipeLoader(this, DETAIL_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> recipeData) {

        // if the recipe data is empty ,then return.
        if (recipeData.isEmpty()) {
            return;
        }

        //Get the recipe object and the extract its information and populate the views.
        networkInfo = connectivityManager.getActiveNetworkInfo();
        Recipe recipe = recipeData.get(0);
        if (recipe != null && networkInfo != null && networkInfo.isConnected()) {
            collapsingToolbarLayout.setTitle("" + recipe.getRecipeTitle());
            if (recipe.getRecipeDescription().equals("")) {
                descriptionText.setText(R.string.No_Description);
            } else {
                descriptionText.setText(recipe.getRecipeDescription());
            }
            categoryText.setText(getString(R.string.category) + " " + recipe.getRecipeCategory());
            subCategoryText.setText(getString(R.string.sub_category) + " " + recipe.getRecipeSubCategory());
            String formattedRating = formatRating(Double.valueOf(recipe.getRecipeRating()));
            ratingText.setText(getString(R.string.rating) + " " + formattedRating);
            ingredientsText.setText(recipe.getRecipeIngredients());
            instructionsText.setText(recipe.getRecipeInstructions());
            Glide.with(this).load(recipe.getRecipeImageUrl()).into(recipeCollapsingImage);
        }
    }

    /*
    * A helper method that format the recipe rating to 1 decimal point
    */
    private String formatRating(double recipeRating) {
        DecimalFormat ratingFormat = new DecimalFormat("0.0");
        return ratingFormat.format(recipeRating);
    }

    @Override
    public void onLoaderReset(Loader<List<Recipe>> loader) {
    }
}
