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
public final class ViewfinderView extends View {

    private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192,
            128, 64};

    private static final long ANIMATION_DELAY = 10L;
    private static final int OPAQUE = 0xFF;


    private int ScreenRate;


    public int CORNER_WIDTH = 3;

    public int CORNER_LENGTH = 5;
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

    public ViewfinderView(Context context, int time, int color) {
        super(context);
        density = context.getResources().getDisplayMetrics().density;

        ScreenRate = (int) (25 * density);

        paint = new Paint();
        paintLine = new Paint();
        Resources resources = getResources();
        maskColor = resources.getColor(R.color.viewfinder_mask);
        resultColor = resources.getColor(R.color.backgroud);
        frameColor = color;
        frameBaseColor = reSetColor(frameColor);
        laserColor = resources.getColor(R.color.viewfinder_laser);
        resultPointColor = resources.getColor(R.color.possible_result_points);
        scannerAlpha = 0;
        possibleResultPoints = new HashSet<ResultPoint>(5);
        drawLine = true;
        scanTime = time;
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
    }

    @Override
    public void onDraw(Canvas canvas) {
        Rect frame = CameraManager.get().getFramingRect();
        if (frame == null) {
            return;
        }

        if (!isFirst) {
            isFirst = true;
            slideTop = frame.top + CORNER_WIDTH;
            slideBottom = frame.bottom - CORNER_WIDTH;

            SPEEN_DISTANCE = (slideBottom - slideTop) / ((scanTime / 16) + 2);


        }

        int width = canvas.getWidth();
        int height = canvas.getHeight();


        paint.setColor(resultBitmap != null ? resultColor : maskColor);
        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1,
                paint);
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);

        if (resultBitmap != null) {
            paint.setAlpha(OPAQUE);
            canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
        } else {
            paint.setColor(frameColor);

            paint.setColor(Color.TRANSPARENT);


            canvas.drawRect(frame.right, frame.top
                            + ScreenRate, frame.right + CORNER_WIDTH,
                    frame.bottom - ScreenRate, paint);

            canvas.drawRect(frame.left + ScreenRate, frame.bottom,
                    frame.right - ScreenRate, frame.bottom + CORNER_WIDTH, paint);

            paint.setColor(frameColor);

            canvas.drawRoundRect(frame.left - CORNER_WIDTH, frame.top - CORNER_WIDTH, frame.left + CORNER_LENGTH, frame.top, RXY, RXY, paint);
            canvas.drawRoundRect(frame.left - CORNER_WIDTH, frame.top - CORNER_WIDTH, frame.left, frame.top + CORNER_LENGTH, RXY, RXY, paint);


            canvas.drawRoundRect(frame.left - CORNER_WIDTH, frame.bottom - CORNER_LENGTH, frame.left, frame.bottom + CORNER_WIDTH, RXY, RXY, paint);

            canvas.drawRoundRect(frame.left - CORNER_WIDTH, frame.bottom, frame.left + CORNER_LENGTH, frame.bottom + CORNER_WIDTH, RXY, RXY, paint);

            canvas.drawRoundRect(frame.right - CORNER_LENGTH, frame.top - CORNER_WIDTH, frame.right + CORNER_WIDTH, frame.top, RXY, RXY, paint);
            canvas.drawRoundRect(frame.right, frame.top - CORNER_WIDTH, frame.right + CORNER_WIDTH, frame.top + CORNER_LENGTH, RXY, RXY, paint);

            canvas.drawRoundRect(frame.right - CORNER_LENGTH, frame.bottom, frame.right + CORNER_WIDTH, frame.bottom + CORNER_WIDTH, RXY, RXY, paint);
            canvas.drawRoundRect(frame.right, frame.bottom - CORNER_LENGTH, frame.right + CORNER_WIDTH, frame.bottom + CORNER_WIDTH, RXY, RXY, paint);

            if (drawLine) {
                slideTop += SPEEN_DISTANCE;
                if (slideTop >= slideBottom) {
                    slideTop = frame.top + CORNER_WIDTH;
                }

                paintLine.setColor(frameColor);

                Shader mShader = new LinearGradient(frame.left + CORNER_WIDTH, slideTop, frame.right
                        - CORNER_WIDTH, slideTop + MIDDLE_LINE_WIDTH, new int[]{Color.TRANSPARENT, frameBaseColor, frameColor, frameColor, frameColor, frameColor, frameColor, frameBaseColor, Color.TRANSPARENT}, null, Shader.TileMode.CLAMP);

                paintLine.setShader(mShader);
                canvas.drawRect(frame.left + CORNER_WIDTH, slideTop, frame.right
                        - CORNER_WIDTH, slideTop + MIDDLE_LINE_WIDTH, paintLine);


            }

            paint.setColor(Color.WHITE);
            paint.setTextSize(TEXT_SIZE * density);
            paint.setAlpha(0x40);
            paint.setTypeface(Typeface.create("System", Typeface.BOLD));
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(
                    ShowText,
                    width / 2, frame.bottom + TEXT_PADDING_TOP * density,
                    paint);

            Collection<ResultPoint> currentPossible = possibleResultPoints;
            Collection<ResultPoint> currentLast = lastPossibleResultPoints;
            if (currentPossible.isEmpty()) {
                lastPossibleResultPoints = null;
            } else {
                possibleResultPoints = new HashSet<ResultPoint>(5);
                lastPossibleResultPoints = currentPossible;
                paint.setAlpha(OPAQUE);
                paint.setColor(resultPointColor);
                for (ResultPoint point : currentPossible) {
                    canvas.drawCircle(frame.left + point.getX(), frame.top
                            + point.getY(), 6.0f, paint);
                }
            }
            if (currentLast != null) {
                paint.setAlpha(OPAQUE / 2);
                paint.setColor(resultPointColor);
                for (ResultPoint point : currentLast) {
                    canvas.drawCircle(frame.left + point.getX(), frame.top
                            + point.getY(), 3.0f, paint);
                }
            }
            postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top,
                    frame.right, frame.bottom);
        }
    }

    public void drawViewfinder() {
        resultBitmap = null;
        invalidate();
    }

    /**
     * Draw a bitmap with the result points highlighted instead of the live scanning display.
     *
     * @param barcode An image of the decoded barcode.
     */
    public void drawResultBitmap(Bitmap barcode) {
        resultBitmap = barcode;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        possibleResultPoints.add(point);
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
