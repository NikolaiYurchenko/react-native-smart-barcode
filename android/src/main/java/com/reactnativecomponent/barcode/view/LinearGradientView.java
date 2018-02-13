package com.reactnativecomponent.barcode.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;


public class LinearGradientView extends View {

    public int frameColor = Color.GREEN;


    public int frameBaseColor;


    public int size;

    public int width;

    Activity activity;

    public LinearGradientView(Context context, Activity activity) {
        super(context);
        this.activity = activity;
    }

    public void setFrameColor(int frameColor) {

        this.frameColor = frameColor;

        this.frameBaseColor = reSetColor(frameColor);

        int[] mColors = new int[]{Color.TRANSPARENT, frameBaseColor, frameColor, frameColor, frameColor, frameColor, frameColor, frameBaseColor, Color.TRANSPARENT};

        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, mColors);
        drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        setBackground(drawable);
    }

    @Override
    protected void onAttachedToWindow() {
        ViewGroup.LayoutParams params = getLayoutParams();
        if (size > 1) {
            params.height = size;
        }
        if (width > 1) {
            params.width = width;
        }
        setLayoutParams(params);

        super.onAttachedToWindow();

    }


    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {

        super.onWindowFocusChanged(hasWindowFocus);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final ViewGroup.LayoutParams params = getLayoutParams();
        int height = getHeight();
        if (height < 3) {
            params.height = 3;
        } else if (height > 10) {
            params.height = 10;
        }

        activity.runOnUiThread(new Runnable() {
            public void run() {
                LinearGradientView.this.setLayoutParams(params);
            }
        });

        super.onLayout(changed, left, top, right, bottom);
    }

    public int reSetColor(int startInt) {

        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endA = startA / 2;


        return ((startA + (endA - startA)) << 24)
                | (startR << 16)
                | (startG << 8)
                | (startB);


    }
}
