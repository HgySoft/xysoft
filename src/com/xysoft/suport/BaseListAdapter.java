package com.xysoft.suport;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class BaseListAdapter<T> extends BaseAdapter{
	
	private List<T> list = new ArrayList<>();
	private Context context;
	private Integer resource;

	public BaseListAdapter(Context context, Integer resource) {
		this.context = context;
		this.resource = resource;
	}
	
	public T get(int position) {
		return list.get(position);
	}
	
	public void add(T item) {
		list.add(item);
		notifyDataSetChanged();
	}
	
	public void remove(int position) {
		list.remove(position);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public T getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			convertView = LayoutInflater.from(context).inflate(resource, null);
		}
		initView(position, convertView, parent);
		return convertView;
	}
	
	protected abstract void initView(int position, View convertView, ViewGroup parent);

}
