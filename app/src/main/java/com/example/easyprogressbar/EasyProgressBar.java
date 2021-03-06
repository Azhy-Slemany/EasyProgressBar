package com.example.easyprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class EasyProgressBar extends View {

    public static final String TAG = "EasyProgressBar";

    public static final int LEFT_TO_RIGHT = 0;
    public static final int RIGHT_TO_LEFT = 1;
    public static final int UP_TO_DOWN = 2;
    public static final int DOWN_TO_UP = 3;

    private int backColor, foreColor, border;
    private int fillDirection;
    private double value, maxValue;
    private boolean isAnimating = false;
    Paint paint;

    public EasyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.EasyProgressBar,
                0, 0);

        try {
            backColor = a.getColor(R.styleable.EasyProgressBar_backColor, Color.WHITE);
            foreColor = a.getColor(R.styleable.EasyProgressBar_foreColor, Color.BLACK);
            border = a.getColor(R.styleable.EasyProgressBar_border, Color.BLACK);
            fillDirection = a.getInt(R.styleable.EasyProgressBar_fillDirection, 0);
            value = a.getFloat(R.styleable.EasyProgressBar_value, 0);
            if(value < 0){
                this.value = 0;
                Log.e(TAG, "value is smaller than zero, the value has been changed to zero.");
            }
            maxValue = a.getFloat(R.styleable.EasyProgressBar_maxValue, 100);
            if(maxValue <= 0) {
                Log.e(TAG, "max value is smaller or equals to zero, the max value is 100.");
                return;
            }
        } finally {
            a.recycle();
        }

        paint = new Paint();
    }

    public EasyProgressBar(Context context, int backColor, int foreColor, int fillDirection) {
        super(context);

        this.backColor = backColor;
        this.foreColor = foreColor;
        if(fillDirection < 0 || fillDirection > 3){
            Log.e(TAG, "Filling Direction is a wrong choice, Left to right would be chosen.");
            this.fillDirection = LEFT_TO_RIGHT;
        }else this.fillDirection = fillDirection;
        this.border = Color.BLACK;
        this.value = 0;
        this.maxValue = 100;

        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getWidth(), h = getHeight();

        paint.setStyle(Paint.Style.FILL);

        paint.setColor(backColor);
        canvas.drawRect(0, 0, w, h, paint);

        paint.setColor(foreColor);
        switch (fillDirection) {
            case LEFT_TO_RIGHT:
                canvas.drawRect(0, 0, (int) (w * (1 - (maxValue - value) / maxValue)), h, paint);
                break;
            case RIGHT_TO_LEFT:
                canvas.drawRect((int) (w * ((maxValue - value) / maxValue)), 0, w, h, paint);
                break;
            case UP_TO_DOWN:
                canvas.drawRect(0, 0, w, (int) (h * (1 - (maxValue - value) / maxValue)), paint);
                break;
            default:
                canvas.drawRect(0, (int) (h * ((maxValue - value) / maxValue)), w, h, paint);
                break;
        }

        paint.setStyle(Paint.Style.STROKE);

        paint.setColor(border);
        paint.setStrokeWidth(5);
        canvas.drawRect(0, 0, w - 1, h - 1, paint);
    }

    public void setForeColor(int foreColor){
        this.foreColor = foreColor;
        this.invalidate();
    }

    public int getForeColor(){return foreColor;}

    public void setBackColor(int foreColor){
        this.backColor = backColor;
        this.invalidate();
    }

    public int getBackColor(){return foreColor;}

    public void setBorderColor(int borderColor){
        this.border = borderColor;
        this.invalidate();
    }

    public int getBorderColor(){return border;}

    public void setFillingDirection(int fillDirection){
        if(fillDirection < 0 || fillDirection > 3){
            Log.e(TAG, "Filling Direction is a wrong choice, Left to right would be chosen.");
            this.fillDirection = LEFT_TO_RIGHT;
        }else this.fillDirection = fillDirection;
        this.invalidate();
    }

    public int getFillingDirection(){return fillDirection;}

    public void setValue(double progressValue){
        if(progressValue > maxValue) {
            this.value = maxValue;
            Log.e(TAG, "value is bigger than the max value, the value has been changed to max value.");
        }
        else if(progressValue < 0) {
            this.value = 0;
            Log.e(TAG, "value is smaller than zero, the value has been changed to zero.");
        }
        else
            this.value = progressValue;
        invalidate();
    }

    public double getValue(){
        return value;
    }

    public void setMaxValue(double maxValue){
        if(maxValue <= 0) {
            Log.e(TAG, "max value is smaller or equals to zero, the max value has not been changed.");
            return;
        }

        this.value = (value / this.maxValue) * maxValue;
        this.maxValue = maxValue;
        invalidate();
    }

    public double getMaxValue(){
        return maxValue;
    }

    public void addValue(double valueToAdd){
        if(value + valueToAdd > maxValue) {
            Log.e(TAG, "value will be bigger than the max value, the value has been changed to the max value.");
            this.value = maxValue;
        }
        else if(valueToAdd < 0) {
            Log.e(TAG, "value is smaller than zero, the value has not been changed.");
            return;
        }
        else
            this.value += valueToAdd;
        invalidate();
    }

    public void addValueAnimated(double valueToAdd_, final double seconds, final int frames){
        if(valueToAdd_ < 0) {
            Log.e(TAG, "value is smaller than zero, the value has not been changed.");
            return;
        }

        if(valueToAdd_ == 0)
            return;

        if(isAnimating) {
            Log.e(TAG, "couldn't add value while animating.");
            return;
        }

        isAnimating = true;

        final double valueToAdd;
        if(valueToAdd_ + value > maxValue)
            valueToAdd = maxValue - value;
        else valueToAdd = valueToAdd_;

        final double before = value;
        final int delay = (int)(seconds * 1000 / frames);

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                setValue(value + (valueToAdd / frames));
                if(value >= before + valueToAdd){
                    setValue(before + valueToAdd);
                    isAnimating = false;
                    return;
                }
                handler.postDelayed(this, delay);
            }
        };
        handler.postDelayed(runnable, delay);
    }

    public void addValueAnimated(double valueToAdd_, final double seconds, final int frames, final Runnable afterAdded){
        if(valueToAdd_ < 0) {
            Log.e(TAG, "value is smaller than zero, the value has not been changed.");
            return;
        }

        if(valueToAdd_ == 0)
            return;

        if(isAnimating) {
            Log.e(TAG, "couldn't add value while animating.");
            return;
        }

        isAnimating = true;

        final double valueToAdd;
        if(valueToAdd_ + value > maxValue)
            valueToAdd = maxValue - value;
        else valueToAdd = valueToAdd_;

        final double before = value;
        final int delay = (int)(seconds * 1000 / frames);

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                setValue(value + (valueToAdd / frames));
                if(value >= before + valueToAdd){
                    setValue(before + valueToAdd);
                    isAnimating = false;
                    afterAdded.run();
                    return;
                }
                handler.postDelayed(this, delay);
            }
        };
        handler.postDelayed(runnable, delay);
    }
}
