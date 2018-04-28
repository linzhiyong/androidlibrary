package com.lzy.androidlibrary.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.androidlibrary.R;
import com.lzy.androidlibrary.util.LImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 可圆盘菜单类,带动画效果
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @time 2017/7/14
 * @desc
 */
public class LAnimationCircleMenuLayout extends ViewGroup {

    /**
     * 直径
     */
    private int mRadius;
    /**
     * 该容器内child item的默认尺寸
     */
    private static final float RADIO_DEFAULT_CHILD_DIMENSION = 1 / 4f;
    /**
     * 菜单的中心child的默认尺寸
     */
    private float RADIO_DEFAULT_CENTERITEM_DIMENSION = 1 / 4f;
    /**
     * 该容器的内边距,无视padding属性，如需边距请用该变量
     */
    private static final float RADIO_PADDING_LAYOUT = 1 / 12f;
    /**
     * 该容器的内边距,无视padding属性，如需边距请用该变量
     */
    private float mPadding = -1;
    /**
     * 布局时的开始角度, 默认从第一象限开始绘制
     */
    private double mStartAngle = 0;
    /**
     * 菜单项的文本
     */
    private String[] mItemTexts;
    /**
     * 菜单项的图标
     */
    private int[] mItemImgs;
    /**
     * 菜单项的图标
     */
    private String[] mItemNetImgs;

    /**
     * 菜单的个数
     */
    private int mMenuItemCount;

    private int mMenuItemLayoutId = R.layout.circle_menu_item;

    /**
     * 圆盘中心菜单id
     */
    private int id_circle_menu_item_center;
    /**
     * 圆盘中心菜单图标
     */
    private int id_circle_menu_item_center_icon;
    /**
     * 圆盘中心菜单图标
     */
    private String id_circle_menu_item_center_icon_net;

    /**
     * 设置是否是开启状态, 默认开启
     */
    private boolean isOpen;
    /**
     * 默认菜单收起放开动画时间
     */
    private static final long ANIMATION_DURATION = 800;
    /**
     * 是否在动画中，如果=true，则禁止子菜单点击响应
     */
    private boolean isDurationAnimation;

    private List<Point> itemPoints;
    private Point centerPoint;

    class Point {

        private int x;

        private int y;

        public Point() {
        }

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }

