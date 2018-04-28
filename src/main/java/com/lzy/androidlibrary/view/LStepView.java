package com.lzy.androidlibrary.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.androidlibrary.R;

import java.util.List;

/**
 * Description:带有标题的步骤指示器
 * 步骤指示器，支持横向/总想展示，支持颜色/线条/完成步骤等设置，标题支持List<String> list<View>设置
 *
 * @author linzhiyong
 * @time 2017年7月4日10:48:40
 * @email wflinzhiyong@163.com
 *
 */
public class LStepView extends FrameLayout {
    public static final String TAG = "StepView";
    /** 步骤指示器 */
    private LStepBar mStepBar;
    /** 用来存放显示步骤名称的布局 */
    private FrameLayout mTitleGroup;
    /** 所有步骤的标题 */
    private List<String> mStepTitles;
    /** 所有步骤的内容 */
    private List<View> mStepViews;

    public LStepView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public LStepView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public LStepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LStepView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context mContext, AttributeSet attrs, int defStyleAttr) {
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.LStepView, defStyleAttr, 0);
        if (array.getInteger(R.styleable.LStepView_orientation, 0) == LStepBar.HORIZONTAL) {
            LayoutInflater.from(mContext).inflate(R.layout.step_view_horizontal, this, true);
        } else {
            LayoutInflater.from(mContext).inflate(R.layout.step_view_vertical, this, true);
        }
        mStepBar = (LStepBar) this.findViewById(R.id.step_bar);
        mTitleGroup = (FrameLayout) this.findViewById(R.id.step_title);

        mStepBar.setLineHeight(array.getDimensionPixelOffset(R.styleable.LStepView_lineheight, LStepBar.DEFAULT_LINE_HEIGHT));
        mStepBar.setSmallRadius(array.getDimensionPixelOffset(R.styleable.LStepView_smallradius, LStepBar.DEFAULT_SMALL_CIRCLE_RADIUS));
        mStepBar.setLargeRadius(array.getDimensionPixelOffset(R.styleable.LStepView_largeradius, LStepBar.DEFAULT_LARGE_CIRCLE_RADIUS));
        mStepBar.setUnDoneColor(array.getColor(R.styleable.LStepView_undonecolor, LStepBar.COLOR_BAR_UNDONE));
        mStepBar.setDoneColor(array.getColor(R.styleable.LStepView_donecolor, LStepBar.COLOR_BAR_DONE));
        mStepBar.setTotalStep(array.getInteger(R.styleable.LStepView_totalstep, 0));
        mStepBar.setCompleteStep(array.getInteger(R.styleable.LStepView_completestep, 0));
        mStepBar.setOrientation(array.getInteger(R.styleable.LStepView_orientation, 0));

        //在StepBar布局完成之后开始添加title
        mStepBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                initStepTitle();
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    mStepBar.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    mStepBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
        array.recycle();
    }

    private void initStepTitle() {
        if (mStepTitles == null && mStepViews == null) {
            return;
        }
        mTitleGroup.removeAllViews();

        int stepNum = mStepBar.getTotalStep();
        for (int i = 0; i < stepNum; i++) {
            final float stepPos = mStepBar.getPositionByStep(i + 1);
            View contentView = null;
            if (mStepTitles != null) {
                TextView title = new TextView(this.getContext());
                title.setTextSize(14);
                title.setText(mStepTitles.get(i));
//                title.setSingleLine();
                contentView = title;

            } else {
                contentView = mStepViews.get(i);
            }

            final View finalContentView = contentView;
            contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (mStepBar.getOrientation() == LStepBar.HORIZONTAL) {
                        finalContentView.setTranslationX(stepPos - finalContentView.getMeasuredWidth() / 2);
                    } else {
                        finalContentView.setTranslationY(stepPos - finalContentView.getMeasuredHeight() / 2);
                    }
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        finalContentView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        finalContentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            });
            LayoutParams lp = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            mTitleGroup.addView(contentView, lp);
        }
        changeTitleColor(mStepBar.getCompleteStep());
    }

    public void setStepTitles(List<String> stepTitles) {
        this.mStepTitles = stepTitles;
        mStepBar.setTotalStep(stepTitles == null ? 0 : stepTitles.size());
    }

    public void setStepViewTitles(List<View> mStepViews) {
        this.mStepViews = mStepViews;
        mStepBar.setTotalStep(mStepViews == null ? 0 : mStepViews.size());
    }

    public void setLineHeight(float mLineHeight) {
        mStepBar.setLineHeight(mLineHeight);
    }

    public void setSmallRadius(float mSmallRadius) {
        mStepBar.setSmallRadius(mSmallRadius);
    }

    public void setLargeRadius(float mLargeRadius) {
        mStepBar.setLargeRadius(mLargeRadius);
    }

    public void setUnDoneColor(int mUnDoneColor) {
        mStepBar.setUnDoneColor(mUnDoneColor);
    }

    public void setDoneColor(int mDoneColor) {
        mStepBar.setDoneColor(mDoneColor);
    }

    /**
     * 获取步骤总数
     *
     * @return
     */
    public int getTotalStep() {
        return mStepBar.getTotalStep();
    }

    /**
     * 进入下一个步骤
     */
    public void nextStep() {
        mStepBar.nextStep();
        changeTitleColor(mStepBar.getCompleteStep());
    }

    /**
     * 重置步骤
     */
    public void reset() {
        mStepBar.reset();
        changeTitleColor(0);
    }

    public void setCompleteStep(int step) {
        mStepBar.setCompleteStep(step);
        changeTitleColor(step);
    }

    public int getCompleteStep() {
        return mStepBar.getCompleteStep();
    }

    private void changeTitleColor(int index) {
        if (mTitleGroup == null || mTitleGroup.getChildCount() == 0) {
            return;
        }

        for (int i = 0; i < mTitleGroup.getChildCount(); i++) {
            if (!(mTitleGroup.getChildAt(i) instanceof TextView)) {
                return;
            }
            TextView titleView = (TextView) mTitleGroup.getChildAt(i);
            if (index == i + 1) {
                titleView.setTextColor(mStepBar.getmDoneColor());
            } else {
                titleView.setTextColor(mStepBar.getmUnDoneColor());
            }
        }
    }
}
