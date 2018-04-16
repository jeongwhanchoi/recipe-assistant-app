package com.jeongwhanchoi.recipeassistant.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.jeongwhanchoi.recipeassistant.Category;
import com.jeongwhanchoi.recipeassistant.Configurations;
import com.jeongwhanchoi.recipeassistant.EmptyRecyclerView;
import com.jeongwhanchoi.recipeassistant.EndlessRecyclerViewScrollListener;
import com.jeongwhanchoi.recipeassistant.MainActivity;
import com.jeongwhanchoi.recipeassistant.R;
import com.jeongwhanchoi.recipeassistant.Recipe;
import com.jeongwhanchoi.recipeassistant.adapter.RecipeAdapter;
import com.jeongwhanchoi.recipeassistant.SingleRecipeActivity;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.List;

/**
 * Created by jeongwhanchoi on 11/04/2018.
 */
public class CategoryRecipesFragment extends Fragment {
    Context context;
    private EmptyRecyclerView mRecyclerView;
    private EmptyRecyclerView.Adapter mAdapter;
    private EmptyRecyclerView.LayoutManager mLayoutManager;
    SwipeRefreshLayout swipeLayout;
    int categoryId = 0;

    public final static int LIST_INITIAL_LOAD = 10;
    public final static int LIST_INITIAL_LOAD_MORE_ONSCROLL = 5;

    List<Recipe> recipes;
    EndlessRecyclerViewScrollListener scrollListener;
    String searchText="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = (EmptyRecyclerView) rootView.findViewById(R.id.recipesList);
        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeToRefresh);
        RelativeLayout empty = (RelativeLayout) rootView.findViewById(R.id.empty);
        mRecyclerView.setEmptyView(empty);
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();

        //set RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manage
        int spanCount = 1;
        if (Configurations.LIST_MENU_TYPE == Configurations.LIST_FULLWIDTH) {
            mLayoutManager = new LinearLayoutManager(context);

        } else if (Configurations.LIST_MENU_TYPE == Configurations.LIST_2COLUMNS) {
            mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            spanCount = ((StaggeredGridLayoutManager) mLayoutManager).getSpanCount();
        }
        mRecyclerView.setLayoutManager(mLayoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager, spanCount) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                System.out.println("load more" + totalItemsCount);
                loadMore(totalItemsCount);
            }
        };
        mRecyclerView.addOnScrollListener(scrollListener);


        //get category Id from
        categoryId = getArguments().getInt("Category_id", 0);

        //load category from server
        Category.getCategoryName(context, categoryId, new Category.onNameFoundListener() {
            @Override
            public void onNameFound(String name) {
                getActivity().setTitle(name);
            }
        });

        // Swipe to Refresh
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        refresh();
    }

    /**
     * Refresh recipe list from server
     */
    public void refresh() {
        Recipe.loadRecipes(getActivity(), 0, LIST_INITIAL_LOAD, searchText, "" + categoryId, new Recipe.onRecipesDownloadedListener() {
            @Override
            public void onRecipesDownloaded(List<Recipe> recipes) {
                swipeLayout.setRefreshing(false);
                setRecipes(recipes);
            }
        });
    }

    /**
     * Load more recipes from server
     *
     * @param first - start loading from this recipe
     */
    public void loadMore(int first) {
        Recipe.loadRecipes(getActivity(), first, LIST_INITIAL_LOAD_MORE_ONSCROLL, searchText, "" + categoryId, new Recipe.onRecipesDownloadedListener() {
            @Override
            public void onRecipesDownloaded(List<Recipe> recipes) {
                swipeLayout.setRefreshing(false);
                ((RecipeAdapter) mAdapter).addItems(recipes);
                mRecyclerView.swapAdapter(mAdapter, false);

            }
        });
    }


    /**
     * Show recipes to screen
     *
     * @param recipes_loaded - list of recipes to show
     */
    public void setRecipes(final List<Recipe> recipes_loaded) {
        this.recipes = recipes_loaded;
        mAdapter = new RecipeAdapter(recipes, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //open recipe in new activity on click
                Intent intent = new Intent(context, SingleRecipeActivity.class);
                intent.putExtra("RECIPE_ID", recipes.get(i).id);
                startActivity(intent);
            }
        }, context);
        //mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.swapAdapter(mAdapter, false);
        scrollListener.resetState();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //clear options menu
        menu.clear();

        //re-initialise menu
        inflater.inflate(R.menu.options_menu, menu);

        //set search icon using FontAwesome
        menu.findItem(R.id.search).setIcon(
                new IconicsDrawable(getContext())
                        .icon(FontAwesome.Icon.faw_search)
                        .color(ContextCompat.getColor(context, R.color.md_white_1000))
                        .sizeDp(18));

        //set search feature
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = new SearchView(((MainActivity) context).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchText=newText;
                //Search for recipes on server when text changed
                Recipe.loadRecipes(getActivity(), 0, 100, newText, "" + categoryId, new Recipe.onRecipesDownloadedListener() {
                    @Override
                    public void onRecipesDownloaded(List<Recipe> recipes) {
                        setRecipes(recipes);
                    }
                });
                return false;
            }
        });
    }
}
