package com.example.android.cookrecipes;

/**
 * {@link Recipe} class represents a Recipe.
 * It contains the recipe Id, title, category, Sub Category, Rating, Image Url,
 * description, ingredients and instructions.
 */
public class Recipe {

    // Declare Recipe data as strings.
    private String mRecipeID;
    private String mRecipeTitle;
    private String mRecipeCategory;
    private String mRecipeSubCategory;
    private String mRecipeRating;
    private String mRecipeImageUrl;
    private String mRecipeDescription;
    private String mRecipeIngredients;
    private String mRecipeInstructions;

    /**
     * {@link Recipe} class Constructor : Initialize the Recipe class variables by the passed information
     * when a new Recipe object is created.
     * Used when populating the recipe list.
     */
    public Recipe(String mRecipeID, String mRecipeTitle, String mRecipeCategory, String mRecipeRating, String mRecipeImageUrl) {
        this.mRecipeID = mRecipeID;
        this.mRecipeTitle = mRecipeTitle;
        this.mRecipeCategory = mRecipeCategory;
        this.mRecipeRating = mRecipeRating;
        this.mRecipeImageUrl = mRecipeImageUrl;
    }

    /**
     * {@link Recipe} class Constructor : Initialize the Recipe class variables by the passed information
     * when a new Recipe object is created.
     * Used when getting a particular recipe information.
     */
    public Recipe(String mRecipeTitle, String mRecipeImageUrl, String mRecipeDescription, String mRecipeCategory,
                  String mRecipeSubCategory, String mRecipeRating, String mRecipeIngredients, String mRecipeInstructions) {
        this.mRecipeTitle = mRecipeTitle;
        this.mRecipeImageUrl = mRecipeImageUrl;
        this.mRecipeDescription = mRecipeDescription;
        this.mRecipeCategory = mRecipeCategory;
        this.mRecipeSubCategory = mRecipeSubCategory;
        this.mRecipeRating = mRecipeRating;
        this.mRecipeIngredients = mRecipeIngredients;
        this.mRecipeInstructions = mRecipeInstructions;

    }

    /**
     * This method returns a string containing the recipe id.
     *
     * @return string : The recipe id.
     */
    public String getRecipeID() {
        return mRecipeID;
    }

    /**
     * This method returns a string containing the recipe title.
     *
     * @return string : The recipe title.
     */
    public String getRecipeTitle() {
        return mRecipeTitle;
    }

    /**
     * This method returns a string containing the recipe category.
     *
     * @return string : The recipe category.
     */
    public String getRecipeCategory() {
        return mRecipeCategory;
    }

    /**
     * This method returns a string containing the recipe rating.
     *
     * @return string : The recipe rating.
     */
    public String getRecipeRating() {
        return mRecipeRating;
    }

    /**
     * This method returns a string containing the recipe image url.
     *
     * @return string : The recipe image url.
     */
    public String getRecipeImageUrl() {
        return mRecipeImageUrl;
    }

    /**
     * This method returns a string containing the recipe sub category.
     *
     * @return string : The recipe sub category.
     */
    public String getRecipeSubCategory() {
        return mRecipeSubCategory;
    }

    /**
     * This method returns a string containing the recipe description.
     *
     * @return string : The recipe description.
     */
    public String getRecipeDescription() {
        return mRecipeDescription;
    }

    /**
     * This method returns a string containing the recipe ingredients.
     *
     * @return string : The recipe ingredients.
     */
    public String getRecipeIngredients() {
        return mRecipeIngredients;
    }

    /**
     * This method returns a string containing the recipe instructions.
     *
     * @return string : The recipe instructions.
     */
    public String getRecipeInstructions() {
        return mRecipeInstructions;
    }
}
