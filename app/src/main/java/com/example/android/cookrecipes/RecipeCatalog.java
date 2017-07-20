package com.example.android.cookrecipes;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link RecipeCatalog} class is a class that represents a list of recipes to the user
 * and allow the user to search for different recipes.
 */
public class RecipeCatalog extends AppCompatActivity implements LoaderCallbacks<List<Recipe>> {

    // declare a logging tag for showing debug messages when something goes wrong in the app.
    public static final String LOGGING_TAG = RecipeCatalog.class.getName();

    // declare a constant id for the recipe loader.
    private static final int RECIPE_LOADER_ID = 1;

    //declare the big oven request url.
    private static String REQUEST_URL = "http://api2.bigoven.com/recipes?&api_key=axV15293h59oU9Z853fw48CmI1H1Js";

    //declare the connectivity manager to check for internet connection.
    ConnectivityManager connectivityManager;

    // declare and bind the view automatically using the butter knife library
    @BindView(R.id.recipe_List)
    ListView recipeList;
    @BindView(R.id.empty_view)
    TextView emptyView;
    @BindView(R.id.searchButton)
    Button searchButton;
    @BindView(R.id.loading_indicator)
    View loadingIndicator;
    @BindView(R.id.searchEditText)
    EditText searchEditText;

    // Declare the loader manager and the network info.
    LoaderManager loaderManager;
    NetworkInfo networkInfo;

    // declare a searching boolean to indicate if the search button is clicked.
    boolean searching = false;

    //declare the recipe custom list adapter.
    private RecipeCustomAdapter recipeCustomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_catalog);
        ButterKnife.bind(this);

        // set the empty view of the list view.
        recipeList.setEmptyView(emptyView);

        connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check for network connectivity before starting the loader.
        networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            loaderManager = getLoaderManager();
            loaderManager.initLoader(RECIPE_LOADER_ID, null, RecipeCatalog.this);
        } else {
            loadingIndicator.setVisibility(View.GONE);
            emptyView.setText(R.string.No_Internet);
        }

        // Initialize the recipe list custom adapter.
        recipeCustomAdapter = new RecipeCustomAdapter(this, new ArrayList<Recipe>());

        // connect the adapter to the list.
        recipeList.setAdapter(recipeCustomAdapter);

        /*
         * When the search button is clicked get the text from the search edit text ,
         * then pass it as a search keyword in the request url and then check for
         * internet connectivity before starting the loader and setting the searching boolean to true.
         */
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchSubject = searchEditText.getText().toString().replaceAll(" ", "");
                REQUEST_URL = "http://api2.bigoven.com/recipes?title_kw=" + searchSubject + "&api_key=axV15293h59oU9Z853fw48CmI1H1Js";

                networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    recipeCustomAdapter.clear();
                    emptyView.setText("");
                    searching = true;
                    loadingIndicator.setVisibility(View.VISIBLE);
                    loaderManager = getLoaderManager();
                    loaderManager.restartLoader(RECIPE_LOADER_ID, null, RecipeCatalog.this);
                } else {
                    loadingIndicator.setVisibility(View.GONE);
                    recipeCustomAdapter.clear();
                    emptyView.setText(R.string.No_Internet);
                }
            }
        });

        /*
         * When a recipe list item is clicked , get the recipe at the clicked position and
         * then get its id and pass it to the recipe info activity to get its information and again
         * check for network connectivity before starting the recipe info activity.
         */
        recipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Recipe chosenRecipe = (Recipe) adapterView.getItemAtPosition(position);
                String recipeID = chosenRecipe.getRecipeID();
                Intent intent = new Intent(RecipeCatalog.this, RecipeInfo.class);
                intent.putExtra("recipeID", recipeID);
                networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_catalog_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                // when refresh is pressed check for network connectivity and then restart the loader again.
                networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    recipeCustomAdapter.clear();
                    emptyView.setText("");
                    loadingIndicator.setVisibility(View.VISIBLE);
                    loaderManager = getLoaderManager();
                    loaderManager.restartLoader(RECIPE_LOADER_ID, null, RecipeCatalog.this);
                    return true;
                } else {
                    recipeCustomAdapter.clear();
                    emptyView.setText(R.string.No_Internet);
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Recipe>> onCreateLoader(int i, Bundle bundle) {
        // Create a new recipe loader for the given URL
        return new RecipeLoader(this, REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> recipeData) {

        // Hide the loading indicator.
        loadingIndicator.setVisibility(View.GONE);

        if (searching) {
            emptyView.setText(R.string.No_Results);
            searching = false;
        } else {
            emptyView.setText(R.string.Start_Search);
        }

        // Clear the recipe custom adapter of previous recipe data
        recipeCustomAdapter.clear();

        // If there is a valid list of recipes, then add them to the recipe custom adapter data set.
        if (recipeData != null && !recipeData.isEmpty()) {
            recipeCustomAdapter.addAll(recipeData);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Recipe>> loader) {

        // Loader reset, so we can clear out our existing data from the recipe custom adapter.
        recipeCustomAdapter.clear();
    }
}
