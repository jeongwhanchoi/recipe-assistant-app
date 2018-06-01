package com.jeongwhanchoi.recipeassistant.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecloud.pulltozoomview.PullToZoomScrollViewEx;
import com.jeongwhanchoi.recipeassistant.R;
import com.jeongwhanchoi.recipeassistant.Recipe;
import com.jeongwhanchoi.recipeassistant.WrapContentViewPager;
import com.jeongwhanchoi.recipeassistant.listener.OnSwipeTouchListener;
import com.jeongwhanchoi.recipeassistant.fragment.DirectionsFragment;
import com.jeongwhanchoi.recipeassistant.fragment.IngredientsFragment;
import com.jeongwhanchoi.recipeassistant.fragment.NutritionFragment;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * This activity shows a single recipe
 */
public class SingleRecipeActivity extends AppCompatActivity {

    PullToZoomScrollViewEx scrollView;
    WrapContentViewPager viewPager;
    IconicsImageView favoriteBtn;
    Recipe recipe;
    Context context;
    Activity activity;
    TextView recipeName;
    ImageView recipeImage;
    int slideshow_current_image = 0;
    int recipeId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //only portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_single_recipe);
        this.context = this;
        this.activity = this;


        //setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                finish();
            }
        });

        //get scrollview
        scrollView = (PullToZoomScrollViewEx) findViewById(R.id.scroll_view);

        //set zoom, content and header view
        View headView = LayoutInflater.from(this).inflate(R.layout.recipe_head_view, null, false);
        View zoomView = LayoutInflater.from(this).inflate(R.layout.recipe_zoom_view, null, false);
        View contentView = LayoutInflater.from(this).inflate(R.layout.recipe_content_view, null, false);
        scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);


        //set aspect ratio of header image
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 16.0F)));
        scrollView.setHeaderLayoutParams(localObject);

        //get recipe image element
        recipeImage = (ImageView) zoomView.findViewById(R.id.recipe_image);

        recipeImage.setOnTouchListener(new OnSwipeTouchListener(this) {

            @Override
            public void onClick() {
            }

            @Override
            public void onSwipeLeft() {
                //next image
                slideshow_current_image--;
                if (slideshow_current_image < 0)
                    slideshow_current_image = recipe.imageUrl.length - 1;

                //switch image
                Picasso.with(context)
                        .load(recipe.imageUrl[slideshow_current_image])
                        .fit()
                        .placeholder(R.drawable.loading)
                        .into(recipeImage);

            }

            @Override
            public void onSwipeRight() {

                //next image
                slideshow_current_image++;
                if (slideshow_current_image >= recipe.imageUrl.length)
                    slideshow_current_image = 0;

                //switch image
                Picasso.with(context)
                        .load(recipe.imageUrl[slideshow_current_image])
                        .fit()
                        .placeholder(R.drawable.loading)
                        .into(recipeImage);
            }
        });

        //set favorite button
        favoriteBtn = (IconicsImageView) headView.findViewById(R.id.recipe_favorite);
        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recipe.isFavorite(context)) {
                    favoriteBtn.setIcon("faw_star_o");
                    recipe.setFavorite(context, false);
                } else {
                    favoriteBtn.setIcon("faw_star");
                    recipe.setFavorite(context, true);
                }
            }
        });

        //get recipe name element
        recipeName = (TextView) headView.findViewById(R.id.recipe_name);

        //set viewPager (tabs)
        viewPager = (WrapContentViewPager) findViewById(R.id.viewpager);
        //setupViewPager(viewPager);

        //set tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //get recipe id
        recipeId = Recipe.getRecipeIdFromIntent(getIntent(), savedInstanceState);
        if (recipeId < 0)
            finish();

        //load recipe
        Recipe.loadRecipe(this, recipeId, new Recipe.onRecipeDownloadedListener() {
            @Override
            public void onRecipeDownloaded(Recipe recipeLocal) {
                recipeLocal.viewed(activity);
                recipe = recipeLocal;
                setTitle(recipe.name);
                recipeName.setText(recipe.name);
                refreshFavoriteBtn();

                if (recipe.imageUrl != null)
                    if (recipe.imageUrl.length > 0)
                        Picasso.with(context)
                                .load(recipe.imageUrl[0])
                                .fit()
                                .placeholder(R.drawable.loading)
                                .into(recipeImage);

                setupViewPager(viewPager);
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

    /**
     * Refresh Favorite button (star).
     * Checks state from local preferences
     */
    public void refreshFavoriteBtn() {
        if (recipe.isFavorite(context)) {
            favoriteBtn.setIcon("faw_star");
        } else {
            favoriteBtn.setIcon("faw_star_o");
        }
    }

    /**
     * Setup viewpager
     *
     * @param viewPager
     */
    private void setupViewPager(final ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new IngredientsFragment(), getString(R.string.ingredients_title));
        adapter.addFragment(new DirectionsFragment(), getString(R.string.directions_title));
        if (recipe.nutrition[Recipe.NUTR_ENABLE] == 1)
            adapter.addFragment(NutritionFragment.newInstance(recipe.nutrition), getString(R.string.nutrition_title));
        viewPager.setAdapter(adapter);

        //functionality to avoid stopping of side scroll when user moves touch up/down
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            int dragthreshold = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, activity.getResources().getDisplayMetrics());

            int downX;
            int downY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = (int) event.getRawX();
                        downY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int distanceX = Math.abs((int) event.getRawX() - downX);
                        int distanceY = Math.abs((int) event.getRawY() - downY);

                        if (distanceY > distanceX && distanceY > dragthreshold) {
                            viewPager.getParent().requestDisallowInterceptTouchEvent(false);
                            scrollView.getParent().requestDisallowInterceptTouchEvent(true);
                        } else if (distanceX > distanceY && distanceX > dragthreshold) {
                            viewPager.getParent().requestDisallowInterceptTouchEvent(true);
                            scrollView.getParent().requestDisallowInterceptTouchEvent(false);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        scrollView.getParent().requestDisallowInterceptTouchEvent(false);
                        viewPager.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });
    }

    /**
     * View pager adapter
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe_menu, menu);

        //set share icon from FontAwsome
        menu.findItem(R.id.share).setIcon(
                new IconicsDrawable(context)
                        .icon(FontAwesome.Icon.faw_share_alt)
                        .color(ContextCompat.getColor(context, R.color.md_white_1000))
                        .sizeDp(18));

        menu.findItem(R.id.timer).setIcon(
                new IconicsDrawable(context)
                        .icon(FontAwesome.Icon.faw_clock_o)
                        .color(ContextCompat.getColor(context, R.color.md_white_1000))
                        .sizeDp(18));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle share
        switch (item.getItemId()) {
            case R.id.share:
                if (recipe != null)
                    recipe.share(activity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
