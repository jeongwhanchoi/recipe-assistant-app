package com.jeongwhanchoi.recipeassistant;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import java.util.List;

/**
 * Created by jeongwhanchoi on 11/04/2018.
 * A fragment which displays a list of all the categories in text and image format.
 */
public class CategoryTextAndImageFragment extends Fragment {
    Context context;
    private EmptyRecyclerView mRecyclerView;
    private EmptyRecyclerView.Adapter mAdapter;
    private EmptyRecyclerView.LayoutManager mLayoutManager;
    SwipeRefreshLayout swipeLayout;


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

        //set title
        getActivity().setTitle(getString(R.string.categories_page_title));

        //set RecycleView
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);

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
     * Reloads categories from server and shows them on screen
     */
    public void refresh() {
        Category.loadCategories(context, "", new Category.onCategoriesDownloadedListener() {
            @Override
            public void onCategoriesDownloaded(List<Category> categories) {
                swipeLayout.setRefreshing(false);
                setCategories(categories);
            }
        });
    }


    /**
     * Shows loaded categories on screen
     *
     * @param categories
     */
    public void setCategories(final List<Category> categories) {
        mAdapter = new CategoryAdapter(categories, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle b = new Bundle();
                b.putInt("Category_id", categories.get(i).id);
                Fragment f = new CategoryRecipesFragment();
                f.setArguments(b);
                changeFragment(f);

            }
        }, context);
        mRecyclerView.setAdapter(mAdapter);
    }


    /**
     * Change parent fragment. Replace itself.
     * @param  - Fragment to open up.
     */
    public void changeFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFragment, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();

    }
}
