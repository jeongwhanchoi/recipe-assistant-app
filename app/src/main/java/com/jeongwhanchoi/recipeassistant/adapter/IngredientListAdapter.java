package com.jeongwhanchoi.recipeassistant.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jeongwhanchoi.recipeassistant.FontManager;
import com.jeongwhanchoi.recipeassistant.IngredientItem;
import com.jeongwhanchoi.recipeassistant.R;
import com.nhaarman.listviewanimations.ArrayAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.GripView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoAdapter;

public class IngredientListAdapter extends ArrayAdapter<IngredientItem> implements UndoAdapter {

    private final Context mContext;

    private boolean showGrip = false;

    public IngredientListAdapter(final Context context) {
        mContext = context;
    }

    @Override
    public boolean add(@NonNull final IngredientItem ingredientItem) {
        ingredientItem.id = ingredientItem.hashCode();
        return super.add(ingredientItem);
    }

    public void showGrip(boolean showGrip) {
        this.showGrip = showGrip;
    }


    @Override
    public long getItemId(final int position) {
        return getItem(position).id;
    }


    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final IngredientItem singleLineItem = getItem(position);

        //get view and store in viewholder
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.ingredients_listview_item, parent, false);
            holder.textView = ((TextView) convertView.findViewById(R.id.list_row_textview));
            holder.cart = ((TextView) convertView.findViewById(R.id.shoppingcart));
            holder.checkBox = ((CheckBox) convertView.findViewById(R.id.list_row_checkbox));
            holder.gripView = ((GripView) convertView.findViewById(R.id.list_row_draganddrop_touchview));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.cart.setTypeface(FontManager.getFontAwsome(convertView.getContext()));
        holder.cart.setText(convertView.getContext().getString(R.string.shoppingcart_icon));
        if (singleLineItem.onCartClickListener != null) {
            //holder.cart.setOnClickListener(singleLineItem.onCartClickListener);
            holder.cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.cart.getText() == mContext.getString(R.string.shoppingcart_icon)) {
                        singleLineItem.onCartClickListener.onClick(view);
                        holder.cart.setText(mContext.getString(R.string.shoppingcart_check_icon));
                    }

                }
            });
        }

        //textbox
        if (singleLineItem.text1 != null) {
            holder.textView.setText(singleLineItem.text1);
        }
        //hide/show checkbox
        if (singleLineItem.onCheckedChangeListener == null) {
            holder.checkBox.setVisibility(View.GONE);
            holder.checkBox.setOnCheckedChangeListener(null);
        } else {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setOnCheckedChangeListener(singleLineItem.onCheckedChangeListener);
        }

        //hide/show grip
        if (!showGrip) {
            holder.gripView.setVisibility(View.GONE);
        } else {
            holder.gripView.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    @NonNull
    @Override
    public View getUndoView(final int position, final View convertView, @NonNull final ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(com.jeongwhanchoi.jeongwhanchoi_listview.R.layout.list_row_singleline_undo, parent, false);
        }
        return view;
    }

    @NonNull
    @Override
    public View getUndoClickView(@NonNull final View view) {
        return view.findViewById(com.jeongwhanchoi.jeongwhanchoi_listview.R.id.undo_row_undobutton);
    }

    public static class ViewHolder {
        public TextView textView;
        public TextView cart;
        public CheckBox checkBox;
        public GripView gripView;
    }

    public static class HeaderViewHolder {
        public TextView textView;
    }


}