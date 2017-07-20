package com.example.android.cookrecipes;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * {@link RecipeLoader} class Loads a list of recipes by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class RecipeLoader extends AsyncTaskLoader {

    /**
     * The big oven api query url
     */
    private String mRecipeQueryUrl;

    /**
     * Loader constructor.
     *
     * @param context        : The context of the activity.
     * @param recipeQueryUrl : The url to load data from.
     */
    public RecipeLoader(Context context, String recipeQueryUrl) {
        super(context);
        mRecipeQueryUrl = recipeQueryUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    @Override
    public Object loadInBackground() {

        // if the url is empty return and skip fetching the recipe info.
        if (mRecipeQueryUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of recipes.
        List<Recipe> recipes = QueryHandler.fetchRecipeInfo(mRecipeQueryUrl);
        return recipes;
    }
}
