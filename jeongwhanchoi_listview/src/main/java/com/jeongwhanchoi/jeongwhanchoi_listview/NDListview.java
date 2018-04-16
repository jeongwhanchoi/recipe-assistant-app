package com.jeongwhanchoi.jeongwhanchoi_listview;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.nhaarman.listviewanimations.ArrayAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.TouchViewDraggableManager;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.SimpleSwipeUndoAdapter;

/**
 * Created by melvin on 05/09/2016.
 */
public class NDListview extends DynamicListView {
    Context context;

    private static final int INITIAL_DELAY_MILLIS = 300;

    SimpleSwipeUndoAdapter simpleSwipeUndoAdapter;
    AlphaInAnimationAdapter animAdapter;
    public ArrayAdapter adapter;

    int loadingAnimationDelayMillis = 100;


    public NDListview(@NonNull final Context context) {
        super(context, null);
        this.context = context;
    }

    public NDListview(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs, Resources.getSystem().getIdentifier("listViewStyle", "attr", "android"));
        this.context = context;
    }

    public NDListview(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public NDListview setAdapter(ArrayAdapter adapter) {
        this.adapter = adapter;
        super.setAdapter(adapter);
        return this;
    }

    public NDListview enableSwipeUndo(OnDismissCallback onDismissCallback) {
        simpleSwipeUndoAdapter = new SimpleSwipeUndoAdapter(adapter, context, onDismissCallback);
        if (animAdapter != null)
            enableFadeIn(loadingAnimationDelayMillis);
        else
            setAdapter(simpleSwipeUndoAdapter);

        //Enable swipe to dismiss
        enableSimpleSwipeUndo();
        return this;
    }

    public NDListview enableFadeIn(int delayMillis) {
        loadingAnimationDelayMillis = delayMillis;

        if (simpleSwipeUndoAdapter != null)
            animAdapter = new AlphaInAnimationAdapter(simpleSwipeUndoAdapter);
        else
            animAdapter = new AlphaInAnimationAdapter(adapter);

        animAdapter.setAbsListView(this);
        assert animAdapter.getViewAnimator() != null;
        animAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);
        setAdapter(animAdapter);

        return this;
    }


    public NDListview enableDragAndDrop(int touchViewResId) {
        enableDragAndDrop();
        setDraggableManager(new TouchViewDraggableManager(touchViewResId));
        return this;
    }

    public  void setHeightBasedOnChildren() {
        try {
            ListAdapter listAdapter = this.getAdapter();
            if (listAdapter == null)
                return;

            int desiredWidth = MeasureSpec.makeMeasureSpec(this.getWidth(), MeasureSpec.UNSPECIFIED);
            int totalHeight = 0;
            View view = null;
            for (int i = 0; i < listAdapter.getCount(); i++) {
                view = listAdapter.getView(i, view, this);
                if (i == 0)
                    view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));

                view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
                totalHeight += view.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = this.getLayoutParams();
            params.height = totalHeight + (this.getDividerHeight() * (listAdapter.getCount() - 1));
            this.setLayoutParams(params);
        }catch (NullPointerException e){

        }
    }


}
