package com.weatherapp.services;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.weatherapp.WeatherRetrievedCallback;
import com.weatherapp.wrapper.AppWeatherObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class AppWeatherService implements WeatherRetrievedCallback {

    private static final String ZIP_PREFERENCE_KEY = "ZIP_KEY";
    static SharedPreferences mSharedPreferences;
    private static final String myPreference = "mypref";
    private WeatherRetrievedCallback mCallback;
    EventListener mEventListener;
    private DownloadWeatherData mDownloadTask;
    Context mContext;
    private final Object mLock = new Object();
    HashMap<Integer, AppWeatherObject> mCurrentAppWeatherObjects;

    public interface EventListener {
        void handleWeatherResults(ArrayList<AppWeatherObject> results);
    }

    public AppWeatherService(Context context) {
        mContext = context;
        mCallback = this;
        mEventListener = (EventListener) context;
        initAppWeatherService();
    }

    public void initAppWeatherService() {
        mSharedPreferences = mContext.getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        mCurrentAppWeatherObjects = new HashMap<>(10);
        startDownload();
    }

    public void saveZipCodes() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        Set<String> zipCodes = new HashSet<>(10);
        for (AppWeatherObject appWeatherObject : mCurrentAppWeatherObjects.values()) {
            zipCodes.add(String.valueOf(appWeatherObject.getZipCode()));
        }
        editor.putStringSet(ZIP_PREFERENCE_KEY, zipCodes);
        editor.apply();
    }

    public Set<String> getSavedZips() {
        return mSharedPreferences.getStringSet(ZIP_PREFERENCE_KEY, new HashSet<String>());
    }

    private void startDownload() {
        Set<String> savedZips = getSavedZips();
        for (String zip : savedZips) {
            startNewDownload(zip);
        }
    }

    public void startNewDownload(String zip) {
        mDownloadTask = new DownloadWeatherData(mContext, mCallback);
        mDownloadTask.execute(zip);
    }

    public void removeWeatherObject(AppWeatherObject appWeatherObject) {
        mCurrentAppWeatherObjects.remove(appWeatherObject.getZipCode());
        saveZipCodes();
        mEventListener.handleWeatherResults(new ArrayList<>(mCurrentAppWeatherObjects.values()));
    }

    @Override
    public void updateFromDownload(AppWeatherObject results) {
        synchronized (mLock) {
            mCurrentAppWeatherObjects.put(results.getZipCode(), results);
        }
        saveZipCodes();
        mEventListener.handleWeatherResults(new ArrayList<>(mCurrentAppWeatherObjects.values()));
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }
}
