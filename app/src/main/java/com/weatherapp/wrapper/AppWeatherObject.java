package com.weatherapp.wrapper;

/*
    Client side object used to store weather information about city
 */
public class AppWeatherObject {

    double mTemperature;
    String mCityName;
    int mZipCode;

    public AppWeatherObject() { }

    public String getCityName() {
        return mCityName;
    }

    public void setCityName(String cityName) {
        this.mCityName = cityName;
    }

    public void setTemperature(double temp) {
        mTemperature = temp;
    }

    public double getTemperature() {
        return mTemperature;
    }

    public String getDisplayTemperature() {
        return String.valueOf((int)mTemperature) + (char) 0x00B0;
    }

    public void setZipCode(int zipCode) {
        this.mZipCode = zipCode;
    }

    public int getZipCode() {
        return mZipCode;
    }
}
