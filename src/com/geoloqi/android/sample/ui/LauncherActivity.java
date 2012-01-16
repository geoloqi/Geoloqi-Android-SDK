package com.geoloqi.android.sample.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.geoloqi.android.sample.Constants;
import com.geoloqi.android.sample.R;
import com.geoloqi.android.sample.receiver.SampleReceiver;
import com.geoloqi.android.sdk.service.LQService;
import com.geoloqi.android.sdk.service.LQService.LQBinder;
import com.geoloqi.android.sdk.ui.LQSettingsActivity;

/**
 * <p>...</p>
 * 
 * @author Tristan Waddington
 */
public class LauncherActivity extends Activity implements SampleReceiver.OnLocationChangedListener {
    public static final String TAG = "LauncherActivity";

    private LQService mService;
    private boolean mBound;
    private SampleReceiver mLocationReceiver = new SampleReceiver();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Start the tracking service
        Intent intent = new Intent(this, LQService.class);
        intent.setAction(LQService.ACTION_START_WITH_ANONYMOUS_USER);
        intent.putExtra(LQService.EXTRA_SDK_ID, Constants.LQ_SDK_ID);
        intent.putExtra(LQService.EXTRA_SDK_SECRET, Constants.LQ_SDK_SECRET);
        startService(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        
        // Bind to the tracking service so we can call public methods on it
        Intent intent = new Intent(this, LQService.class);
        bindService(intent, mConnection, 0);
        
        // Wire up the sample location receiver
        final IntentFilter filter = new IntentFilter();
        filter.addAction(SampleReceiver.ACTION_LOCATION_CHANGED);
        registerReceiver(mLocationReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        
        // Unbind from LQService
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
        
        // Unregister our location receiver
        unregisterReceiver(mLocationReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
        case R.id.settings:
            startActivity(new Intent(this, LQSettingsActivity.class));
            return true;
        }
        return false;
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                // We've bound to LocalService, cast the IBinder and get LocalService instance.
                LQBinder binder = (LQBinder) service;
                mService = binder.getService();
                mBound = true;
            } catch (ClassCastException e) {
                // Pass
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    @Override
    public void onLocationChanged(Location location) {
        TextView latitudeView = (TextView) findViewById(R.id.location_lat);
        TextView longitudeView = (TextView) findViewById(R.id.location_long);
        
        latitudeView.setText(Double.toString(location.getLatitude()));
        longitudeView.setText(Double.toString(location.getLongitude()));
    }
}
