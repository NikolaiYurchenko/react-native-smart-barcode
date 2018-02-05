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

//    private int slideTop;
//

//    private static int SPEEN_DISTANCE = 3;
//
//    private int slideBottom;
//
//    public int CORNER_WIDTH = 3;*/

    
    public int frameColor=Color.GREEN;

    
    public int frameBaseColor;

    
    public int size;
    
    public int width;

    Activity activity;


//    private static final int MIDDLE_LINE_WIDTH = 3;


//    public int top,left,right;


//    private Paint paintLine;


    public LinearGradientView(Context context,Activity activity) {
        super(context);
        this.activity=activity;

//        paintLine=new Paint();
    }

    public void setFrameColor(int frameColor) {

        this.frameColor = frameColor;

        this.frameBaseColor = reSetColor(frameColor);
        
        int[] mColors = new int[]{Color.TRANSPARENT, frameBaseColor, frameColor, frameColor, frameColor, frameColor, frameColor, frameBaseColor, Color.TRANSPARENT};

        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, mColors);
        drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
//        drawable.setCornerRadius(15);
//        drawable.setStroke(10,-1);
        setBackground(drawable);
    }

    @Override
    protected void onAttachedToWindow() {
        ViewGroup.LayoutParams params= getLayoutParams();
        if(size>1) {
            params.height = size;
        }
        if(width>1){
            params.width=width;
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
       final ViewGroup.LayoutParams params= getLayoutParams();
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




/*    @Override
    protected void onDraw(Canvas canvas) {
        paintLine.setColor(frameColor);


        slideTop += SPEEN_DISTANCE;
        if (slideTop >= slideBottom) {
            slideTop = top + CORNER_WIDTH;
        }
        
        paintLine.setColor(frameColor);

//                0x8800FF00
        Shader mShader = new LinearGradient(left + CORNER_WIDTH, slideTop, right
                - CORNER_WIDTH, slideTop + MIDDLE_LINE_WIDTH,new int[] {Color.TRANSPARENT,frameBaseColor,frameColor,frameColor,frameColor,frameColor,frameColor,frameBaseColor,Color.TRANSPARENT},null, Shader.TileMode.CLAMP);
        paintLine.setShader(mShader);
        canvas.drawRect(left + CORNER_WIDTH, slideTop, right
                - CORNER_WIDTH, slideTop + MIDDLE_LINE_WIDTH, paintLine);

    }*/

    
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
