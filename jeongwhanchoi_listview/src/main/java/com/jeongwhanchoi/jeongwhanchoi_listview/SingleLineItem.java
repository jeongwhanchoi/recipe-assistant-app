package com.jeongwhanchoi.jeongwhanchoi_listview;

import android.widget.CompoundButton;

public class SingleLineItem {

    public String text1;
    public int imageDrawableRes = -1;
    public CompoundButton.OnCheckedChangeListener onCheckedChangeListener;
    public boolean checkBoxIsChecked = false;
    public int id;

    public SingleLineItem(String text) {
        this.text1 = text;

    }

    public SingleLineItem(String text, int imageDrawableRes) {
        this.text1 = text;
        this.imageDrawableRes = imageDrawableRes;

    }

    public SingleLineItem(String text, int imageDrawableRes, CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        this.text1 = text;
        this.imageDrawableRes = imageDrawableRes;
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    public SingleLineItem(String text, CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        this.text1 = text;
        this.onCheckedChangeListener = onCheckedChangeListener;
    }


    public SingleLineItem setAvatar(int imageDrawableRes) {
        this.imageDrawableRes = imageDrawableRes;
        return this;
    }

    public SingleLineItem setCheckbox(CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
        return this;
    }

    public SingleLineItem setCheckbox(CompoundButton.OnCheckedChangeListener onCheckedChangeListener, boolean checkBoxIsChecked) {
        this.onCheckedChangeListener = onCheckedChangeListener;
        this.checkBoxIsChecked = checkBoxIsChecked;
        return this;
    }


}