package com.jeongwhanchoi.recipeassistant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.gc.materialdesign.views.ButtonFloat;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.jeongwhanchoi.jeongwhanchoi_listview.NDListview;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;

import java.util.List;

import static com.jeongwhanchoi.recipeassistant.IngredientItem.getIngredients;

/**
 * Created by jeongwhanchoi on 11/04/2018.
 */
public class ShoppingListFragment extends Fragment {
    Context context;
    NDListview ingredientListView;
    ShoppingListAdapter shoppingListAdapter;
    RelativeLayout empty;
    ButtonFloat buttonFloat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shoppinglist, container, false);
        ingredientListView = (NDListview) rootView.findViewById(R.id.listview_ingredients);
        empty = (RelativeLayout) rootView.findViewById(R.id.empty);
        buttonFloat = (ButtonFloat) rootView.findViewById(R.id.buttonFloat);
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();

        //set title
        getActivity().setTitle(getString(R.string.shoppinglist_page_title));

        //set Float '+' button icon
        buttonFloat.setDrawableIcon(
                new IconicsDrawable(getContext())
                        .icon(FontAwesome.Icon.faw_plus)
                        .color(ContextCompat.getColor(context, R.color.md_white_1000))
                        .sizeDp(18));

        //set Float '+' button on click listener
        buttonFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddItemToShoppingListDialog();
            }
        });

        //load ingredient list
        createIngredientList(getContext());
    }


    /**
     * open a dialog which asks the user to add an item to shopping cart
     */
    public void openAddItemToShoppingListDialog() {
        Alert AddItemAlert = new Alert();
        AddItemAlert.DisplayEditText(getString(R.string.add_item_dialog_title), getString(R.string.add_item_dialog_description), getString(R.string.add_item_dialog_hint), getString(R.string.Alert_accept), getString(R.string.Alert_cancel), getActivity());
        AddItemAlert.setPositiveButtonListener(new Alert.PositiveButtonListener() {
            @Override
            public void onPositiveButton(String input) {
                //add to shopping cart
                //Save.addToArray(input, "shopping_list", getContext());
                IngredientItem.addIngredient(context, input, getString(R.string.add_item_other));
                createIngredientList(getContext());
            }
        });
        AddItemAlert.show(getActivity().getSupportFragmentManager(), getString(R.string.add_item_dialog_title));
    }

    /**
     * Creates list of ingredients
     *
     * @param context
     */
    public void createIngredientList(final Context context) {
        List<IngredientItem> items = getIngredients(context);
        if (items != null) {
            //create List Adapter
            shoppingListAdapter = new ShoppingListAdapter(context);

            //add ingredients
            for (int i = 0; i < items.size(); i++) {

                //add item to list adapter
                shoppingListAdapter.add(items.get(i));
            }


            //set the adapter
            ingredientListView.setAdapter(shoppingListAdapter);
            ingredientListView.setEmptyView(empty);

            //enable drag and drop
            // ingredientListView.enableDragAndDrop(R.id.list_row_draganddrop_touchview);

            //enable swipe to delete item with undo
            ingredientListView.enableSwipeUndo(new OnDismissCallback() {
                @Override
                public void onDismiss(@NonNull ViewGroup listView, @NonNull int[] reverseSortedPositions) {
                    for (int position : reverseSortedPositions) {
                        List<IngredientItem> items = getIngredients(context);
                        if (items.get(position).isSection) {
                            IngredientItem.deleteAllRecipeIngredients(context, items.get(position).text1);
                        } else {
                            IngredientItem.deleteIngredient(context, items.get(position).text1, items.get(position).parentRecipe);
                        }
                    }
                    createIngredientList(context);
                }
            });
        }
    }


    /**
     * Share cart on social media
     */
    public void share(Activity activity) {
        String str_ingredients = "";
        List<IngredientItem> items = getIngredients(context);
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isSection)
                str_ingredients += items.get(i).text1 + "\r\n";
            else
                str_ingredients += " + " + items.get(i).text1 + "\r\n";
        }
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, str_ingredients);
        sendIntent.setType("text/plain");
        activity.startActivity(Intent.createChooser(sendIntent, "Share via"));
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.shoppinglist_menu, menu);

        //set share icon from FontAwsome
        menu.findItem(R.id.share).setIcon(
                new IconicsDrawable(context)
                        .icon(FontAwesome.Icon.faw_share_alt)
                        .color(ContextCompat.getColor(context, R.color.md_white_1000))
                        .sizeDp(18));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            //handle clear all button in option menu
            case R.id.clear:
                //Save.removeArray("shopping_list", getContext());
                IngredientItem.deleteAllCart(context);
                ingredientListView.adapter.clear();

                return true;
            //handle share button
            case R.id.share:
                share(getActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        List<IngredientItem> items = getIngredients(context);
        for (int i = 0; i < items.size(); i++) {
            ingredientListView.dismiss(i);
        }
    }
}
