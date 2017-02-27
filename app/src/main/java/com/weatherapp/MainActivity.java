package com.weatherapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.weatherapp.services.AppWeatherService;
import com.weatherapp.ui.fragment.WeatherListFragment;
import com.weatherapp.wrapper.AppWeatherObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AppWeatherService.EventListener {

    private WeatherListFragment mWeatherListFragment;
    AppWeatherService mAppWeatherService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        if (mAppWeatherService == null) {
            mAppWeatherService = new AppWeatherService(this);
        }
        mWeatherListFragment = WeatherListFragment.getInstance(getSupportFragmentManager(), "");
        startWeatherListFragment(mWeatherListFragment);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAppWeatherService.saveZipCodes();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_item) {
            QuantityDialogFragment dialog = new QuantityDialogFragment();
            dialog.show(getSupportFragmentManager(), "Dialog");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startWeatherListFragment(WeatherListFragment weatherListFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, weatherListFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void onUserSelectValue(String selectedValue) {
        if (selectedValue.length() < 5 || selectedValue.length() > 5) {
            String errorMessage = "Unknown zip code: " + selectedValue;
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        } else {
            mAppWeatherService.startNewDownload(selectedValue);
        }
    }

    @Override
    public void handleWeatherResults(ArrayList<AppWeatherObject> results) {
        if (mWeatherListFragment != null) {
            mWeatherListFragment.updateAdapter(results);
        }
    }

    public AppWeatherService getAppWeatherService() {
        return mAppWeatherService;
    }

    static public class QuantityDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

        private EditText editQuantity;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            editQuantity = new EditText(getActivity());
            editQuantity.setPadding(20, 0, 20, 0);
            editQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
            return new AlertDialog.Builder(getActivity()).setTitle(R.string.app_name).setMessage("Please enter zip:")
                    .setPositiveButton("OK", this).setNegativeButton("CANCEL", null).setView(editQuantity).create();
        }

        @Override
        public void onClick(DialogInterface dialog, int position) {
            String value = editQuantity.getText().toString();
            Log.d("Quantity: ", value);
            MainActivity callingActivity = (MainActivity) getActivity();
            callingActivity.onUserSelectValue(value);
            dialog.dismiss();
        }
    }
}

