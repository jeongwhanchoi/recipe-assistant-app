package com.jeongwhanchoi.recipeassistant;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by jeongwhanchoi on 11/04/2018.
 */
public class InfoFragment extends Fragment {
    Context context;

    WebView infoWebview;
    SwipeRefreshLayout swipeLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_info, container, false);
        infoWebview = (WebView) rootView.findViewById(R.id.webview_info);
        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeToRefresh);

        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        getActivity().setTitle(getString(R.string.info_page_title));

        //load info
        refresh();

        // Swipe to Refresh
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // refreshes the WebView
                refresh();
            }
        });

        //stop refresh
        infoWebview.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                swipeLayout.setRefreshing(false);
            }
        });
        //enable javascript
        infoWebview.getSettings().setJavaScriptEnabled(true);
       // infoWebview.clearCache(false);
        //infoWebview.getSettings().setMinimumFontSize((int) getResources().getDimension(R.dimen.webviewMinTextSize));
        //infoWebview.getSettings().setBuiltInZoomControls(true);
        //infoWebview.getSettings().setDisplayZoomControls(false);


    }

    public void refresh() {
        //load info
        Preference.load(context, "info", new Preference.onPreferenceDownloadedListener() {
            @Override
            public void onPreferenceDownloaded(String info) {
                infoWebview.loadData(Functions.HTMLTemplate(info), "text/html; charset=utf-8", "utf-8");
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        infoWebview.destroy();
    }

}
