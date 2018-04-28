package com.lzy.androidlibrary.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础滑动适配器，用于继承
 * 
 * @author linzhiyong
 * @time 2015年6月4日15:39:37
 * @email wflinzhiyong@163.com
 * 
 */
public class LBasePagerAdapter<T> extends PagerAdapter {

	/**
	 * 上下文对象
	 */
	protected Context ctx;
	/**
	 * 存放实体的集合
	 */
	protected List<T> list;
	/**
	 * 实例化布局的对象
	 */
	protected final LayoutInflater mInflater;

	public LBasePagerAdapter(Context ctx) {
		super();
		this.ctx = ctx;
		this.list = new ArrayList<T>();
		this.mInflater = LayoutInflater.from(ctx);
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

}
