package com.jeongwhanchoi.recipeassistant.view;

/**
 * Created by melvin on 01/10/2016.
 * <p>
 * Extends RecyclerView to add support for empty lists.
 * When the recyclerView is empty this will show a view which can be used to indicate that the list is empty.
 * <p>
 * Example usage in xml:
 * <p>
 * <RelativeLayout
 * android:id="@+id/empty"
 * android:layout_width="match_parent"
 * android:layout_height="match_parent">
 * <TextView
 * android:layout_width="wrap_content"
 * android:layout_height="wrap_content"
 * android:layout_centerInParent="true"
 * android:text="@string/empty_recipes"
 * android:textSize="16sp" />
 * </RelativeLayout>
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class EmptyRecyclerView extends RecyclerView {

    private View emptyView;

    final private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
        }
    };

    public EmptyRecyclerView(Context context) {
        super(context);
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs,
                             int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Checks if Recycler view is empty and sets visibility of emptyView
     */
    void checkIfEmpty() {
        if (emptyView != null && getAdapter() != null) {
            final boolean emptyViewVisible = getAdapter().getItemCount() == 0;
            emptyView.setVisibility(emptyViewVisible ? VISIBLE : GONE);
            setVisibility(emptyViewVisible ? GONE : VISIBLE);
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }

        checkIfEmpty();
    }

    /**
     * Sets an view to be shown when list is empty
     * @param emptyView
     */
    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        checkIfEmpty();
    }
}