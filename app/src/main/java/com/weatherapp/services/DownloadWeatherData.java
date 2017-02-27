package com.weatherapp.services;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.weatherapp.R;
import com.weatherapp.WeatherRetrievedCallback;
import com.weatherapp.wrapper.AppWeatherObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class DownloadWeatherData extends AsyncTask<String, Void, AppWeatherObject> {

    private WeatherRetrievedCallback<AppWeatherObject> mCallback;
    static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?zip=";
    static final String END_URL = ",us&units=imperial&APPID=3d3ad3ee7e744da48ad325e7b625741e";
    Context mContext;

    public DownloadWeatherData(Context context, WeatherRetrievedCallback<AppWeatherObject> callback) {
        mCallback = callback;
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        if (mCallback != null) {
            NetworkInfo networkInfo = mCallback.getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isConnected() ||
                    (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                            && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                Toast.makeText(mContext, mContext.getString(R.string.no_internet_toast_message), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected AppWeatherObject doInBackground(String... prams) {
        AppWeatherObject appWeatherObject = new AppWeatherObject();
        try {
            String zip = prams[0];
            URL url = new URL(BASE_URL + zip + END_URL);
            appWeatherObject = downloadUrl(url, zip);
        } catch(Exception e) {
            Log.d("EXCEPTION", e.toString());
        }
        return appWeatherObject;
    }

    @Override
    protected void onPostExecute(AppWeatherObject appWeatherObject) {
        super.onPostExecute(appWeatherObject);
        mCallback.updateFromDownload(appWeatherObject);
    }

    private AppWeatherObject downloadUrl(URL url, String zip) throws IOException {
        InputStream stream = null;
        HttpURLConnection connection = null;
        String result = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            setConnectionAttributes(connection);
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            // Retrieve the response body as an InputStream.
            stream = connection.getInputStream();
            if (stream != null) {
                result = readStream(stream, 500);
            }
        } finally {
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }

        AppWeatherObject appWeatherObject = WeatherJSONParser.createAppWeatherObject(result);
        appWeatherObject.setZipCode(Integer.valueOf(zip));
        return appWeatherObject;
    }

    private void setConnectionAttributes(HttpURLConnection connection) {
        try {
            connection.setReadTimeout(3000);
            connection.setConnectTimeout(3000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
    }

    private String readStream(InputStream stream, int maxLength) throws IOException {
        String result = null;
        InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[maxLength];
        int numChars = 0;
        int readSize = 0;
        while (numChars < maxLength && readSize != -1) {
            numChars += readSize;
            readSize = reader.read(buffer, numChars, buffer.length - numChars);
        }

        if (numChars != -1) {
            numChars = Math.min(numChars, maxLength);
            result = new String(buffer, 0, numChars);
        }
        return result;
    }
}
