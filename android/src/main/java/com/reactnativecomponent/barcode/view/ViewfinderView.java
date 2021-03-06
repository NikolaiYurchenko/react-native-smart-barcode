/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.reactnativecomponent.barcode.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.reactnativecomponent.barcode.R;
import com.reactnativecomponent.barcode.camera.CameraManager;


import java.util.Collection;
import java.util.HashSet;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder rectangle and partial
 * transparency outside it, as well as the laser scanner animation and result points.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ViewfinderView extends View
{
    
    private static final int[] SCANNER_ALPHA = { 0, 64, 128, 192, 255, 192,
            128, 64 };
    
    private static final long ANIMATION_DELAY = 10L;
    private static final int OPAQUE = 0xFF;

   
    private int ScreenRate;

    
        public int CORNER_WIDTH = 3;

        public int CORNER_LENGTH = 5;
                /**Corner radius */
        public int RXY = 2;
    
    private int MIDDLE_LINE_WIDTH = 3;
    
    private static final int MIDDLE_LINE_PADDING = 5;

    private static int SPEEN_DISTANCE = 3;

    private static float density;

    private static final int TEXT_SIZE = 16;

    public String ShowText;

    private static final int TEXT_PADDING_TOP = 30;

    private final Paint paint;
    private final Paint paintLine;

    private Bitmap resultBitmap;

    private final int maskColor;

    private final int resultColor;

    public int frameColor;

    public int frameBaseColor;

    private final int laserColor;

    private final int resultPointColor;
    private int scannerAlpha;

    private Collection<ResultPoint> possibleResultPoints;

    private Collection<ResultPoint> lastPossibleResultPoints;

    public boolean drawLine = false;

    private int slideTop;

    private int slideBottom;
    private boolean isFirst;

    public int scanTime;


    // This constructor is used when the class is built from an XML resource.
    public ViewfinderView(Context context,int time,int color)
    {
        super(context);
        density = context.getResources().getDisplayMetrics().density;
        
        ScreenRate = (int) (25 * density);

        // Initialize these once for performance rather than calling them every time in onDraw().
        paint = new Paint();
        paintLine=new Paint();
        Resources resources = getResources();
        maskColor = resources.getColor(R.color.viewfinder_mask);
        resultColor = resources.getColor(R.color.backgroud);
        frameColor = color;//resources.getColor(R.color.viewfinder_frame);
        frameBaseColor = reSetColor(frameColor);
//        Log.i("Test","reSetColor"+frameBaseColor);
        laserColor = resources.getColor(R.color.viewfinder_laser);
        resultPointColor = resources.getColor(R.color.possible_result_points);
        scannerAlpha = 0;
        possibleResultPoints = new HashSet<ResultPoint>(5);
        drawLine=true;
        scanTime=time;
    }

    public void setCORNER_WIDTH(int CORNER_WIDTH) {
            Log.e("VVsetCORNER_WIDTH", String.valueOf(CORNER_WIDTH));
        this.CORNER_WIDTH = CORNER_WIDTH;
    }

    public void setCORNER_LENGTH(int CORNER_LENGTH) {
            Log.e("VVsetCORNER_LENGTH", String.valueOf(CORNER_LENGTH));
        this.CORNER_LENGTH = CORNER_LENGTH;
    }

    public void setRXY(int RXY) {
            Log.e("VVsetRXY", String.valueOf(RXY));
        this.RXY = RXY;
    }

    public void setMIDDLE_LINE_WIDTH(int MIDDLE_LINE_WIDTH) {
        this.MIDDLE_LINE_WIDTH = MIDDLE_LINE_WIDTH;
//        Log.i("Test","MIDDLE_LINE_WIDTH:"+MIDDLE_LINE_WIDTH);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        Rect frame = CameraManager.get().getFramingRect();
        if (frame == null)
        {
            return;
        }

        if (!isFirst)
        {
            isFirst = true;
            slideTop = frame.top + CORNER_WIDTH;
            slideBottom = frame.bottom - CORNER_WIDTH;

            SPEEN_DISTANCE= (slideBottom-slideTop)/((scanTime/16)+2);


        }

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        // Draw the exterior (i.e. outside the framing rect) darkened
        
        paint.setColor(resultBitmap != null ? resultColor : maskColor);
        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1,
                paint);
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);

        if (resultBitmap != null)
        {
            // Draw the opaque result bitmap over the scanning rectangle
            paint.setAlpha(OPAQUE);
            canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
        }
        else
        {

            // Draw a two pixel solid black border inside the framing rect
            
            paint.setColor(frameColor);
            //      canvas.drawRect(frame.left, frame.top, frame.right + 1, frame.top + 2, paint);
            //      canvas.drawRect(frame.left, frame.top + 2, frame.left + 2, frame.bottom - 1, paint);
            //      canvas.drawRect(frame.right - 1, frame.top, frame.right + 1, frame.bottom - 1, paint);
            //      canvas.drawRect(frame.left, frame.bottom - 1, frame.right + 1, frame.bottom + 1, paint);
            //public void drawRect (float left, float top, float right, float bottom, Paint paint) 
            

            paint.setColor(Color.TRANSPARENT);
            
            
            canvas.drawRect(frame.right  , frame.top
                            + ScreenRate, frame.right+ CORNER_WIDTH,
                    frame.bottom - ScreenRate, paint);
            
            canvas.drawRect(frame.left+ ScreenRate  , frame.bottom ,
                    frame.right- ScreenRate,frame.bottom + CORNER_WIDTH, paint);

        paint.setColor(frameColor);

        canvas.drawRoundRect(frame.left - CORNER_WIDTH, frame.top - CORNER_WIDTH, frame.left + CORNER_LENGTH, frame.top, RXY, RXY, paint);//左白线
        canvas.drawRoundRect(frame.left - CORNER_WIDTH, frame.top - CORNER_WIDTH, frame.left, frame.top + CORNER_LENGTH, RXY, RXY, paint);//上白线


        canvas.drawRoundRect(frame.left - CORNER_WIDTH, frame.bottom - CORNER_LENGTH, frame.left, frame.bottom + CORNER_WIDTH, RXY, RXY, paint);//左下角竖线
        //     canvas.drawRect(frame.left - CORNER_WIDTH/2 , frame.bottom
        //     - ScreenRate, frame.left + CORNER_WIDTH/2 , frame.bottom
        //     + CORNER_WIDTH/2 , paint);//左下角竖线
        canvas.drawRoundRect(frame.left - CORNER_WIDTH, frame.bottom, frame.left + CORNER_LENGTH, frame.bottom + CORNER_WIDTH, RXY, RXY, paint);//左下角横线
        //     canvas.drawRect(frame.left - CORNER_WIDTH/2 , frame.bottom
        //     - CORNER_WIDTH/2 , frame.left + ScreenRate, frame.bottom
        //     + CORNER_WIDTH/2 , paint);//左下角横线

        canvas.drawRoundRect(frame.right - CORNER_LENGTH, frame.top - CORNER_WIDTH, frame.right + CORNER_WIDTH, frame.top, RXY, RXY, paint);//右上横线
        canvas.drawRoundRect(frame.right, frame.top - CORNER_WIDTH, frame.right + CORNER_WIDTH, frame.top + CORNER_LENGTH, RXY, RXY, paint);//右上竖线

        canvas.drawRoundRect(frame.right - CORNER_LENGTH , frame.bottom, frame.right + CORNER_WIDTH, frame.bottom + CORNER_WIDTH, RXY, RXY, paint);//右下竖线
        canvas.drawRoundRect(frame.right , frame.bottom - CORNER_LENGTH, frame.right + CORNER_WIDTH, frame.bottom + CORNER_WIDTH, RXY, RXY, paint);//右下横线
             
            //      Rect bigRect = new Rect();
            //		bigRect.left = frame.left;
            //		bigRect.right = frame.right;
            //		bigRect.top = frame.top;
            //		bigRect.bottom = frame.bottom;
            //		Drawable drawable =  getResources().getDrawable(R.drawable.qr_mask);
            //		BitmapDrawable b= (BitmapDrawable) drawable;
            //		canvas.drawBitmap(b.getBitmap(), null, bigRect, paint);

            // Draw a red "laser scanner" line through the middle to show decoding is active
            //      paint.setColor(laserColor);
            //      paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
            //      scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
            //      int middle = frame.height() / 2 + frame.top;
            //      canvas.drawRect(frame.left + 2, middle - 1, frame.right - 1, middle + 2, paint);

            
            if(drawLine) {
                slideTop += SPEEN_DISTANCE;
                if (slideTop >= slideBottom) {
                    slideTop = frame.top + CORNER_WIDTH;
                }
                
                paintLine.setColor(frameColor);

//                0x8800FF00
                Shader mShader = new LinearGradient(frame.left + CORNER_WIDTH, slideTop, frame.right
                        - CORNER_WIDTH, slideTop + MIDDLE_LINE_WIDTH,new int[] {Color.TRANSPARENT,frameBaseColor,frameColor,frameColor,frameColor,frameColor,frameColor,frameBaseColor,Color.TRANSPARENT},null, Shader.TileMode.CLAMP);

                paintLine.setShader(mShader);
                canvas.drawRect(frame.left + CORNER_WIDTH, slideTop, frame.right
                        - CORNER_WIDTH, slideTop + MIDDLE_LINE_WIDTH, paintLine);



            }
            
            //      Rect lineRect = new Rect();
            //		lineRect.left = frame.left;
            //		lineRect.right = frame.right;
            //		lineRect.top = slideTop;
            //		lineRect.bottom = slideTop + MIDDLE_LINE_PADDING;
            //		canvas.drawBitmap(((BitmapDrawable)(getResources().getDrawable(R.drawable.qrcode_scan_line))).getBitmap(), null, lineRect, paint);

           
            paint.setColor(Color.WHITE);
            paint.setTextSize(TEXT_SIZE * density);
            paint.setAlpha(0x40);
            paint.setTypeface(Typeface.create("System", Typeface.BOLD));
            paint.setTextAlign(Paint.Align.CENTER);//文字居中,X,Y 对应文字坐标中心
            canvas.drawText(
                    ShowText,
                    width/2, frame.bottom + TEXT_PADDING_TOP * density,
                    paint);

            Collection<ResultPoint> currentPossible = possibleResultPoints;
            Collection<ResultPoint> currentLast = lastPossibleResultPoints;
            if (currentPossible.isEmpty())
            {
                lastPossibleResultPoints = null;
            }
            else
            {
                possibleResultPoints = new HashSet<ResultPoint>(5);
                lastPossibleResultPoints = currentPossible;
                paint.setAlpha(OPAQUE);
                paint.setColor(resultPointColor);
                for (ResultPoint point : currentPossible)
                {
                    canvas.drawCircle(frame.left + point.getX(), frame.top
                            + point.getY(), 6.0f, paint);//画扫描到的可能的点
                }
            }
            if (currentLast != null)
            {
                paint.setAlpha(OPAQUE / 2);
                paint.setColor(resultPointColor);
                for (ResultPoint point : currentLast)
                {
                    canvas.drawCircle(frame.left + point.getX(), frame.top
                            + point.getY(), 3.0f, paint);
                }
            }

            // Request another update at the animation interval, but only repaint the laser line,
            // not the entire viewfinder mask.
            postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top,
                    frame.right, frame.bottom);
        }
    }

    public void drawViewfinder()
    {
        resultBitmap = null;
        invalidate();
    }

    /**
     * Draw a bitmap with the result points highlighted instead of the live scanning display.
     *
     * @param barcode An image of the decoded barcode.
     */
    public void drawResultBitmap(Bitmap barcode)
    {
        resultBitmap = barcode;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point)
    {
        possibleResultPoints.add(point);
    }

    public int reSetColor(int startInt) {

        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endA = startA/2;


      return  ((startA + (endA - startA)) << 24)
                | (startR << 16)
                | (startG  << 8)
                | (startB );
    }

}
