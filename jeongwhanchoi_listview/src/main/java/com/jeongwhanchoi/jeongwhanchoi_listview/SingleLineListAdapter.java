package com.jeongwhanchoi.jeongwhanchoi_listview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.nhaarman.listviewanimations.ArrayAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.GripView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoAdapter;

import de.hdodenhof.circleimageview.CircleImageView;

public class SingleLineListAdapter extends ArrayAdapter<SingleLineItem> implements UndoAdapter {

    private final Context mContext;

    private boolean showGrip = false;

    public SingleLineListAdapter(final Context context) {
        mContext = context;
    }

    @Override
    public boolean add(@NonNull final SingleLineItem singleLineItem) {
        singleLineItem.id = singleLineItem.hashCode();
        return super.add(singleLineItem);
    }

    public SingleLineListAdapter showGrip(boolean showGrip) {
        this.showGrip = showGrip;
        return this;
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
        SingleLineItem singleLineItem = getItem(position);

        //get view and store in viewholder
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_row_singleline_text_avatar_checkbox, parent, false);
            holder.textView = ((TextView) convertView.findViewById(R.id.list_row_textview));
            holder.circleImageView = ((CircleImageView) convertView.findViewById(R.id.list_row_avatar));
            holder.checkBox = ((CheckBox) convertView.findViewById(R.id.list_row_checkbox));
            holder.gripView = ((GripView) convertView.findViewById(R.id.list_row_draganddrop_touchview));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //avatar
        if (singleLineItem.imageDrawableRes >= 0) {
            holder.circleImageView.setVisibility(View.VISIBLE);
            holder.circleImageView.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(), singleLineItem.imageDrawableRes, null));
        } else {
            holder.circleImageView.setVisibility(View.GONE);
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
        //set checkbox checked
        holder.checkBox.setChecked(singleLineItem.checkBoxIsChecked);

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
            view = LayoutInflater.from(mContext).inflate(R.layout.list_row_singleline_undo, parent, false);
        }
        return view;
    }

    @NonNull
    @Override
    public View getUndoClickView(@NonNull final View view) {
        return view.findViewById(R.id.undo_row_undobutton);
    }

    public static class ViewHolder {
        public TextView textView;
        public CheckBox checkBox;
        public CircleImageView circleImageView;
        public GripView gripView;
    }

}