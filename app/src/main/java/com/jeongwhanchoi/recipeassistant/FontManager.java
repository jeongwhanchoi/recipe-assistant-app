package com.jeongwhanchoi.recipeassistant;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by jeongwhanchoi on 11/04/2018.
 *
 * Enables use of fontAwesome. For more details on icon ids go to: http://fontawesome.io/
 */
public class FontManager {

    public static final String FONTAWESOME = "fontawesome-webfont.ttf";

    /**
     * Creates a typeface from assets
     *
     * @param context
     * @param font
     * @return
     */
    public static Typeface getTypeface(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }

    /**
     * Creates a fontAwesome typeset
     *
     * @param context
     * @return
     */
    public static Typeface getFontAwsome(Context context) {
        return Typeface.createFromAsset(context.getAssets(), FONTAWESOME);
    }

}
