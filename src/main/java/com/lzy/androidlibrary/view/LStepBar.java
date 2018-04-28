package com.lzy.androidlibrary.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Description:步骤指示器
 *
 * @author linzhiyong
 * @time 2017年7月4日10:48:40
 * @email wflinzhiyong@163.com
 *
 */
public class LStepBar extends View {

    public static final String TAG="StepBar";

    /**未完成的步骤的颜色*/
    public static final int COLOR_BAR_UNDONE=0XFF808080;
    /**完成步骤的颜色*/
    public static final int COLOR_BAR_DONE=0XFF00FF00;
    /**默认线条高度*/
    public static final int DEFAULT_LINE_HEIGHT = 5;
    /**默认小圆的半径*/
    public static final int DEFAULT_SMALL_CIRCLE_RADIUS=10;
    /**默认大圆的半径*/
    public static final int DEFAULT_LARGE_CIRCLE_RADIUS=20;
     /**默认距离边缘的距离*/
    public static final int DEFAULT_PADDING=20;

    private static final int DEFAULT_BEGIN_END_LINE = 30;

    private float mCenterX = 0.0f;
    private float mCenterY = 0.0f;
    private float mLeftX = 0.0f;
    private float mLeftY = 0.0f;
    private float mRightX = 0.0f;
    private float mRightY = 0.0f;
    private float mDistance = 0.0f;


    private float mLineHeight = 0f;

    private float mSmallRadius = 0f;
    private float mLargeRadius = 0f;
    private int mUnDoneColor = COLOR_BAR_UNDONE;
    private int mDoneColor = COLOR_BAR_DONE;
    private int mTotalStep;
    private int mCompleteStep;

    /**
     * 方向
     */
    private int orientation;
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    public LStepBar(Context context) {
        super(context);
        init(context, null, 0);
    }

    public LStepBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public LStepBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LStepBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    /**
     * 用来处理自定义属性
     * 注意：此方法中最好不要涉及计算UI相关尺寸的逻辑，如果一定要在这里计算，那么务必使用{@link #post(Runnable)}
     *
     * @param mContext
     * @param attrs
     * @param defStyeAttr
     */
    private void init(Context mContext, AttributeSet attrs, int defStyeAttr) {
        this.orientation = HORIZONTAL;
    }

    /**
     * 设置总的步骤数
     *
     * @param mTotalStep
     */
    public void setTotalStep(int mTotalStep) {
        if (mTotalStep <= 0) {
//            throw new IllegalArgumentException("步骤总数必须大于0!");
        }
        this.mTotalStep = mTotalStep;
    }

    /**
     * 获取步骤总数
     *
     * @return
     */
    public int getTotalStep() {
        return mTotalStep;
    }

    /**
     * 设置完成的步骤
     *
     * @param mComplteStep 步骤从1开始,如果设置为0,那么没有任何完成的步骤
     */
    public void setCompleteStep(int mComplteStep) {
        if (mComplteStep < 0 || mComplteStep > mTotalStep) {
            return;
        }
        this.mCompleteStep = mComplteStep;
    }

    public int getCompleteStep() {
        return mCompleteStep;
    }

    /**
     * 通过步骤序号，得到此步骤对应的点的位置,主要用于确定标题的位置
     *
     * @param step
     * @return
     */
    public float getPositionByStep(int step) {
        if (step < 1 || step > mTotalStep) {
            throw new IllegalArgumentException("step必须在 1~总步骤数之间!");
        }
        if (orientation == HORIZONTAL) {
            return mLeftX + (step - 1) * mDistance;
        } else {
            return mLeftY + (step - 1) * mDistance;
        }
    }

    /**
     * 进入下一个步骤
     * 如果已经是最后一个步骤,则不做任何处理
     */
    public void nextStep() {
        if (mCompleteStep == mTotalStep) {
            return;
        }
        mCompleteStep++;
        invalidate();
    }

    /**
     * 重置步骤
     */
    public void reset() {
        mCompleteStep = 0;
        invalidate();
    }

