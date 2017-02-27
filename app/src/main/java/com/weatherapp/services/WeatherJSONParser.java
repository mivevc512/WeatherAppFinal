package com.weatherapp.services;


import android.support.annotation.NonNull;

import com.weatherapp.wrapper.AppWeatherObject;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherJSONParser {

    private static final String MAIN = "main";
    private static final String TEMP = "temp";
    private static final String NAME = "name";
    private static final char END_CITY_NAME_MARKER = '"';
    private static final int NUM_CHARS_BEFORE_CITY_NAME = 7;

    //Extract needed information to create AppWeatherObject
    @NonNull
    public static AppWeatherObject createAppWeatherObject(String jsonResponse) {
        AppWeatherObject appWeatherObject = new AppWeatherObject();
        try {
            JSONObject reader = new JSONObject(jsonResponse);
            JSONObject object = reader.getJSONObject(MAIN);
            float temp = getFloat(TEMP, object);
            String cityName = extractCityName(jsonResponse);
            appWeatherObject.setCityName(cityName);
            appWeatherObject.setTemperature(temp);
            return appWeatherObject;
        } catch (JSONException ex) {

        }
        return appWeatherObject;
    }

    private static float getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

    //Get city name from JSON response
    private static String extractCityName(String response) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean endCityName = false;
        int i = response.indexOf(NAME) + NUM_CHARS_BEFORE_CITY_NAME;
        while (!endCityName) {
            char charAtIndex = response.charAt(i);
            if (charAtIndex == END_CITY_NAME_MARKER) {
                endCityName = true;
            } else {
                stringBuilder.append(charAtIndex);
                i++;
            }
        }
        return stringBuilder.toString();
    }
}
