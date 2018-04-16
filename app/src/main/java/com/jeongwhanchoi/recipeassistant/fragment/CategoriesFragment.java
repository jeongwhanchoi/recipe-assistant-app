package com.jeongwhanchoi.recipeassistant.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.jeongwhanchoi.jeongwhanchoi_listview.NDListview;
import com.jeongwhanchoi.jeongwhanchoi_listview.SingleLineItem;
import com.jeongwhanchoi.jeongwhanchoi_listview.SingleLineListAdapter;
import com.jeongwhanchoi.recipeassistant.Category;
import com.jeongwhanchoi.recipeassistant.R;

import java.util.List;

/**
 * Created by jeongwhanchoi on 11/04/2018.
 * A fragment which displays a list of all the categories in text format.
 */
public class CategoriesFragment extends Fragment {
    Context context;
    NDListview categoryListView;
    SingleLineListAdapter categoryListViewAdapter;
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_categorylist, container, false);
        categoryListView = (NDListview) rootView.findViewById(R.id.listview_categories);
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();

        //set title
        getActivity().setTitle(getString(R.string.categories_page_title));

        //load small toolbar progress bar and make it visible
        progressBar = (ProgressBar) getActivity().findViewById(R.id.progress_spinner);
        progressBar.setVisibility(View.VISIBLE);

        //load categories from server
        Category.loadCategories(context, "", new Category.onCategoriesDownloadedListener() {
            @Override
            public void onCategoriesDownloaded(List<Category> categories) {
                createCatagoryList(getContext(), categories);
                progressBar.setVisibility(View.GONE);
            }
        });
    }


    /**
     * Generates the category list and fills it up with the downloaded categories
     * @param context
     * @param categories
     */
    public void createCatagoryList(final Context context, final List<Category> categories) {
        if (categories != null) {
            //create List Adapter
            categoryListViewAdapter = new SingleLineListAdapter(context);

            //categories to the list
            for (int i = 0; i < categories.size(); i++) {
                //create single line item and add it to adapter
                SingleLineItem item = new SingleLineItem(categories.get(i).name);
                categoryListViewAdapter.add(item);
            }

            //set the adapter
            categoryListView.setAdapter(categoryListViewAdapter);
        }

        //callback when an item is clicked
        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                //open up the fragment that shows all recipes in the category
                Bundle b = new Bundle();
                b.putInt("Category_id", categories.get(position).id);
                Fragment f = new CategoryRecipesFragment();
                f.setArguments(b);
                changeFragment(f);

            }
        });
    }

    /**
     * Cahnges the fragment of the parent activity. Replace itself.
     * @param fragment - Fragment to open up
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
        //display no options menu
        menu.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressBar != null)
            progressBar.setVisibility(View.GONE);
    }
}
