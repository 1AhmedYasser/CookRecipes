package com.example.android.cookrecipes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link RecipeCustomAdapter} is an {@link ArrayAdapter} that provides the layout for each recipe list item
 * based on a data source, which is a list of {@link Recipe} objects.
 */
public class RecipeCustomAdapter extends ArrayAdapter<Recipe> {

    /**
     * A {@link RecipeCustomAdapter} Constructor.
     *
     * @param context is the current app context.
     * @param recipes : the list of recipes to be displayed to the user.
     */
    public RecipeCustomAdapter(Context context, List<Recipe> recipes) {
        super(context, 0, recipes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view.
        View recipeItemView = convertView;
        ViewHolder viewHolder;
        if (recipeItemView == null) {
            recipeItemView = LayoutInflater.from(getContext()).inflate(R.layout.recipe_list_item, parent, false);
            viewHolder = new ViewHolder(recipeItemView);
            recipeItemView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) recipeItemView.getTag();
        }

        // Get the recipe object located at this position in the list.
        Recipe currentRecipe = getItem(position);

        // Set the list animation on each item of the list.
        viewHolder.itemLinearLayout.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.list_item_animation));

        // Get the recipe title ,category ,recipe rating and image from the currentRecipe object and set the views with it.
        viewHolder.recipeTitle.setText(currentRecipe.getRecipeTitle());
        viewHolder.recipeCategory.setText(getContext().getString(R.string.category) + " " + currentRecipe.getRecipeCategory());
        String formattedRating = formatRating(Double.valueOf(currentRecipe.getRecipeRating()));
        viewHolder.recipeRating.setText(getContext().getString(R.string.rating) + " " + formattedRating);
        Glide.with(getContext()).load(currentRecipe.getRecipeImageUrl()).into(viewHolder.recipeImage);

        // returns the whole recipe item layout.
        return recipeItemView;
    }

    /*
    * A helper method that format the recipe rating to 1 decimal point
    */
    private String formatRating(double recipeRating) {
        DecimalFormat ratingFormat = new DecimalFormat("0.0");
        return ratingFormat.format(recipeRating);
    }

    class ViewHolder {

        // Get recipe title ,category ,recipe rating text views and the recipe image view in the recipe_list_item.xml layout.
        @BindView(R.id.recipe_title_text)
        TextView recipeTitle;

        @BindView(R.id.recipe_category_text)
        TextView recipeCategory;

        @BindView(R.id.recipe_rating_text)
        TextView recipeRating;

        @BindView(R.id.recipe_img)
        ImageView recipeImage;

        @BindView(R.id.item_linearLayout)
        LinearLayout itemLinearLayout;

        ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }

}
