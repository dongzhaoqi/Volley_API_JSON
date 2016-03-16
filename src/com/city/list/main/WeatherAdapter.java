package com.city.list.main;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WeatherAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, String>>datas;
	private LayoutInflater inflater;
	
	public WeatherAdapter(Context mContext, List<Map<String, String>> datas) {
		this.mContext = mContext;
		this.datas = datas;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int arg0) {
		return datas.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		if(convertView == null){
			convertView = inflater.inflate(R.layout.weather_item, null);
			holder = new ViewHolder();
			holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
			holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
			holder.tv_max = (TextView) convertView.findViewById(R.id.tv_max);
			holder.tv_min = (TextView) convertView.findViewById(R.id.tv_min);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tv_date.setText(datas.get(position).get("tv_date"));
		holder.tv_status.setText(datas.get(position).get("tv_status"));
		holder.tv_max.setText("×î¸ßÎÂ:" + datas.get(position).get("tv_max"));
		holder.tv_min.setText("×îµÍÎÂ:" + datas.get(position).get("tv_min"));
		
		return convertView;
	}

	static class ViewHolder{
		private TextView tv_date;
		private TextView tv_status;
		private TextView tv_max;
		private TextView tv_min;
	}
}