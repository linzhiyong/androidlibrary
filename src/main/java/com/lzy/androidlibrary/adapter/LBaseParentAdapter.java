package com.lzy.androidlibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础适配器，用于其他适配器继承
 * 
 * @author linzhiyong
 * @time 2015年6月4日11:29:05
 * @email wflinzhiyong@163.com
 * 
 */
public class LBaseParentAdapter<T> extends BaseAdapter {

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

	public LBaseParentAdapter(Context ctx) {
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
	public Object getItem(int position) {
		return list == null ? null : list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

}
