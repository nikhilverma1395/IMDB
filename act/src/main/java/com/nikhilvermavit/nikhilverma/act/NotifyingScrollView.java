package com.nikhilvermavit.nikhilverma.act;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Project :    HeaderViewPager
 * Author :     Mathieu
 * Date :       27/10/2014
 */

public class NotifyingScrollView extends ScrollView {
    public interface OnScrollChangedListener {
        void onScrollChanged(ScrollView who, int x, int y, int oldX, int oldY);
    }

    private OnScrollChangedListener onScrollChangedListener;

    public NotifyingScrollView(Context context) {
        super(context);
    }

    public NotifyingScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NotifyingScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);
        if (onScrollChangedListener != null) {
            onScrollChangedListener.onScrollChanged(this, x, y, oldX, oldY);
        }
    }

    public void setOnScrollChangedListener(OnScrollChangedListener listener) {
        onScrollChangedListener = listener;
    }

}