    /**
     * View的绘制的第一阶段调用
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getDefaultWidth();
        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(widthMeasureSpec)) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        }

        int height = 120;
        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(heightMeasureSpec)) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        }
        Log.d(TAG, "onMeasure-->width:" + width + " height:" + height);
        setMeasuredDimension(width, height);

    }

    /*
     * 在View的绘制的第二阶段(layout)中，当尺寸发生变化时调用
     * 注意：第二阶段本来是调用onLayout方法，此方法是在onLayout方法中被调用
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //计算位置
        if (orientation == HORIZONTAL) {
            mCenterY = this.getHeight() / 2;
            mLeftX = this.getLeft() + getPaddingLeft();
            mLeftY = mCenterY - mLineHeight / 2;
            mRightX = this.getRight() - getPaddingRight();
            mRightY = mCenterY + mLineHeight / 2;
            Log.d(TAG, "onSizeChanged->mLeftX:" + mLeftX);
            Log.d(TAG, "onSizeChanged->mRightX:" + mRightX);
            if (mTotalStep > 1) {
                mDistance = (mRightX - mLeftX) / (mTotalStep - 1);
                Log.d(TAG, "onSizeChanged->mDistance:" + mDistance);
            }
        } else {
            mCenterX = this.getWidth() / 2;
            mLeftY = this.getTop() + getPaddingTop();
            mRightY = this.getBottom() - getPaddingBottom();
            mLeftX = mCenterX - mLineHeight / 2;
            mRightX = mCenterX + mLineHeight / 2;
            Log.d(TAG, "onSizeChanged->mLeftX:" + mLeftY);
            Log.d(TAG, "onSizeChanged->mRightX:" + mRightY);
            if (mTotalStep > 1) {
                mDistance = (mRightY - mLeftY) / (mTotalStep - 1);
                Log.d(TAG, "onSizeChanged->mDistance:" + mDistance);
            }
        }
    }

    /**
     * View的绘制的第三阶段调用
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mTotalStep <= 0 || mCompleteStep < 0 || mCompleteStep > mTotalStep) {
            return;
        }
        Paint mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(mUnDoneColor);

        float xLoc = 0.0f;
        // 根据方向 绘制横线或者竖线
        if (orientation == HORIZONTAL) {
            xLoc = mLeftX;
            canvas.drawRect(mLeftX - DEFAULT_BEGIN_END_LINE, mLeftY, mRightX + DEFAULT_BEGIN_END_LINE, mRightY, mCirclePaint);
        } else {
            xLoc = mLeftY;
            canvas.drawRect(mLeftX, mLeftY - DEFAULT_BEGIN_END_LINE, mRightX, mRightY + DEFAULT_BEGIN_END_LINE, mCirclePaint);
        }

        //画所有的步骤(圆形)
        for (int i = 0; i < mTotalStep; i++) {
            if (orientation == HORIZONTAL) {
                canvas.drawCircle(xLoc, mLeftY + mLineHeight / 2, mSmallRadius, mCirclePaint);
            } else {
                canvas.drawCircle(mLeftX + mLineHeight / 2, xLoc, mSmallRadius, mCirclePaint);
            }
            xLoc = xLoc + mDistance;
        }

        //画已经完成的步骤(圆形加矩形)
        if (orientation == HORIZONTAL) {
            xLoc = mLeftX;
        } else {
            xLoc = mLeftY;
        }
        mCirclePaint.setColor(mDoneColor);
        for (int i = 0; i < mCompleteStep; i++) {
            if (i == 0) {
                if (orientation == HORIZONTAL) {
                    canvas.drawRect(mLeftX - DEFAULT_BEGIN_END_LINE, mLeftY, mLeftX, mRightY, mCirclePaint);
                } else {
                    canvas.drawRect(mLeftX, mLeftY - DEFAULT_BEGIN_END_LINE, mRightX, mLeftY, mCirclePaint);
                }
            }
            else if (i == mTotalStep - 1) {
                if (orientation == HORIZONTAL) {
                    canvas.drawRect(xLoc - mDistance, mLeftY, xLoc + DEFAULT_BEGIN_END_LINE, mRightY, mCirclePaint);
                } else {
                    canvas.drawRect(mLeftX, xLoc - mDistance, mRightX, xLoc + DEFAULT_BEGIN_END_LINE, mCirclePaint);
                }
            }
            else {
                if (orientation == HORIZONTAL) {
                    canvas.drawRect(xLoc - mDistance, mLeftY, xLoc, mRightY, mCirclePaint);
                } else {
                    canvas.drawRect(mLeftX, xLoc - mDistance, mRightX, xLoc, mCirclePaint);
                }
            }

            if (orientation == HORIZONTAL) {
                canvas.drawCircle(xLoc, mLeftY + mLineHeight / 2, mSmallRadius, mCirclePaint);
            } else {
                canvas.drawCircle(mLeftX + mLineHeight / 2, xLoc, mSmallRadius, mCirclePaint);
            }

            //画当前步骤(加光晕效果)
            if (i == mCompleteStep - 1) {
                mCirclePaint.setColor(getTranspartColorByAlpha(mDoneColor, 0.2f));
                if (orientation == HORIZONTAL) {
                    canvas.drawCircle(xLoc, mLeftY + mLineHeight / 2, mLargeRadius, mCirclePaint);
                } else {
                    canvas.drawCircle(mLeftX + mLineHeight / 2, xLoc, mLargeRadius, mCirclePaint);
                }
            } else {
                xLoc = xLoc + mDistance;
            }

        }
    }

    /**
     * 得到默认的StepBar的宽度
     *
     * @return
     */
    private int getDefaultWidth() {
        int screenWidth = this.getResources().getDisplayMetrics().widthPixels;
        return screenWidth - 2 * dp2px(DEFAULT_PADDING);
    }

    /**
     * dp单位和px单位转换
     *
     * @param dp
     * @return
     */
    public int dp2px(int dp) {
        return (int) (this.getContext().getResources().getDisplayMetrics().density * dp + 0.5);
    }

    /**
     * 将指定的颜色转换成制定透明度的颜色
     *
     * @param color
     * @param ratio
     * @return
     */
    private int getTranspartColorByAlpha(int color, float ratio) {
        int newColor = 0;
        int alpha = Math.round(Color.alpha(color) * ratio);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        newColor = Color.argb(alpha, r, g, b);
        return newColor;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setLineHeight(float mLineHeight) {
        this.mLineHeight = mLineHeight;
    }

    public void setSmallRadius(float mSmallRadius) {
        this.mSmallRadius = mSmallRadius;
    }

    public void setLargeRadius(float mLargeRadius) {
        this.mLargeRadius = mLargeRadius;
    }

    public void setUnDoneColor(int mUnDoneColor) {
        this.mUnDoneColor = mUnDoneColor;
    }

    public void setDoneColor(int mDoneColor) {
        this.mDoneColor = mDoneColor;
    }

    public int getmUnDoneColor() {
        return mUnDoneColor;
    }

    public int getmDoneColor() {
        return mDoneColor;
    }
}
