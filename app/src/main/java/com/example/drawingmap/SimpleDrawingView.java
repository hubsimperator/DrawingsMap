package com.example.drawingmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class SimpleDrawingView extends View {

public ArrayList<Path> path_list=new ArrayList<>();
    // setup initial color
    private final int paintColor = Color.RED;
    // defines paint and canvas
    private Paint drawPaint;
    // stores next circle
    private Path path = new Path();

    public SimpleDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setupPaint();
    }

    // Setup paint with color and stroke styles
    private void setupPaint() {
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(9);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void clear(){
        Log.d("TAG", "**********************");
        Log.d("TAG", " Nieudana proba kasowania");
Log.d("TAG",Integer.toString(path_list.size()));

        Log.d("TAG", "**********************");
       path_list.removeAll(path_list);
       path_list.clear();
        for(Path pat:path_list){
           pat.reset();
       }
        invalidate();


    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float pointX = event.getX();
        float pointY = event.getY();
        // Checks for the event that occurs
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(pointX, pointY);
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(pointX, pointY);
                break;
            default:
                return false;
        }
        // Force a view to draw again
        postInvalidate();
        return true;
    }

}
