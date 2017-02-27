package com.weatherapp.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.weatherapp.MainActivity;
import com.weatherapp.R;
import com.weatherapp.WeatherRetrievedCallback;
import com.weatherapp.services.DownloadWeatherData;
import com.weatherapp.ui.fragment.adapter.WeatherListAdapter;
import com.weatherapp.wrapper.AppWeatherObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class WeatherListFragment extends android.support.v4.app.ListFragment {

    public static final String TAG = "NetworkFragment";
    ListView mListView;
    WeatherListAdapter mAdapter;

    public static WeatherListFragment getInstance(FragmentManager fragmentManager, String url) {
        WeatherListFragment networkFragment = new WeatherListFragment();
        fragmentManager.beginTransaction().add(networkFragment, TAG).commit();
        return networkFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_weather_list, container, false);
        mListView = (ListView) layout.findViewById(android.R.id.list);
        return layout;
    }

    @Override
    public void onListItemClick(ListView l, View v, final int position, long id) {
        super.onListItemClick(l, v, position, id);
        final AppWeatherObject appWeatherObject = mAdapter.getItemAtPosition(position);
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setTitle(getString(R.string.delete));
        adb.setMessage(getString(R.string.delete_confirmation, appWeatherObject.getCityName()));
        adb.setNegativeButton(R.string.cancel, null);
        adb.setPositiveButton(getString(R.string.OK), new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ((MainActivity)getActivity()).getAppWeatherService().removeWeatherObject(appWeatherObject);
                mAdapter.notifyDataSetChanged();
            }});
        adb.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void updateAdapter(ArrayList<AppWeatherObject> list) {
        if (mAdapter == null) {
            mAdapter = new WeatherListAdapter(getActivity(), list);
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.updateAdapter(list);
        }
    }
}
