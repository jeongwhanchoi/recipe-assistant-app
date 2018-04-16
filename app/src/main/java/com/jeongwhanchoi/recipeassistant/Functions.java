package com.jeongwhanchoi.recipeassistant;

import android.support.v4.app.FragmentActivity;

/**
 * Created by jeongwhanchoi on 11/04/2018.
 * <p>
 * Contains general functions
 */

public class Functions {

    /**
     * Display an alert when no internet is detected
     *
     * @param activity
     */
    public static void noInternetAlert(FragmentActivity activity) {
        //error connecting
        Alert alert = new Alert();
        alert.DisplayText(activity.getString(R.string.no_internet_error_title), activity.getString(R.string.no_internet_error), activity.getString(R.string.Alert_accept), activity.getString(R.string.Alert_cancel), activity);
        alert.show(activity.getSupportFragmentManager(), activity.getString(R.string.no_internet_error));
    }

    /**
     * This is a template to create a responsive HTML document for the webview. The content is then downloaded/stored locally.
     *
     * @param content - Content to show in <body></body>
     * @return - Html Document with content
     */
    public static String HTMLTemplate(String content) {
        return "<!doctype html> \n" +
                "<html> \n" +
                "<head> \n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">   \n" +
                "</head>  \n" +
                "\n" +
                "<body> \n" +
                "<div class='content'>" + content + "</div>" +
                "</body> \n" +
                "\n" +
                "</html>\n" +
                "\n" +
                "<style>\n" +
                "\n" +
                ".content {\n" +
                "    padding:  1px 1px 1px 1px;\n" +
                "}" +
                ".note-video-clip{\n" +
                "    position: absolute;\n" +
                "    top: 0;\n" +
                "    left: 0;\n" +
                "    width: 100%;\n" +
                "    height: 100%;" +
                "}\n" +
                ".video_container {\n" +
                "    position: relative;\n" +
                "    width: 100%;\n" +
                "    height: 0;\n" +
                "    padding-bottom: 56.25%;\n" +
                "}" +
                "\n" +
                "}\n" +
                "img{\n" +
                "  display: block;\n" +
                "  width: 100%;\n" +
                "  height: auto   !important;\n" +
                "}\n" +
                "\n" +
                "</style>";
    }
}
