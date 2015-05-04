package com.nikhilvermavit.nikhilverma.imdb.Views;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by Nikhil Verma on 11-01-2015.
 */
public class swipeDetector extends GestureDetector.SimpleOnGestureListener {


    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return super.onFling(e1, e2, velocityX, velocityY);
    }

    static interface SimpleGestureListener {
        void onSwipe(int direction);

        void onDoubleTap();

    }

}

