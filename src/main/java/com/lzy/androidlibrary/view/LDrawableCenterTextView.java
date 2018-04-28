package com.lzy.androidlibrary.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 自定义DrawableCenterTextView, 实现图片居中显示
 * 
 * @author linzhiyong
 * @time 2016年11月14日17:21:58
 * @email wflinzhiyong@163.com
 * @desc 目前针对drawableLeft属性
 * 
 */
public class LDrawableCenterTextView extends AppCompatTextView {

	public LDrawableCenterTextView(Context context, AttributeSet attrs,
                                   int defStyle) {
		super(context, attrs, defStyle);
	}

	public LDrawableCenterTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LDrawableCenterTextView(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// 获取drawables集合
		Drawable[] drawables = getCompoundDrawables();
		if (drawables.length > 0) {
			// 获取第1个drawable
			Drawable drawableLeft = drawables[0];
			if (drawableLeft != null) {
				// 计算文本宽度
				float textWidth = getPaint().measureText(getText().toString());
				// 计算drawablePadding
				int drawablePadding = getCompoundDrawablePadding() / 2;
				// 计算drawable宽度
				int drawableWidth = drawableLeft.getIntrinsicWidth();
				// 计算content宽度
				float bodyWidth = textWidth + drawableWidth + drawablePadding;
				// 计算实际控件宽度
				int width = getWidth() - getPaddingLeft() - getPaddingRight();
				// 开始位置
				float start = 0;
				if (bodyWidth > width) {
					start = getPaddingLeft();
				} else {
					start = (getWidth() - bodyWidth) / 2 - getPaddingLeft();
				}
				canvas.translate(start, 0);
			}
		}
		super.onDraw(canvas);
	}
}
