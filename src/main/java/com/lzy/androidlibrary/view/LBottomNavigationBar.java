package com.lzy.androidlibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.androidlibrary.R;
import com.lzy.androidlibrary.util.LImageLoaderUtil;

/**
 * 底部导航tabbar实现类.
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @time 2017/7/30
 * @desc
 */
public class LBottomNavigationBar extends LinearLayout {

    /** 当前活动item */
    private int activeIndex;

    /** 选中颜色 */
    private int activeColor;

    /** 默认颜色 */
    private int unactiveColor;

    /** tabbar监听器 */
    private OnNavigationItemSelectedListener listener;

    public LBottomNavigationBar(Context context) {
        super(context, null);
        init(null, 0);
    }

    public LBottomNavigationBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LBottomNavigationBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        if (attrs == null) {
            return;
        }
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.LBottomNavigationBar, defStyleAttr, 0);
        activeColor = array.getColor(R.styleable.LBottomNavigationBar_activeColor, Color.WHITE);
        unactiveColor = array.getColor(R.styleable.LBottomNavigationBar_unactiveColor, Color.GRAY);
        array.recycle();
    }

    /**
     * 添加底部item
     *
     * @param item
     */
    public void addItemBar(View item) {
        if (item == null) {
            throw new IllegalArgumentException("navigation bar item cannot be null");
        }

        LayoutParams params = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
        item.setLayoutParams(params);
        item.setOnClickListener(new OnItemBarClickListener(getChildCount()));
        addView(item);
    }

    /**
     * 添加底部item
     *
     * @param iconPath
     * @param title
     */
    public void addItemBar(String iconPath, String title) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        View item = layoutInflater.inflate(R.layout.item_tabbar_layout, null);
        ImageView icon = (ImageView) item.findViewById(R.id.tabbar_imageView);
        LImageLoaderUtil.displayImage(iconPath, icon);
        TextView titleView = (TextView) item.findViewById(R.id.tabbar_titleView);
        titleView.setText(title);

        LayoutParams params = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
        item.setLayoutParams(params);
        item.setOnClickListener(new OnItemBarClickListener(getChildCount()));
        addView(item);
    }

    /**
     * 添加底部item
     *
     * @param iconPath
     * @param title
     */
    public void addItemBar(int iconPath, String title) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        View item = layoutInflater.inflate(R.layout.item_tabbar_layout, null);
        ImageView icon = (ImageView) item.findViewById(R.id.tabbar_imageView);
        icon.setImageResource(iconPath);
        TextView titleView = (TextView) item.findViewById(R.id.tabbar_titleView);
        titleView.setText(title);

        LayoutParams params = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
        item.setLayoutParams(params);
        item.setOnClickListener(new OnItemBarClickListener(getChildCount()));
        addView(item);
    }

    /**
     * 设置当前活动item
     * @param index
     */
    public void setActive(int index) {
        for (int i = 0; i < getChildCount(); i++) {
            if (index == i) {
                changeitemStatus(getChildAt(i), getActiveColor());
            } else {
                changeitemStatus(getChildAt(i), getUnactiveColor());
            }
        }
    }

    /**
     * item 项监听器
     */
    private class OnItemBarClickListener implements OnClickListener {

        private int index;

        public OnItemBarClickListener(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            if (index >= getChildCount()) {
                return;
            }

            if (activeIndex == index) {
                if (listener != null) {
                    listener.onNavigationItemSelectedAgain(v, index);
                }
                return;
            }

            activeIndex = index;

            for (int i = 0; i < getChildCount(); i++) {
                if (index == i) {
                    changeitemStatus(getChildAt(i), getActiveColor());
                } else {
                    changeitemStatus(getChildAt(i), getUnactiveColor());
                }
            }

            if (listener != null) {
                listener.onNavigationItemSelected(v, index);
            }
        }
    }

    private void changeitemStatus(View item, int color) {
        if (item instanceof LinearLayout) {
            for (int i = 0; i < ((LinearLayout) item).getChildCount(); i++) {
                View child = ((LinearLayout) item).getChildAt(i);
                if (child instanceof ImageView) {
                    ((ImageView) child).setColorFilter(color, PorterDuff.Mode.SRC_IN);
                }
                else if (child instanceof TextView) {
                    ((TextView) child).setTextColor(color);
                }
            }
        }
        else if (item instanceof ImageView) {
            ((ImageView) item).setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
        else if (item instanceof TextView) {
            ((TextView) item).setTextColor(color);
        }
    }

    public int getActiveColor() {
        return activeColor;
    }

    public void setActiveColor(int activeColor) {
        this.activeColor = activeColor;
    }

    public int getUnactiveColor() {
        return unactiveColor;
    }

    public void setUnactiveColor(int unactiveColor) {
        this.unactiveColor = unactiveColor;
    }

    public int getActiveIndex() {
        return activeIndex;
    }

    public void setOnNavigationItemSelectedListener(OnNavigationItemSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnNavigationItemSelectedListener {

        void onNavigationItemSelected(View view, int position);

        void onNavigationItemSelectedAgain(View view, int position);

    }

}
