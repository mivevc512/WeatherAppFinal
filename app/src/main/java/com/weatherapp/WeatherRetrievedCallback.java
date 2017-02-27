package com.weatherapp;


import android.net.NetworkInfo;

import com.weatherapp.wrapper.AppWeatherObject;

import java.util.ArrayList;


public interface WeatherRetrievedCallback <T> {

    /**
     * Indicates that the callback handler needs to update its appearance or information based on
     * the result of the task. Expected to be called from the main thread.
     */
    void updateFromDownload(AppWeatherObject results);

    /**
     * Get the device's active network status in the form of a NetworkInfo object.
     */
    NetworkInfo getActiveNetworkInfo();

}

