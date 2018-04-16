package com.jeongwhanchoi.recipeassistant;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by jeongwhanchoi on 28/09/2016.
 * A Fragment to Display ingredients. Uses a WebView to show Directions in HTML format.
 * Supports videos, images, javascript
 */
public class NutritionFragment extends Fragment {

    // WebView webViewDirections;
    int recipeId;
    public int[] nutrition = new int[16];
    TextView[] nutritionTextVews = new TextView[16];

    TextView fat_daily_value_textview;
    TextView saturated_fat_daily_value_textview;
    TextView trans_fat_daily_value_textview;
    TextView cholesterol_daily_value_textview;
    TextView sodium_daily_value_textview;
    TextView total_carbohydrate_daily_value_textview;
    TextView dietary_fiber_daily_value_textview;

    /**
     * Required empty public constructor
     */
    public NutritionFragment() {
    }

    public static NutritionFragment newInstance(int[] nutrition) {
        NutritionFragment f = new NutritionFragment();
        Bundle args = new Bundle();
        args.putIntArray("nutrition", nutrition);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //get recipe nutrition
        nutrition = getArguments().getIntArray("nutrition");

        //get rootview
        View rootView = inflater.inflate(R.layout.fragment_nutrition, container, false);

        //nutrition textviews
        nutritionTextVews[Recipe.NUTR_CALORIES] = (TextView) rootView.findViewById(R.id.calories);
        nutritionTextVews[Recipe.NUTR_CALORIES_FROM_FAT] = (TextView) rootView.findViewById(R.id.calories_from_fat);
        nutritionTextVews[Recipe.NUTR_TOTAL_FAT] = (TextView) rootView.findViewById(R.id.total_fat);
        nutritionTextVews[Recipe.NUTR_SATURATED_FAT] = (TextView) rootView.findViewById(R.id.saturated_fat);
        nutritionTextVews[Recipe.NUTR_TRANS_FAT] = (TextView) rootView.findViewById(R.id.trans_fat);
        nutritionTextVews[Recipe.NUTR_CHOLESTEROL] = (TextView) rootView.findViewById(R.id.cholesterol);
        nutritionTextVews[Recipe.NUTR_SODIUM] = (TextView) rootView.findViewById(R.id.sodium);
        nutritionTextVews[Recipe.NUTR_PROTEIN] = (TextView) rootView.findViewById(R.id.protein);
        nutritionTextVews[Recipe.NUTR_TOTAL_CARBOHYDRATE] = (TextView) rootView.findViewById(R.id.total_carbohydrate);
        nutritionTextVews[Recipe.NUTR_DIETARY_FIBER] = (TextView) rootView.findViewById(R.id.dietary_fiber);
        nutritionTextVews[Recipe.NUTR_SUGARS] = (TextView) rootView.findViewById(R.id.sugars);
        nutritionTextVews[Recipe.NUTR_VITAMIN_A] = (TextView) rootView.findViewById(R.id.vitamin_a_percent);
        nutritionTextVews[Recipe.NUTR_VITAMIN_C] = (TextView) rootView.findViewById(R.id.vitamin_c_percent);
        nutritionTextVews[Recipe.NUTR_CALCIUM] = (TextView) rootView.findViewById(R.id.calcium_percent);
        nutritionTextVews[Recipe.NUTR_IRON] = (TextView) rootView.findViewById(R.id.iron_percent);

        //percent textviews
        fat_daily_value_textview = (TextView) rootView.findViewById(R.id.total_fat_percent);
        saturated_fat_daily_value_textview = (TextView) rootView.findViewById(R.id.saturated_fat_percent);
        cholesterol_daily_value_textview = (TextView) rootView.findViewById(R.id.cholesterol_percent);
        sodium_daily_value_textview = (TextView) rootView.findViewById(R.id.sodium_percent);
        total_carbohydrate_daily_value_textview = (TextView) rootView.findViewById(R.id.total_carbohydrate_percent);
        dietary_fiber_daily_value_textview = (TextView) rootView.findViewById(R.id.dietary_fiber_percent);

        //set nutrition textview
        nutritionTextVews[Recipe.NUTR_CALORIES].setText(getString(R.string.calories) + " " + nutrition[Recipe.NUTR_CALORIES]);
        nutritionTextVews[Recipe.NUTR_CALORIES_FROM_FAT].setText(getString(R.string.calories_from_fat) + " " + nutrition[Recipe.NUTR_CALORIES_FROM_FAT]);
        nutritionTextVews[Recipe.NUTR_TOTAL_FAT].setText(getString(R.string.total_fat) + " " + nutrition[Recipe.NUTR_TOTAL_FAT] + getString(R.string.gram));
        nutritionTextVews[Recipe.NUTR_SATURATED_FAT].setText(getString(R.string.saturated_fat) + " " + nutrition[Recipe.NUTR_SATURATED_FAT] + getString(R.string.gram));
        nutritionTextVews[Recipe.NUTR_TRANS_FAT].setText(getString(R.string.trans_fat) + " " + nutrition[Recipe.NUTR_TRANS_FAT] + getString(R.string.gram));
        nutritionTextVews[Recipe.NUTR_CHOLESTEROL].setText(getString(R.string.cholesterol) + " " + nutrition[Recipe.NUTR_CHOLESTEROL] + getString(R.string.milligram));
        nutritionTextVews[Recipe.NUTR_SODIUM].setText(getString(R.string.sodium) + " " + nutrition[Recipe.NUTR_SODIUM] + getString(R.string.milligram));
        nutritionTextVews[Recipe.NUTR_TOTAL_CARBOHYDRATE].setText(getString(R.string.total_carbohydrate) + " " + nutrition[Recipe.NUTR_TOTAL_CARBOHYDRATE] + getString(R.string.gram));
        nutritionTextVews[Recipe.NUTR_DIETARY_FIBER].setText(getString(R.string.dietary_fiber) + " " + nutrition[Recipe.NUTR_DIETARY_FIBER] + getString(R.string.gram));
        nutritionTextVews[Recipe.NUTR_SUGARS].setText(getString(R.string.sugars) + " " + nutrition[Recipe.NUTR_SUGARS] + getString(R.string.gram));
        nutritionTextVews[Recipe.NUTR_PROTEIN].setText(getString(R.string.protein) + " " + nutrition[Recipe.NUTR_PROTEIN] + getString(R.string.gram));
        nutritionTextVews[Recipe.NUTR_VITAMIN_A].setText(nutrition[Recipe.NUTR_VITAMIN_A] + getString(R.string.percent));
        nutritionTextVews[Recipe.NUTR_VITAMIN_C].setText(nutrition[Recipe.NUTR_VITAMIN_C] + getString(R.string.percent));
        nutritionTextVews[Recipe.NUTR_CALCIUM].setText(nutrition[Recipe.NUTR_CALCIUM] + getString(R.string.percent));
        nutritionTextVews[Recipe.NUTR_IRON].setText(nutrition[Recipe.NUTR_IRON] + getString(R.string.percent));

        //daily value percent
        fat_daily_value_textview.setText(Math.round((nutrition[Recipe.NUTR_TOTAL_FAT] / Configurations.FAT_DAILY_VALUE) * 100) + getString(R.string.percent));
        saturated_fat_daily_value_textview.setText(Math.round((nutrition[Recipe.NUTR_SATURATED_FAT] / Configurations.SATURATED_FAT_DAILY_VALUE) * 100) + getString(R.string.percent));
        cholesterol_daily_value_textview.setText(Math.round((nutrition[Recipe.NUTR_CHOLESTEROL] / Configurations.CHOLESTEROL_DAILY_VALUE) * 100) + getString(R.string.percent));
        sodium_daily_value_textview.setText(Math.round((nutrition[Recipe.NUTR_SODIUM] / Configurations.SODIUM_DAILY_VALUE) * 100) + getString(R.string.percent));
        total_carbohydrate_daily_value_textview.setText(Math.round((nutrition[Recipe.NUTR_TOTAL_CARBOHYDRATE] / Configurations.TOTAL_CARBOHYDRATE_DAILY_VALUE) * 100) + getString(R.string.percent));
        dietary_fiber_daily_value_textview.setText(Math.round((nutrition[Recipe.NUTR_DIETARY_FIBER] / Configurations.DIETARY_FIBER_DAILY_VALUE) * 100) + getString(R.string.percent));

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        // webViewDirections.destroy();
    }


}
