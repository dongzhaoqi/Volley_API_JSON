package com.city.list.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dialog.CustomProgressDialog;

public class MainActivity extends Activity {

	private Button btn;
	private TextView tv_city;
	private ListView list_weather;
	private WeatherAdapter mAdapter;
	private static final int REQUEST_CITY = 0;
	private RequestQueue mQueue; // volley���������
	private static final String apikey = "1a03add595481b304fdef3660c02d97d";	//�˴�Ϊ�������apikey
	private List<Map<String, String>>weatherDatas = new ArrayList<Map<String,String>>();
	private String date,max,min,tv_status;
	private CustomProgressDialog processDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mQueue = Volley.newRequestQueue(getApplicationContext());	//�½��������

		tv_city = (TextView) findViewById(R.id.tv_city);
		list_weather = (ListView) findViewById(R.id.list_weather);
		
		btn = (Button) findViewById(R.id.selectBtn);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, CityList.class);
				
				//��ΪҪ���ճ����б���ѡ��ĳ��У����Դ˴���startActivityForResult
				startActivityForResult(intent, REQUEST_CITY);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case REQUEST_CITY:
			if (resultCode == RESULT_OK) {
				showProcessDialog();
				String cityName = data.getStringExtra("cityName");
				
				//ȥ�����һ����"��"����Ϊ��������в�����"��"�֣�  �������ݿ��е���Щ���в�ѯ����
				String city = cityName.substring(0, cityName.length() - 1);
				weatherDatas.clear();		//��仰Ҫ����,�����µ����ݻ����ԭ�����ݵĺ���
				getWeather(city);
			}
			break;

		default:
			break;
		}
	}
	
	private void getWeather(final String city) {
		String url = "http://apis.baidu.com/heweather/weather/free?city="+city;
		
		//����ɹ�
		Listener<String>listener = new Listener<String>() {
			@Override
			public void onResponse(String arg0) {
				dismissProcessDialog();
				Log.d("onResponse", arg0);
				tv_city.setText(city + "����������");
				parseData(arg0);
				mAdapter = new WeatherAdapter(MainActivity.this, weatherDatas);
				list_weather.setAdapter(mAdapter);
			}
			
		};
		
		//����ʧ��
		ErrorListener errorListener = new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
				Log.d("onErrorResponse", arg0.toString());
				tv_city.setText("�ݲ�֧�ָó���!");
			}
		};
		
		StringRequest request = new StringRequest(Method.GET, url,
				listener, errorListener){
			
			@Override
			public Map<String, String> getHeaders()
					throws AuthFailureError {
				Map<String, String>mHeaders = new HashMap<String, String>();
				mHeaders.put("apikey", apikey);
				return mHeaders;
			}
			
		};
		mQueue.add(request);
	}
	

	//�Է��ص�json���ݽ��н���
	private void parseData(String arg0) {
		try {
			JSONArray results = new JSONObject(arg0).optJSONArray("HeWeather data service 3.0");
			JSONArray daily_forecast = results.optJSONObject(0).optJSONArray("daily_forecast");
			for(int i = 0; i < daily_forecast.length(); i++){
				HashMap<String, String>map = new HashMap<String, String>();
				
				date = daily_forecast.optJSONObject(i).optString("date");
				tv_status = daily_forecast.optJSONObject(i).optJSONObject("cond").optString("txt_d");
				max = daily_forecast.optJSONObject(i).optJSONObject("tmp").optString("max");
				min = daily_forecast.optJSONObject(i).optJSONObject("tmp").optString("min");
				
				map.put("tv_date", date);
				map.put("tv_status", tv_status);
				map.put("tv_max", max);
				map.put("tv_min", min);
				
				weatherDatas.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void showProcessDialog() {

		if (processDialog == null){
			processDialog = new CustomProgressDialog(this,"loading...");
			processDialog.show();
			processDialog.setCanceledOnTouchOutside(true);
		}
		if (processDialog.isShowing() == false)
			processDialog.show();
	}

	public void dismissProcessDialog() {

		if (processDialog != null)
			processDialog.dismiss();
	}
	
}