package com.jeongwhanchoi.jeongwhanchoi_listview;

import android.widget.CompoundButton;

public class ThreeLineItem {

    public String text1;
    public String text2;
    public int imageDrawableRes = -1;
    public CompoundButton.OnCheckedChangeListener onCheckedChangeListener;
    public int id;

    public ThreeLineItem(String text1, String text2) {
        this.text1 = text1;
        this.text2 = text2;
    }

    public ThreeLineItem(String text1, String text2, int imageDrawableRes) {
        this.text1 = text1;
        this.text2 = text2;
        this.imageDrawableRes = imageDrawableRes;

    }

    public ThreeLineItem(String text1, String text2, int imageDrawableRes, CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        this.text1 = text1;
        this.text2 = text2;
        this.imageDrawableRes = imageDrawableRes;
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    public ThreeLineItem(String text1, String text2, CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        this.text1 = text1;
        this.text2 = text2;
        this.onCheckedChangeListener = onCheckedChangeListener;
    }


    public ThreeLineItem setAvatar(int imageDrawableRes) {
        this.imageDrawableRes = imageDrawableRes;
        return this;
    }

    public ThreeLineItem setCheckbox(CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
        return this;
    }

}