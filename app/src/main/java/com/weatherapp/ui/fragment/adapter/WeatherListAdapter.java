package com.weatherapp.ui.fragment.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.weatherapp.R;
import com.weatherapp.wrapper.AppWeatherObject;

import java.util.ArrayList;


public class WeatherListAdapter extends BaseAdapter {

    Activity mActivity;
    ArrayList<AppWeatherObject> mList;

    public WeatherListAdapter(Activity activity,  ArrayList<AppWeatherObject> list) {
        mActivity = activity;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        AppWeatherObject conversationForView = mList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.weather_list_row, parent, false);
            holder = createViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tempTextView.setText(conversationForView.getDisplayTemperature());
        holder.cityNameTextView.setText(conversationForView.getCityName());
        return convertView;
    }

    //Update adapter with new ArrayList
    public void updateAdapter(ArrayList<AppWeatherObject> newList) {
        mList = newList;
        notifyDataSetChanged();
    }

    public AppWeatherObject getItemAtPosition(int position) {
        return mList.get(position);
    }

    private ViewHolder createViewHolder(View convertView) {
        ViewHolder holder = new ViewHolder();
        holder.tempTextView = (TextView) convertView.findViewById(R.id.temperature);
        holder.cityNameTextView = (TextView) convertView.findViewById(R.id.city_name);
        return holder;
    }

    class ViewHolder {
        TextView tempTextView, cityNameTextView;
    }
}
