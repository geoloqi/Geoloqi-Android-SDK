package com.geoloqi.android.sample.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.geoloqi.android.sample.Constants;
import com.geoloqi.android.sample.R;
import com.geoloqi.android.sample.receiver.SampleReceiver;
import com.geoloqi.android.sdk.LQTracker;
import com.geoloqi.android.sdk.LQTracker.LQTrackerProfile;
import com.geoloqi.android.sdk.provider.LQDatabaseHelper;
import com.geoloqi.android.sdk.service.LQService;
import com.geoloqi.android.sdk.service.LQService.LQBinder;

/**
 * <p>...</p>
 * 
 * @author Tristan Waddington
 */
public class LauncherActivity extends Activity implements SampleReceiver.OnLocationChangedListener,
        SampleReceiver.OnTrackerProfileChangedListener, SampleReceiver.OnLocationUploadedListener {
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
        filter.addAction(SampleReceiver.ACTION_TRACKER_PROFILE_CHANGED);
        filter.addAction(SampleReceiver.ACTION_LOCATION_CHANGED);
        filter.addAction(SampleReceiver.ACTION_LOCATION_UPLOADED);
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
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return false;
    }

    /**
     * Display the number of batched location fixes waiting to be sent.
     */
    private void showBatchedLocationCount() {
        TextView updates = (TextView) findViewById(R.id.batched_updates);
        if (updates != null) {
            final LQTracker tracker = mService.getTracker();
            final LQDatabaseHelper helper = new LQDatabaseHelper(this);
            final SQLiteDatabase db = helper.getWritableDatabase();
            final Cursor c = tracker.getBatchedLocationFixes(db);
            updates.setText(String.format("%d batched updates",
                            c.getCount()));
            c.close();
            db.close();
        }
    }

    /**
     * Display the values from the last recorded location fix.
     * @param location
     */
    private void showCurrentLocation(Location location) {
        TextView latitudeView = (TextView) findViewById(R.id.location_lat);
        if (latitudeView != null) {
            latitudeView.setText(Double.toString(location.getLatitude()));
        }
        
        TextView longitudeView = (TextView) findViewById(R.id.location_long);
        if (longitudeView != null) {
            longitudeView.setText(Double.toString(location.getLongitude()));
        }
        
        TextView accuracyView = (TextView) findViewById(R.id.location_accuracy);
        if (accuracyView != null) {
            accuracyView.setText(String.valueOf(location.getAccuracy()));
        }
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
                
                // Display the current tracker profile
                TextView profileView = (TextView) findViewById(R.id.tracker_profile);
                if (profileView != null) {
                    profileView.setText(mService.getTracker().getProfile().toString());
                }
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
    public void onTrackerProfileChanged(LQTrackerProfile oldProfile,
                    LQTrackerProfile newProfile) {
        TextView profileView = (TextView) findViewById(R.id.tracker_profile);
        if (profileView != null) {
            profileView.setText(newProfile.toString());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        showBatchedLocationCount();
        showCurrentLocation(location);
    }

    @Override
    public void onLocationUploaded(int count) {
        showBatchedLocationCount();
    }
}
