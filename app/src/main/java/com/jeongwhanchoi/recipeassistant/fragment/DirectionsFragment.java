package com.jeongwhanchoi.recipeassistant.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.jeongwhanchoi.recipeassistant.Functions;
import com.jeongwhanchoi.recipeassistant.R;
import com.jeongwhanchoi.recipeassistant.Recipe;

/**
 * Created by jeongwhanchoi on 11/04/2018.
 * A Fragment to Display ingredients. Uses a WebView to show Directions in HTML format.
 * Supports videos, images, javascript
 */
public class DirectionsFragment extends Fragment {

    WebView webViewDirections;
    TextView difficulty_textview, preperationTime_textview, cookingTime_textview;
    int recipeId;
    int time;

    /**
     * Required empty public constructor
     */
    public DirectionsFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_directions, container, false);

        //preparation, cooking time and difficulty
        preperationTime_textview = (TextView) rootView.findViewById(R.id.preparation_time);
        cookingTime_textview = (TextView) rootView.findViewById(R.id.cooking_time);
        difficulty_textview = (TextView) rootView.findViewById(R.id.difficulty);

        //get webView Resource
        webViewDirections = (WebView) rootView.findViewById(R.id.webview_directions);

        //enable javascript
        webViewDirections.getSettings().setJavaScriptEnabled(true);

        //load recipe
        Recipe.loadRecipe(getActivity(), recipeId, new Recipe.onRecipeDownloadedListener() {
            @Override
            public void onRecipeDownloaded(Recipe recipe) {
                webViewDirections.loadData(Functions.HTMLTemplate(recipe.directions), "text/html; charset=utf-8", "utf-8");
                preperationTime_textview.setText(recipe.prep_time+ getString(R.string.minutes));
                cookingTime_textview.setText(recipe.cook_time+ getString(R.string.minutes));
                difficulty_textview.setText(""+recipe.difficulty);
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    /* Timer
    * */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //load recipe
        Recipe.loadRecipe(getActivity(), recipeId, new Recipe.onRecipeDownloadedListener() {
            @Override
            public void onRecipeDownloaded(Recipe recipe) {
                time = recipe.cook_time;
            }
        });

        cookingTime_textview.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(time > 0)
                {
                    startTimerActivity(time* 60);
//                startTimerActivity(10 * 60);
                }
            }
        });

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        webViewDirections.destroy();
    }

    private void startTimerActivity(int length)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            try
            {
                Intent intent = new Intent(AlarmClock.ACTION_SET_TIMER);
                intent.putExtra(AlarmClock.EXTRA_LENGTH, length);
                startActivity(intent);
            }
            catch(android.content.ActivityNotFoundException e)
            {
                // can't start activity
            }
        }
    }

}
