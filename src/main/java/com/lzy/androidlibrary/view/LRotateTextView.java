package com.lzy.androidlibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.lzy.androidlibrary.R;

/**
 * 自定义可旋转文字TextView
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @time 2017/7/26
 * @desc
 */
public class LRotateTextView extends AppCompatTextView {

    private int angle = 0;

    private Rect mBounds;
    private Paint paint;
    private PathEffect effects;

    public LRotateTextView(Context context) {
        super(context);
        init(null, 0);
    }

    public LRotateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public LRotateTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        paint = new Paint();
        paint.setAntiAlias(true); // 设置抗锯齿，如果不设置，加载位图的时候可能会出现锯齿状的边界
        paint.setDither(true); // 设置是否抖动，如果不设置感觉就会有一些僵硬的线条，如果设置图像就会看的更柔和
        paint.setSubpixelText(true); // 设置亚像素，是对文本的一种优化设置，可以让文字看起来更加清晰明显
        paint.setStrokeWidth(5);

        mBounds = new Rect();
        effects = new DashPathEffect(new float[] {6, 6, 6, 6}, 1); // 6px实线，6px虚线

        if (attrs == null) {
            return;
        }
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.LRotateTextView, defStyleAttr, 0);
        angle = array.getInteger(R.styleable.LRotateTextView_angle, 0);
        array.recycle();
    }

    public void setText(String text) {
        if (!TextUtils.isEmpty(text) && text.length() > 3) {
            String str1 = text.substring(0, 2);
            String str2 = text.substring(2);
            super.setText(str1 + "\n" + str2);
        } else {
            super.setText(text);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 旋转角度
        canvas.rotate(angle, getWidth() / 2, getHeight() / 2);
        // 通过TextView属性设置文本
        super.onDraw(canvas);
        // 半径
        int radius = Math.min(getWidth(), getHeight()) / 2 - dp2px(getContext(), 3);

        paint.setColor(getCurrentTextColor());

        // 绘制外圈实线
        paint.setPathEffect(null);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, paint);

        // 绘制内圈虚线
        paint.setPathEffect(effects);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius - dp2px(getContext(), 5), paint);

//        // 绘制文本
//        String text = String.valueOf(getText());
//        paint.getTextBounds(text, 0, text.length(), mBounds);
//        paint.setStyle(Paint.Style.FILL);
//        paint.setPathEffect(null);
//        paint.setTextSize(getTextSize());
//        float textWidth = mBounds.width();
//        float textHeight = mBounds.height();
//        canvas.drawText(text, getWidth() / 2 - textWidth / 2, getHeight() / 2 + textHeight / 2, paint);
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    private int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
