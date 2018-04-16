package com.jeongwhanchoi.recipeassistant.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.jeongwhanchoi.jeongwhanchoi_listview.NDListview;
import com.jeongwhanchoi.recipeassistant.FontManager;
import com.jeongwhanchoi.recipeassistant.IngredientItem;
import com.jeongwhanchoi.recipeassistant.adapter.IngredientListAdapter;
import com.jeongwhanchoi.recipeassistant.R;
import com.jeongwhanchoi.recipeassistant.Recipe;

/**
 * Created by jeongwhanchoi on 11/04/2018.
 */
public class IngredientsFragment extends Fragment {

    NDListview ingredientListView;
    IngredientListAdapter ingredientListViewAdapter;
    int servings = 3;
    NumberPicker servingsPicker;
    TextView servingsTextview;
    Recipe recipe;
    int recipeId;


    /**
     * Required empty public constructor
     */
    public IngredientsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //get recipe id
        recipeId = Recipe.getRecipeIdFromIntent(getActivity().getIntent(), savedInstanceState);

        //get rootview
        View rootView = inflater.inflate(R.layout.fragment_ingredients, container, false);

        //get listView
        ingredientListView = (NDListview) rootView.findViewById(R.id.listview_ingredients);

        //get and set servings
        servingsTextview = (TextView) rootView.findViewById(R.id.textview_servings);
        servingsTextview.setTypeface(FontManager.getFontAwsome(getContext()));
        servingsTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createServingsPicker();
            }
        });



        //load recipes
        Recipe.loadRecipe(getActivity(), recipeId, new Recipe.onRecipeDownloadedListener() {
            @Override
            public void onRecipeDownloaded(Recipe recipeLocal) {
                //set default servings
                setServings(recipeLocal.default_servings);
                recipe = recipeLocal;
                createIngredientList(getContext(), recipe.calculateServings(servings));
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        if (recipeId >= 0)
            savedInstanceState.putInt("RECIPE_ID", recipeId);
        // etc.
    }

    /**
     * Create ingredient list and display
     *
     * @param context
     * @param ingredients - A List of ingridients
     */
    public void createIngredientList(final Context context, String[] ingredients) {
        if (ingredients != null) {
            //create List Adapter
            ingredientListViewAdapter = new IngredientListAdapter(context);

            //add ingredients
            for (int i = 0; i < ingredients.length; i++) {
                //create single line item
                IngredientItem item = new IngredientItem(ingredients[i]);

                final int i_const = i;

                //Handle cart clicks
                item.setCart(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //display info
                        Toast.makeText(context, getString(R.string.shoppingcart_message), Toast.LENGTH_SHORT).show();

                        //add to shopping cart
                        //  Save.addToArray(recipe.calculateServings(servings)[i_const], "shopping_list", getContext());
                        IngredientItem.addIngredient(getContext(), recipe.calculateServings(servings)[i_const], recipe.name);
                    }
                });

                //add item to list adapter
                ingredientListViewAdapter.add(item);
            }

            //set the adapter
            ingredientListView.setAdapter(ingredientListViewAdapter);
            ingredientListView.setHeightBasedOnChildren();
        }
    }


    /**
     * Set number of servings and refresh
     *
     * @param servings - Number of servings
     */
    public void setServings(int servings) {
        this.servings = servings;
        servingsTextview.setText(getContext().getString(R.string.servings, servings));
        if (recipe != null)
            createIngredientList(getContext(), recipe.calculateServings(servings));
    }

    /**
     * Create number of servings picker dialog and show.
     */
    public void createServingsPicker() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View npView = inflater.inflate(R.layout.dialog_servings_picker, null);

        //get and set number picker
        servingsPicker = (NumberPicker) npView.findViewById(R.id.numberpicker_servings);
        servingsPicker.setMaxValue(100);
        servingsPicker.setMinValue(1);
        servingsPicker.setValue(servings);

        //generate dialog
        AlertDialog servingsPickerDialog = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.servings_picker_servings))
                .setView(npView)
                .setPositiveButton(R.string.servings_picker_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                setServings(servingsPicker.getValue());
                            }
                        })
                .setNegativeButton(R.string.servings_picker_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                .create();

        //show dialog
        servingsPickerDialog.show();
    }

}
