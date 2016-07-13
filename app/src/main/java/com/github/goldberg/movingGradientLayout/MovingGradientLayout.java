package com.github.goldberg.movingGradientLayout;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Goldberg on 2016/7/13.
 */

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class MovingGradientLayout extends FrameLayout {


    private int startColor;
    private int endColor;
    private int[] colors;
    private boolean isGradient;
    private boolean isOtherMode;
    private int cyclePeriod;
    private GradientDrawable gradientDrawable;


    public MovingGradientLayout(Context context) {
        this(context, null);
    }

    public MovingGradientLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MovingGradientLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MovingGradientLayout, 0, 0);
        isGradient = typedArray.getBoolean(R.styleable.MovingGradientLayout_isGradient, true);
        isOtherMode = typedArray.getBoolean(R.styleable.MovingGradientLayout_isOtherMode, false);
        int colorsId = typedArray.getResourceId(R.styleable.MovingGradientLayout_colors, -1);
        colors = getResources().getIntArray(colorsId);

        cyclePeriod = typedArray.getInt(R.styleable.MovingGradientLayout_cyclePeriod, 3000);
        typedArray.recycle();
        gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[]{startColor, endColor});
        setBackground(gradientDrawable);
        startAnim();

    }


    private void startAnim() {
        if (isOtherMode) {
            startAnimOtherMode();
            return;
        }

        ValueAnimator valueAnimator = ValueAnimator.ofInt(colors);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                startColor = (int) animation.getAnimatedValue();
            }
        });
        valueAnimator.setEvaluator(new ArgbEvaluator());
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);

        int[] colors1 = colors;

        for (int i = 0; i < colors1.length >> 1; i++) {
            int temp = colors1[i];
            colors1[i] = colors1[colors1.length - 1 - i];
            colors1[colors1.length - 1 - i] = temp;
        }


        ValueAnimator valueAnimator1 = ValueAnimator.ofInt(isGradient ? colors1 : colors);
        valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                endColor = (int) animation.getAnimatedValue();
                gradientDrawable.setColors(new int[]{startColor, endColor});
            }
        });
        valueAnimator1.setEvaluator(new ArgbEvaluator());
        valueAnimator1.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator1.setRepeatCount(ValueAnimator.INFINITE);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(cyclePeriod);
        animatorSet.play(valueAnimator).with(valueAnimator1);
        animatorSet.start();


    }

    private void startAnimOtherMode() {


        startColor = colors[0];
        endColor = colors[colors.length - 1];
        startColor_red = Color.red(startColor);
        startColor_green = Color.green(startColor);
        startColor_blue = Color.blue(startColor);
        endColor_red = Color.red(endColor);
        endColor_green = Color.green(endColor);
        endColor_blue = Color.blue(endColor);
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(CHANGE);
            }
        };
        timer.schedule(timerTask, 0, cyclePeriod/512>32?cyclePeriod/512:32);


    }

    private int startColor_red;
    private int startColor_green;
    private int startColor_blue;
    private int endColor_red;
    private int endColor_green;
    private int endColor_blue;

    boolean startColor_red_plus = true;
    boolean startColor_green_plus = true;
    boolean startColor_blue_plus = true;
    boolean endColor_red_plus = true;
    boolean endColor_green_plus = true;
    boolean endColor_blue_plus = true;

    private void calculateAll() {

        if (startColor_red_plus) {
            startColor_red++;
            if (startColor_red >= 255) {
                startColor_red_plus = false;
            }
        } else {
            startColor_red--;
            if (startColor_red <= 0) {
                startColor_red_plus = true;
            }
        }



        if (startColor_green_plus) {
            startColor_green++;
            if (startColor_green >= 255) {
                startColor_green_plus = false;
            }
        } else {
            startColor_green--;
            if (startColor_green <= 0) {
                startColor_green_plus = true;
            }
        }


        if (startColor_blue_plus) {
            startColor_blue++;
            if (startColor_blue >= 255) {
                startColor_blue_plus = false;
            }
        } else {
            startColor_blue--;
            if (startColor_blue <= 0) {
                startColor_blue_plus = true;
            }
        }



        if (endColor_red_plus) {
            endColor_red++;
            if (endColor_red >= 255) {
                endColor_red_plus = false;
            }
        } else {
            endColor_red--;
            if (endColor_red <= 0) {
                endColor_red_plus = true;
            }
        }


        if (endColor_green_plus) {
            endColor_green++;
            if (endColor_green >= 255) {
                endColor_green_plus = false;
            }
        } else {
            endColor_green--;
            if (endColor_green <= 0) {
                endColor_green_plus = true;
            }
        }

        if (endColor_blue_plus) {
            endColor_blue++;
            if (endColor_blue >= 255) {
                endColor_blue_plus = false;
            }
        } else {
            endColor_blue--;
            if (endColor_blue <= 0) {
                endColor_blue_plus = true;
            }
        }

        gradientDrawable.setColors(new int[]{Color.rgb(startColor_red, startColor_green, startColor_blue),
                Color.rgb(endColor_red, endColor_green, endColor_blue)});


    }

    private static final int CHANGE = Integer.MAX_VALUE - 1023;
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CHANGE:
                    calculateAll();
                    break;
            }

        }
    };

    private Timer timer;
    private TimerTask timerTask;



    public GradientDrawable getGradientDrawable() {
        return gradientDrawable;
    }

}
