package com.jeongwhanchoi.recipeassistant;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

//import com.google.firebase.messaging.FirebaseMessaging;
import com.jeongwhanchoi.recipeassistant.fragment.AddRecipeFragment;
import com.jeongwhanchoi.recipeassistant.fragment.CategoriesFragment;
import com.jeongwhanchoi.recipeassistant.fragment.CategoryRecipesFragment;
import com.jeongwhanchoi.recipeassistant.fragment.CategoryTextAndImageFragment;
import com.jeongwhanchoi.recipeassistant.fragment.FavoriteFragment;
import com.jeongwhanchoi.recipeassistant.fragment.HomeFragment;
import com.jeongwhanchoi.recipeassistant.fragment.InfoFragment;
import com.jeongwhanchoi.recipeassistant.fragment.ShoppingListFragment;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the first Activity shown.
 * <p>
 * Handles the generation of the side navigation drawer, shows the main fragment and shows ads if enabled
 */

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    Drawer drawer;
    Context context;

    //navigation drawer item identification numbers
    final int NAV_HOME = 0, NAV_FAV = 1, NAV_SHOP = 2, NAV_MORE = 3, NAV_INFO = 4, NAVSETTINGS = 5, NAV_CATEGORIES = 100, NAV_ADD_RECIPE = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        this.context = this;

        //portrait only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //set toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Generate the side navigation drawer
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withRootView(R.id.drawer_container)
                .withDisplayBelowStatusBar(true)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(getDrawerItems(null))
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        //On click: open the required activity or fragment
                        Intent intent;
                        switch ((int) drawerItem.getIdentifier()) {
                            case NAV_HOME:
                                changeFragment(new HomeFragment());
                                break;
                            case NAV_MORE:
                                //this can be set through configurations.jave
                                if (Configurations.CATEGORY_MENU_TYPE == Configurations.CATEGORY_TEXT_ONLY)
                                    changeFragment(new CategoriesFragment());
                                else if (Configurations.CATEGORY_MENU_TYPE == Configurations.CATEGORY_TEXT_AND_IMAGE)
                                    changeFragment(new CategoryTextAndImageFragment());
                                else
                                    changeFragment(new CategoriesFragment());
                                break;
                            case NAV_FAV:
                                changeFragment(new FavoriteFragment());
                                break;
                            case NAV_SHOP:
                                changeFragment(new ShoppingListFragment());
                                break;
                            case NAV_INFO:
                                changeFragment(new InfoFragment());
                                break;
                            case NAVSETTINGS:
                                intent = new Intent(context, SettingsActivity.class);
                                startActivity(intent);
                                break;
                            case NAV_ADD_RECIPE:
                                changeFragment(new AddRecipeFragment());
                                break;
                            default:
                                //opens the categories displayed in drawer
                                if (drawerItem.getIdentifier() > NAV_CATEGORIES) {
                                    Bundle b = new Bundle();
                                    b.putInt("Category_id", (int) (drawerItem.getIdentifier() - NAV_CATEGORIES));
                                    Fragment f = new CategoryRecipesFragment();
                                    f.setArguments(b);
                                    changeFragment(f);
                                }
                        }
                        drawer.closeDrawer();
                        return true;
                    }
                })
                .build();


        Category.loadCategories(context, "", new Category.onCategoriesDownloadedListener() {
            @Override
            public void onCategoriesDownloaded(List<Category> categories) {
                refreshNavDrawer(categories);
            }
        });

    }


    /**
     * Removes all items from drawer and creates them again to refresh.
     *
     * @param categories - List of Categories
     */
    public void refreshNavDrawer(List<Category> categories) {
        drawer.removeAllItems();
        drawer.addItems(getDrawerItems(categories));
    }

    /**
     * Generates a list of Drawer items
     *
     * @param categories
     * @return
     */
    public IDrawerItem[] getDrawerItems(List<Category> categories) {
        List<IDrawerItem> drawerItems = new ArrayList<>();

        //Add Home, Favorites and Shopping list
        drawerItems.add(new PrimaryDrawerItem().withIdentifier(NAV_HOME).withName(R.string.nav_home).withIcon(FontAwesome.Icon.faw_home));
        drawerItems.add(new SecondaryDrawerItem().withIdentifier(NAV_FAV).withName(R.string.nav_favorites).withIcon(FontAwesome.Icon.faw_star));
        drawerItems.add(new SecondaryDrawerItem().withIdentifier(NAV_SHOP).withName(R.string.nav_shopping_list).withIcon(FontAwesome.Icon.faw_shopping_cart));

        if (Configurations.DISPLAY_CATEGORIES_IN_NAVIGATION_DRAWER) {
            //Add categories and more...
            drawerItems.add(new SectionDrawerItem().withName(R.string.nav_categories));
            if (categories != null) {
                for (int i = 0; i < categories.size(); i++) {
                    if (i < getResources().getInteger(R.integer.categories_to_show_in_drawer))
                        drawerItems.add(new SecondaryDrawerItem().withIdentifier(NAV_CATEGORIES + categories.get(i).id).withName(categories.get(i).name).withIcon(FontAwesome.Icon.faw_cutlery));
                }
            }
            drawerItems.add(new SecondaryDrawerItem().withIdentifier(NAV_MORE).withName(R.string.nav_categories_more).withIcon(FontAwesome.Icon.faw_ellipsis_h));
            drawerItems.add(new DividerDrawerItem());
        } else {
            //add just a categories button
            drawerItems.add(new SecondaryDrawerItem().withIdentifier(NAV_MORE).withName(R.string.nav_categories).withIcon(FontAwesome.Icon.faw_bars));
        }

        //add final 4 items
        drawerItems.add(new SecondaryDrawerItem().withIdentifier(NAV_ADD_RECIPE).withName(R.string.nav_add_recipe).withIcon(FontAwesome.Icon.faw_plus));
        drawerItems.add(new SecondaryDrawerItem().withIdentifier(NAV_INFO).withName(R.string.nav_info).withIcon(FontAwesome.Icon.faw_question));
        drawerItems.add(new SecondaryDrawerItem().withIdentifier(NAVSETTINGS).withName(R.string.nav_settings).withIcon(FontAwesome.Icon.faw_cog));

        return drawerItems.toArray(new IDrawerItem[0]);
    }

    /**
     * Change main fragment
     *
     * @param fragment
     */
    public void changeFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFragment, fragment);
        transaction.commit();
    }


    /**
     * On back pressed, always go to home fragment before closing
     */
    @Override
    public void onBackPressed() {
        //if stack has items left
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            //get current fragment
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.mainFragment);

            //only close if in HomeFragment else go to HomeFragment
            if (fragment instanceof HomeFragment) {
                finish();
            } else {
                changeFragment(new HomeFragment());
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
