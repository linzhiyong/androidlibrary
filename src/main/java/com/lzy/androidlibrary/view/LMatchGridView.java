package com.lzy.androidlibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 自定义GridView, 解决ScrollView嵌套ListView/GridView自适应问题
 * <p>
 * 直接在ScollView中嵌套ListView和GridView的话，ListView和GridView只会显示一部分内容，
 * 不能显示完整，需要重写ListView和GridView中的onMeasure方法
 *
 * @author linzhiyong
 * @time 2016年11月14日17:21:46
 * @email wflinzhiyong@163.com
 * @desc
 */
public class LMatchGridView extends GridView {

    public LMatchGridView(Context context) {
        super(context);
    }

    public LMatchGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LMatchGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 重写onMeasure方法, 解决ScrollView嵌套ListView/GridView自适应问题
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