    public LAnimationCircleMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 无视padding
        setPadding(0, 0, 0, 0);
//        setBackgroundResource(R.drawable.circle_menu_shape);
        this.itemPoints = new ArrayList<>();
        this.centerPoint = new Point();
        this.isOpen = true;
    }

    /**
     * 设置布局的宽高，并策略menu item宽高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int resWidth = 0;
        int resHeight = 0;

        /**
         * 根据传入的参数，分别获取测量模式和测量值
         */
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        /**
         * 如果宽或者高的测量模式非精确值
         */
        if (widthMode != MeasureSpec.EXACTLY
                || heightMode != MeasureSpec.EXACTLY) {
            // 主要设置为背景图的高度
            resWidth = getSuggestedMinimumWidth();
            // 如果未设置背景图片，则设置为屏幕宽高的默认值
            resWidth = resWidth == 0 ? getDefaultWidth() : resWidth;

            resHeight = getSuggestedMinimumHeight();
            // 如果未设置背景图片，则设置为屏幕宽高的默认值
            resHeight = resHeight == 0 ? getDefaultWidth() : resHeight;
        } else {
            // 如果都设置为精确值，则直接取小值；
            resWidth = resHeight = Math.min(width, height);
        }

        setMeasuredDimension(resWidth, resHeight);

        // 获得直径
        mRadius = Math.max(getMeasuredWidth(), getMeasuredHeight());
        centerPoint = new Point(getMeasuredWidth() / 2, getMeasuredHeight() / 2);

        // menu item数量
        final int count = getChildCount();
        // menu item尺寸
        int childSize = (int) (mRadius * RADIO_DEFAULT_CHILD_DIMENSION);
        // menu item测量模式
        int childMode = MeasureSpec.EXACTLY;

        // 迭代测量
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                continue;
            }

            // 计算menu item的尺寸；以及和设置好的模式，去对item进行测量
            int makeMeasureSpec = -1;

            if (child.getId() == id_circle_menu_item_center) {
                makeMeasureSpec = MeasureSpec.makeMeasureSpec(
                        (int) (mRadius * RADIO_DEFAULT_CENTERITEM_DIMENSION),
                        childMode);
            } else {
                makeMeasureSpec = MeasureSpec.makeMeasureSpec(childSize, childMode);
            }
            child.measure(makeMeasureSpec, makeMeasureSpec);
        }

        mPadding = mPadding == -1 ? RADIO_PADDING_LAYOUT * mRadius : mPadding;
    }

    /**
     * MenuItem的点击事件接口
     *
     * @author zhy
     */
    public interface OnMenuItemClickListener {
        void itemClick(View view, int pos);

        void itemCenterClick(View view);
    }

    /**
     * MenuItem的点击事件接口
     */
    private OnMenuItemClickListener mOnMenuItemClickListener;

    /**
     * 设置MenuItem的点击事件接口
     *
     * @param mOnMenuItemClickListener
     */
    public void setOnMenuItemClickListener(OnMenuItemClickListener mOnMenuItemClickListener) {
        this.mOnMenuItemClickListener = mOnMenuItemClickListener;
    }

    /**
     * 设置menu item的位置
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int layoutRadius = mRadius;

        // Laying out the child views
        final int childCount = getChildCount();

        int left, top;
        // menu item 的尺寸
        int cWidth = (int) (layoutRadius * RADIO_DEFAULT_CHILD_DIMENSION);

        final View centerView = findViewById(id_circle_menu_item_center);

        // 根据menu item的个数，计算角度
        float angleDelay = getChildCount() == 1 ? 0 : 360 / (getChildCount() - 1);

        itemPoints.clear();
        // 遍历去设置menuitem的位置
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);

            if (child.getId() == id_circle_menu_item_center) {
                continue;
            }

            if (child.getVisibility() == GONE) {
                continue;
            }

            mStartAngle %= 360;

            float tmp;
            // 计算，中心点到menu item中心的距离
//            if (centerView == null) {
                tmp = layoutRadius / 2f - cWidth / 2 - mPadding;
//            } else {
//                tmp = (layoutRadius / 2f - centerView.getMeasuredWidth() / 2 - mPadding) / 2f + centerView.getMeasuredWidth() / 2;
//            }

            // tmp cosa 即menu item中心点的横坐标
            left = layoutRadius / 2
                    + (int) Math.round(tmp
                    * Math.cos(Math.toRadians(mStartAngle)) - 1 / 2f
                    * cWidth);
            // tmp sina 即menu item的纵坐标
            top = layoutRadius / 2
                    + (int) Math.round(tmp
                    * Math.sin(Math.toRadians(mStartAngle)) - 1 / 2f
                    * cWidth);

            child.layout(left, top, left + cWidth, top + cWidth);
            itemPoints.add(new Point(left + cWidth / 2, top + cWidth / 2));
            // 叠加尺寸
            mStartAngle += angleDelay;
        }

        // 找到中心的view，如果存在设置onclick事件
        final View cView = findViewById(id_circle_menu_item_center);
        if (cView != null) {
            cView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFastDoubleClick(ANIMATION_DURATION)) {
                        return;
                    }

                    if (mOnMenuItemClickListener != null) {
                        mOnMenuItemClickListener.itemCenterClick(v);
                    }
                    RotateAnimation rotateAnimation = null;
                    if (isOpen) {
                        rotateAnimation = new RotateAnimation(0.0f, +360.0f,
                                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    } else {
                        rotateAnimation = new RotateAnimation(+360.0f, 0.0f,
                                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    }
                    rotateAnimation.setDuration(ANIMATION_DURATION);
                    cView.startAnimation(rotateAnimation);

                    // 设置子菜单收缩，打开
                    setChildViewAnimationsPlus(isOpen);
                    isOpen = !isOpen;
                }
            });
            // 设置center item位置
            int cl = layoutRadius / 2 - cView.getMeasuredWidth() / 2;
            int cr = cl + cView.getMeasuredWidth();
            cView.layout(cl, cl, cr, cr);
        }

    }

    /**
     * 设置子空间open close 及其动画效果
     * 使用先进行动画，再移动控件到目标位置方法，移动之后控件闪烁现象，弃用
     * @desc 使用 setChildViewAnimationsPlus 代替
     */
    @Deprecated
    private void setChildViewAnimations(final boolean isOpen) {
        if (getChildCount() == 0 || (getChildCount() == 1 && findViewById(id_circle_menu_item_center) != null)) {
            return;
        }

        // 如果中心菜单不为null，则从第二个子View开始
        int start = 0;
        if (findViewById(id_circle_menu_item_center) != null) {
            start = 1;
        }

        for (int i = start; i < getChildCount(); i++) {
            final View item = getChildAt(i);
            if (item == null) {
                continue;
            }

            Point itemPoint = null;
            if (start == 0) {
                itemPoint = itemPoints.get(i);
            } else {
                itemPoint = itemPoints.get(i - 1);
            }

            AnimationSet animationSet = new AnimationSet(true);

            // 旋转动画
            RotateAnimation rotateAnimation = null;
            // 平移动画
            TranslateAnimation translateAnimation = null;
            // 透明度动画
            AlphaAnimation alphaAnimation = null;

            item.setVisibility(INVISIBLE);
            if (isOpen) {
                rotateAnimation = new RotateAnimation(
                        0.0f,
                        +360.0f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f);
                translateAnimation = new TranslateAnimation(
                        0,
                        centerPoint.getX() - itemPoint.getX(),
                        0,
                        centerPoint.getY() - itemPoint.getY());
                alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            }
            else {
                rotateAnimation = new RotateAnimation(
                        +360.0f,
                        0.0f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f);
                translateAnimation = new TranslateAnimation(
                        0,
                        itemPoint.getX() - centerPoint.getX(),
                        0,
                        itemPoint.getY() - centerPoint.getY());
                alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            }

            animationSet.setDuration(ANIMATION_DURATION);
            animationSet.addAnimation(rotateAnimation);
            animationSet.addAnimation(translateAnimation);
            animationSet.addAnimation(alphaAnimation);

            item.startAnimation(animationSet);

            final Point finalItemPoint = itemPoint;
            animationSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    /*************移动控件到目标位置********************/
                    int width = item.getWidth();
                    int height = item.getHeight();

                    int left = item.getLeft();
                    int top = item.getTop();

                    if (isOpen) {
                        left += centerPoint.getX() - finalItemPoint.getX();
                        top += centerPoint.getY() - finalItemPoint.getY();
                    } else {
                        left += finalItemPoint.getX() - centerPoint.getX();
                        top += finalItemPoint.getY() - centerPoint.getY();
                    }

                    item.layout(left, top, left + width, top + height);
                    /*************移动控件到目标位置********************/

                    if (isOpen) {
                        item.setVisibility(INVISIBLE);
                    } else {
                        item.setVisibility(VISIBLE);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    }

    /**
     * 设置子空间open close 及其动画效果
     * @desc 解决后设置位置闪烁问题，思路：先移动控件到目标位置，然后进行相对目标位置的动画实现
     */
    private void setChildViewAnimationsPlus(final boolean isOpen) {
        if (getChildCount() == 0 || (getChildCount() == 1 && findViewById(id_circle_menu_item_center) != null)) {
            return;
        }

        // 如果中心菜单不为null，则从第二个子View开始
        int start = 0;
        if (findViewById(id_circle_menu_item_center) != null) {
            start = 1;
        }

        isDurationAnimation = true;
        for (int i = start; i < getChildCount(); i++) {
            final View item = getChildAt(i);
            if (item == null) {
                continue;
            }

            Point itemPoint = null;
            if (start == 0) {
                itemPoint = itemPoints.get(i);
            } else {
                itemPoint = itemPoints.get(i - 1);
            }

            AnimationSet animationSet = new AnimationSet(true);

            // 旋转动画
            RotateAnimation rotateAnimation = null;
            // 平移动画
            TranslateAnimation translateAnimation = null;
            // 透明度动画
            AlphaAnimation alphaAnimation = null;

            item.setVisibility(INVISIBLE);
            if (isOpen) {
                rotateAnimation = new RotateAnimation(
                        0.0f,
                        +360.0f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f);
                translateAnimation = new TranslateAnimation(
                        -centerPoint.getX() + itemPoint.getX(),
                        0,
                        -centerPoint.getY() + itemPoint.getY(),
                        0);
                alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            }
            else {
                rotateAnimation = new RotateAnimation(
                        +360.0f,
                        0.0f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f);
                translateAnimation = new TranslateAnimation(
                        centerPoint.getX() - itemPoint.getX(),
                        0,
                        centerPoint.getY() - itemPoint.getY(),
                        0);
                alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            }

            animationSet.setDuration(ANIMATION_DURATION);
            animationSet.addAnimation(rotateAnimation);
            animationSet.addAnimation(translateAnimation);
            animationSet.addAnimation(alphaAnimation);

            /*************移动控件到目标位置********************/
            int width = item.getWidth();
            int height = item.getHeight();

            int left = item.getLeft();
            int top = item.getTop();

            if (isOpen) {
                left += centerPoint.getX() - itemPoint.getX();
                top += centerPoint.getY() - itemPoint.getY();
            } else {
                left += itemPoint.getX() - centerPoint.getX();
                top += itemPoint.getY() - centerPoint.getY();
            }

            item.layout(left, top, left + width, top + height);
            /*************移动控件到目标位置********************/

            item.startAnimation(animationSet);
            animationSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (isOpen) {
                        item.setVisibility(INVISIBLE);
                    } else {
                        item.setVisibility(VISIBLE);
                        isDurationAnimation = false;
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    /**
     * 主要为了action_down时，返回true
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    /**
     * 设置菜单条目的图标和文本
     *
     * @param resIds 本地图标资源
     * @param texts
     */
    public void setMenuItemIconsAndTexts(int[] resIds, String[] texts) {
        mItemImgs = resIds;
        mItemTexts = texts;

        // 参数检查
        if (resIds == null && texts == null) {
            throw new IllegalArgumentException("菜单项文本和图片至少设置其一");
        }

        // 初始化mMenuCount
        mMenuItemCount = resIds == null ? texts.length : resIds.length;

        if (resIds != null && texts != null) {
            mMenuItemCount = Math.min(resIds.length, texts.length);
        }

        addMenuItems();
    }

    /**
     * 设置菜单条目的图标和文本
     *
     * @param netImages 网络图标资源
     * @param texts
     */
    public void setMenuItemIconsAndTexts(String[] netImages, String[] texts) {
        mItemNetImgs = netImages;
        mItemTexts = texts;

        // 参数检查
        if (netImages == null && texts == null) {
            throw new IllegalArgumentException("菜单项文本和图片至少设置其一");
        }

        // 初始化mMenuCount
        mMenuItemCount = netImages == null ? texts.length : netImages.length;

        if (netImages != null && texts != null) {
            mMenuItemCount = Math.min(netImages.length, texts.length);
        }

        addMenuItems();
    }

    /**
     * 设置MenuItem的布局文件，必须在setMenuItemIconsAndTexts之前调用
     *
     * @param mMenuItemLayoutId
     */
    public void setMenuItemLayoutId(int mMenuItemLayoutId) {
        this.mMenuItemLayoutId = mMenuItemLayoutId;
    }

    /**
     * 添加菜单项
     */
    private void addMenuItems() {
        LayoutInflater mInflater = LayoutInflater.from(getContext());

        /**
         * 根据用户设置的参数，初始化view
         */
        for (int i = 0; i < mMenuItemCount; i++) {
            final int j = i;
            View view = mInflater.inflate(mMenuItemLayoutId, this, false);
            ImageView iv = (ImageView) view.findViewById(R.id.id_circle_menu_item_image);
            TextView tv = (TextView) view.findViewById(R.id.id_circle_menu_item_text);

            if (iv != null) {
                iv.setVisibility(View.VISIBLE);
                if (mItemImgs != null && mItemImgs.length >= mMenuItemCount) {
                    iv.setImageResource(mItemImgs[i]);
                }
                else if (mItemNetImgs != null && mItemNetImgs.length >= mMenuItemCount) {
                    LImageLoaderUtil.displayImage(mItemNetImgs[i], iv);
                }
                iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnMenuItemClickListener != null && !isDurationAnimation) {
                            mOnMenuItemClickListener.itemClick(v, j);
                        }
                    }
                });
            }
            if (tv != null) {
                if (mItemTexts != null && mItemTexts.length >= mMenuItemCount) {
                    tv.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(mItemTexts[i]) && mItemTexts[i].length() > 5) {
                        tv.setTextSize(10);
                    } else {
                        tv.setTextSize(12);
                    }
                    tv.setText(mItemTexts[i]);
                }
            }

            // 添加view到容器中
            addView(view);
        }
    }

    /**
     * 设置内边距的比例
     *
     * @param mPadding
     */
    public void setPadding(float mPadding) {
        this.mPadding = mPadding;
    }

    /**
     * 获得默认该layout的尺寸
     *
     * @return
     */
    private int getDefaultWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return Math.min(outMetrics.widthPixels, outMetrics.heightPixels);
    }

    public int getId_circle_menu_item_center() {
        return id_circle_menu_item_center;
    }

    public void setId_circle_menu_item_center(int id_circle_menu_item_center, int icon) {
        this.id_circle_menu_item_center = id_circle_menu_item_center;
        this.id_circle_menu_item_center_icon = icon;
        if (findViewById(id_circle_menu_item_center) instanceof ImageView) {
            ((ImageView) findViewById(id_circle_menu_item_center)).setImageResource(icon);
        }
    }

    public void setId_circle_menu_item_center(int id_circle_menu_item_center, String icon) {
        this.id_circle_menu_item_center = id_circle_menu_item_center;
        this.id_circle_menu_item_center_icon_net = icon;
        if (findViewById(id_circle_menu_item_center) instanceof ImageView) {
            ImageView imageView = ((ImageView) findViewById(id_circle_menu_item_center));
            LImageLoaderUtil.displayImage(icon, imageView);
        }
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public double getStartAngle() {
        return mStartAngle;
    }

    public void setStartAngle(double mStartAngle) {
        this.mStartAngle = mStartAngle;
    }

    /**
     * 防止按钮连续点击
     *
     * @param interval
     * @return
     */
    private long lastClickTime = 0;
    public boolean isFastDoubleClick(long interval) {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < interval) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

}
