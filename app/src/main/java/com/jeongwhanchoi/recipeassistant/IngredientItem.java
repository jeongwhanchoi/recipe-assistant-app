package com.jeongwhanchoi.recipeassistant;

import android.content.Context;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.jeongwhanchoi.recipeassistant.Save.loadArray;

/**
 * A single ingredient item to be used in a recipe
 */
public class IngredientItem {

    public String text1;
    public CompoundButton.OnCheckedChangeListener onCheckedChangeListener;
    public TextView.OnClickListener onCartClickListener;
    public int id;

    public boolean isSection = false;
    public String parentRecipe = "";

    public IngredientItem(String text) {
        this.text1 = text;
    }

    /**
     * Creates an ingredient with a checkbox
     *
     * @param text                    - ingridient name
     * @param onCheckedChangeListener - checkbox listener
     */
    public IngredientItem(String text, CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        this.text1 = text;
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    /**
     * Add checkbox
     *
     * @param onCheckedChangeListener
     * @return
     */
    public IngredientItem setCheckbox(CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
        return this;
    }

    /**
     * Add a cart to the Ingredient
     *
     * @param onCartClickListener
     * @return
     */
    public IngredientItem setCart(TextView.OnClickListener onCartClickListener) {
        this.onCartClickListener = onCartClickListener;
        return this;
    }


    //Shopping cart related functions---------------------------------------------------------------


    /**
     * Add an ingredient to Cart
     *
     * @param context
     * @param ingredientName
     * @param RecipeName
     */
    public static void addIngredient(Context context, String ingredientName, String RecipeName) {
        Save.addToArray(ingredientName, "shopping_list_" + RecipeName, context);

        String[] headers = loadArray("sl_headers", context);
        if (headers != null) {
            if (Arrays.asList(headers).contains("shopping_list_" + RecipeName)) {
                return;
            }
        }
        Save.addToArray("shopping_list_" + RecipeName, "sl_headers", context);
    }

    /**
     * Get all recipe headers from cart. the recipe headers wil be in this format: shopping_list_+RecipeName
     *
     * @param context
     * @return
     */
    public static String[] getAllRecipesInCart(Context context) {
        return loadArray("sl_headers", context);
    }

    /**
     * Get all ingredients in a recipe subheader from cart.
     *
     * @param context
     * @param RecipeName
     * @return
     */
    public static String[] getAllRecipeIngredients(Context context, String RecipeName) {
        return loadArray("shopping_list_" + RecipeName, context);
    }

    /**
     * Get the name of a recipe from a recipe header. Removes the leading 'shopping_list_'
     *
     * @param RecipeName
     * @return
     */
    public static String getRecipeName(String RecipeName) {
        return RecipeName.substring(14);
    }

    /**
     * Delete all ingredients of a particular recipe. Also remove the recipe header itself
     *
     * @param context
     * @param RecipeName
     */
    public static void deleteAllRecipeIngredients(Context context, String RecipeName) {
        Save.removeArray("shopping_list_" + RecipeName, context);
        String[] recipes = getAllRecipesInCart(context);
        for (int i = 0; i < recipes.length; i++) {
            if (RecipeName.equals(getRecipeName(recipes[i]))) {
                Save.removeFromArray(i, "sl_headers", context);
            }
        }
    }

    /**
     * Delete a single ingredient from a recipe header. If the recipe header is empty, delete as well
     *
     * @param context
     * @param ingredientName
     * @param RecipeName
     */
    public static void deleteIngredient(Context context, String ingredientName, String RecipeName) {
        String[] ingredients = getAllRecipeIngredients(context, RecipeName);
        for (int i = 0; i < ingredients.length; i++) {
            if (ingredientName == ingredients[i]) {
                Save.removeFromArray(i, "shopping_list_" + RecipeName, context);
            }
        }
        ingredients = getAllRecipeIngredients(context, RecipeName);
        //delete whole recipe if empty
        if (ingredients.length < 1)
            Save.removeArray("shopping_list_" + RecipeName, context);
    }

    /**
     * Remove all items from cart
     * @param context
     */
    public static void deleteAllCart(Context context) {
        String[] recipes = getAllRecipesInCart(context);
        for (int i = 0; i < recipes.length; i++) {
            Save.removeArray(recipes[i], context);
        }
        Save.removeArray("sl_headers", context);
    }


    /**
     * Get all ingredients and recipe headers
     *
     * @param context
     * @return
     */
    public static List<IngredientItem> getIngredients(Context context) {
        List<IngredientItem> items = new ArrayList<>();
        String[] headers = getAllRecipesInCart(context);
        for (int i = 0; i < headers.length; i++) {
            String[] ingredients = getAllRecipeIngredients(context, getRecipeName(headers[i]));
            //add section header
            IngredientItem temp = new IngredientItem(getRecipeName(headers[i]));
            temp.isSection = true;
            items.add(temp);
            for (int ingredient_i = 0; ingredient_i < ingredients.length; ingredient_i++) {
                temp = new IngredientItem(ingredients[ingredient_i]);
                temp.parentRecipe = getRecipeName(headers[i]);
                items.add(temp);
            }
        }
        return items;
    }

}