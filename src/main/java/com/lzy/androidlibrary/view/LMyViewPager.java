package com.lzy.androidlibrary.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 自定义ViewPager，解决手势pointerIndex out of rangejava.lang.IllegalArgumentException: pointerIndex out of range
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @time 2017/7/21
 * @desc
 */
public class LMyViewPager extends ViewPager {

    public LMyViewPager(Context context) {
        super(context);
    }

    public LMyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

}
