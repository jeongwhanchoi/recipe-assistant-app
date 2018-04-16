package com.jeongwhanchoi.recipeassistant;


public class Configurations {

    //GENERAL---------------------------------------------------------------------------------------

    //Server URL
    public static String SERVER_URL = "http://recipeassistant.info/";


    //DEEP LINK SHARE-------------------------------------------------------------------------------
    //in android manifest don't forget:
    //<data android:host="recipeassistant.info" android:scheme="http"/>
    //<data android:host="recipeassistant.info" android:scheme="https"/>
    final static String DEEP_LINK_TO_SHARE = "http://recipeassistant.info";


    //LIST TYPE------------------------------------------------------------------------------------
    public final static int LIST_FULLWIDTH = 0, LIST_2COLUMNS = 1;
    public final static int LIST_MENU_TYPE = LIST_2COLUMNS;


    //CATEGORIES------------------------------------------------------------------------------------
    public final static int CATEGORY_TEXT_ONLY = 0, CATEGORY_TEXT_AND_IMAGE = 1;
    public final static int CATEGORY_MENU_TYPE = CATEGORY_TEXT_AND_IMAGE;

    public final static boolean DISPLAY_CATEGORIES_IN_NAVIGATION_DRAWER = true;

    //Ingredients-----------------------------------------------------------------------------------
    //fraction (true) or decimal (false)
    public final static boolean INGREDIENTS_FRACTION = true;

    //Nutrition-------------------------------------------------------------------------------------
    //daily values to calculate percent
    public final static float FAT_DAILY_VALUE =65;//grams
    public final static float SATURATED_FAT_DAILY_VALUE =20;//grams
    public final static float CHOLESTEROL_DAILY_VALUE =300;//milligrams
    public final static float SODIUM_DAILY_VALUE =2400;//milligrams
    public final static float TOTAL_CARBOHYDRATE_DAILY_VALUE =300;//grams
    public final static float DIETARY_FIBER_DAILY_VALUE =25;//grams


}
