package com.lzy.androidlibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.lzy.androidlibrary.R;


/**
 * 自定义Imageview，能保持宽高比，点击响应
 *
 */
public class LClickMaskRatioImageView extends AppCompatImageView{
	/**
	 * 自定义view中用于帮助计算view大小
	 */
	private LViewAspectRatioMeasurer varm;
	private int MaskColor;
	private final int defaultMaskColor=0x33000000;
	
	public LClickMaskRatioImageView(Context context) {
		this(context, null);
	}

	public LClickMaskRatioImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	/**
	 * 自定义view需要覆盖的父类构造方法，可以通过参数读取xml中的自定义属性值
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public LClickMaskRatioImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if(varm==null){
			varm = new LViewAspectRatioMeasurer();;
		}
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LClickMaskRatioImageView);
		//读取xml中的自定义属性值
		float ratio=a.getFloat(R.styleable.LClickMaskRatioImageView_ratio, -1);
		MaskColor=a.getColor(R.styleable.LClickMaskRatioImageView_maskColor, defaultMaskColor);
		setRatio(ratio);
		a.recycle();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if(varm!=null&&varm.getRatio()>0){
			varm.measure(widthMeasureSpec, heightMeasureSpec);
			setMeasuredDimension(varm.getMeasuredWidth(), varm.getMeasuredHeight());
		}else{
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}
	
	@Override
	public void setImageBitmap(Bitmap bm) {
		setRatio(bm.getWidth()/(float)bm.getHeight());
		super.setImageBitmap(bm);
	}
	
	@Override
	public void setImageDrawable(Drawable drawable) {
		setRatio(drawable.getIntrinsicWidth()/(float)drawable.getIntrinsicHeight());
		super.setImageDrawable(drawable);
	}
	
	@Override
	public void setImageResource(int resId) {
		Drawable d=getResources().getDrawable(resId);
		setImageDrawable(d);
	}

	/**
	 * 设置view宽高比
	 * @param ratio
	 */
	public void setRatio(float ratio){
		if(ratio>0){
			if(varm==null){
				varm = new LViewAspectRatioMeasurer();;
			}
			varm.setRatio(ratio);
		}
	}
	
	@Override
	protected void drawableStateChanged() {
		super.drawableStateChanged();
		invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(isClickable()&&isPressed()){
			canvas.drawColor(MaskColor);
		}
	}
}
