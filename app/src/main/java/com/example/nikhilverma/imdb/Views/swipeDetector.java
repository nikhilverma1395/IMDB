package com.example.nikhilverma.imdb.Views;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Nikhil Verma on 11-01-2015.
 */
public class swipeDetector implements View.OnTouchListener {
    public static enum Action {
        LR,
        RL,
        TB,
        BT,
        None
    }

    private static final int MIN_DISTANCE = 100;
    private float downX, downY, upX, upY;
    private Action mSwipeDetected = Action.None;

    public boolean swipeDetected() {
        return mSwipeDetected != Action.None;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}