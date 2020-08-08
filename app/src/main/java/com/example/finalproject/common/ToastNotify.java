package com.example.finalproject.common;

import android.content.Context;
import android.widget.Toast;

public class ToastNotify {
    public static void showLongMsg(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static void showShortMsg(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
