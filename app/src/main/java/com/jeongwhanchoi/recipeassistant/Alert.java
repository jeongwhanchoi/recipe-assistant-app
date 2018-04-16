package com.jeongwhanchoi.recipeassistant;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.LinearLayout;

public class Alert extends DialogFragment {
    Context mContext;
    String title, hint, hint2, text;
    String acceptText, cancelText;
    Boolean exit;
    Activity activity;
    PositiveButtonListener PositiveButtonListener;
    NegativeButtonListener NegativeButtonListener;
    PositiveButtonListener2 PositiveButtonListener2;
    NegativeButtonListener2 NegativeButtonListener2;
    int type;
    final int TEXT = 0, EDITTEXT = 1, EDITTEXT2 = 2;

    public void DisplayText(String title, String text, String acceptText, String cancelText, Activity activity) {
        this.title = title;
        this.text = text;
        this.activity = activity;
        type = TEXT;
        this.acceptText = acceptText;
        this.cancelText = cancelText;
    }

    public void DisplayText(String title, String text, String acceptText, Activity activity) {
        this.title = title;
        this.text = text;
        this.activity = activity;
        type = TEXT;
        this.acceptText = acceptText;
    }

    public void DisplayEditText(String title, String text, String hint, String acceptText, String cancelText, Activity activity) {
        this.title = title;
        this.activity = activity;
        this.text = text;
        this.hint = hint;
        type = EDITTEXT;
        this.acceptText = acceptText;
        this.cancelText = cancelText;
    }

    public void DisplayEditText2(String title, String text, String hint, String hint2, String acceptText, String cancelText, Activity activity) {
        this.title = title;
        this.activity = activity;
        this.text = text;
        this.hint = hint;
        this.hint2 = hint2;
        type = EDITTEXT2;
        this.acceptText = acceptText;
        this.cancelText = cancelText;
    }

    //positive button interface
    public interface PositiveButtonListener {
        public void onPositiveButton(String input);
    }

    public interface PositiveButtonListener2 {
        public void onPositiveButton(String input, String input2);
    }

    public synchronized void setPositiveButtonListener(PositiveButtonListener PositiveButtonListener) {
        this.PositiveButtonListener = PositiveButtonListener;
    }

    public synchronized void setPositiveButtonListener2(PositiveButtonListener2 PositiveButtonListener2) {
        this.PositiveButtonListener2 = PositiveButtonListener2;
    }

    //negative button interface
    public interface NegativeButtonListener {
        public void onNegativeButton(String input);
    }

    public interface NegativeButtonListener2 {
        public void onNegativeButton(String input, String input2);
    }

    public synchronized void setNegativeButtonListener(NegativeButtonListener NegativeButtonListener) {
        this.NegativeButtonListener = NegativeButtonListener;
    }

    public synchronized void setNegativeButtonListener2(NegativeButtonListener2 NegativeButtonListener2) {
        this.NegativeButtonListener2 = NegativeButtonListener2;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContext = getActivity();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(text);

        if (type == EDITTEXT) {
            final EditText edittext = new EditText(activity);
            edittext.setHint(hint);
            LinearLayout layout = new LinearLayout(activity);
            int dp16 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, activity.getResources().getDisplayMetrics());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            edittext.setLayoutParams(lp);
            layout.setPadding(dp16, dp16, dp16, dp16);
            layout.addView(edittext);
            alertDialogBuilder.setView(layout);

            alertDialogBuilder.setPositiveButton(acceptText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (PositiveButtonListener != null)
                        PositiveButtonListener.onPositiveButton(edittext.getText().toString());
                }
            });
            alertDialogBuilder.setNegativeButton(cancelText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (NegativeButtonListener != null)
                        NegativeButtonListener.onNegativeButton(edittext.getText().toString());
                }
            });
        }
        if (type == EDITTEXT2) {
            final EditText edittext = new EditText(activity);
            edittext.setHint(hint);
            LinearLayout layout = new LinearLayout(activity);
            layout.setOrientation(LinearLayout.VERTICAL);
            int dp16 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, activity.getResources().getDisplayMetrics());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            edittext.setLayoutParams(lp);
            layout.setPadding(dp16, dp16, dp16, dp16);
            layout.addView(edittext);
            //alertDialogBuilder.setView(layout);

            final EditText edittext2 = new EditText(activity);
            edittext2.setHint(hint2);
           // LinearLayout layout2 = new LinearLayout(activity);
           // dp16 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, activity.getResources().getDisplayMetrics());
            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            edittext2.setLayoutParams(lp2);
            //layout2.setPadding(dp16, dp16, dp16, dp16);
            layout.addView(edittext2);
            alertDialogBuilder.setView(layout);

            alertDialogBuilder.setPositiveButton(acceptText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (PositiveButtonListener2 != null)
                        PositiveButtonListener2.onPositiveButton(edittext.getText().toString(), edittext2.getText().toString());
                }
            });
            alertDialogBuilder.setNegativeButton(cancelText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (NegativeButtonListener2 != null)
                        NegativeButtonListener2.onNegativeButton(edittext.getText().toString(), edittext2.getText().toString());
                }
            });
        }
        if (type == TEXT) {

            alertDialogBuilder.setPositiveButton(acceptText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (PositiveButtonListener != null)
                        PositiveButtonListener.onPositiveButton("");

                }
            });
            if (cancelText != null) {
                alertDialogBuilder.setNegativeButton(cancelText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (NegativeButtonListener != null)
                            NegativeButtonListener.onNegativeButton("");

                    }
                });
            }
        }

        return alertDialogBuilder.create();
    }


}